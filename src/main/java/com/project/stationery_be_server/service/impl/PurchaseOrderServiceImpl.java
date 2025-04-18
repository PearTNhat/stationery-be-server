package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.dto.request.MomoRequest;
import com.project.stationery_be_server.dto.request.PurchaseOrderRequest;
import com.project.stationery_be_server.dto.response.MomoResponse;
import com.project.stationery_be_server.dto.response.PurchaseOrderDetailResponse;
import com.project.stationery_be_server.dto.response.PurchaseOrderResponse;
import com.project.stationery_be_server.entity.*;
import com.project.stationery_be_server.repository.CartRepository;
import com.project.stationery_be_server.repository.PurchaseOrderRepository;
import com.project.stationery_be_server.repository.UserPromotionRepository;
import com.project.stationery_be_server.repository.UserRepository;
import com.project.stationery_be_server.service.PurchaseOrderService;
import lombok.AccessLevel;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.hc.client5.http.utils.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.project.stationery_be_server.entity.PurchaseOrder.Status.PENDING;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    WebClient webClient;
    PurchaseOrderRepository purchaseOrderRepository;
    CartRepository cartRepository;
    UserRepository userRepository;
    UserPromotionRepository userPromotionRepository;
    @Value(value = "${momo.partnerCode}")
    @NonFinal
    String partnerCode;
    @Value(value = "${momo.accessKey}")
    @NonFinal
    String accessKey;
    @Value(value = "${momo.secretKey}")
    @NonFinal
    String secretKey;
    @Value(value = "${momo.redirectUrl}")
    @NonFinal
    String redirectUrl;
    @Value(value = "${momo.ipnUrl}")
    @NonFinal
    String ipnUrl;
    @Value(value = "${momo.requestType}")
    @NonFinal
    String requestType;
    @Value(value = "${momo.endpoint}")
    @NonFinal
    String endpoint;
    @Value(value = "${momo.accessKey}")
    @NonFinal
    String accessKeyMomo;
    @Value(value = "${momo.secretKey}")
    @NonFinal
    String secretKeyMomo;
    @Value(value = "${momo.urlCheckTransaction}")
    @NonFinal
    String urlCheckTransaction;
    @Transactional
    @Override
    public PurchaseOrderResponse createOrderFromCart(PurchaseOrderRequest request) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> cartItems = cartRepository.findByUser_UserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }


        BigDecimal totalAmountBeforeUsePromotion = BigDecimal.ZERO;
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .createdAt(new Date())
                .user(user)
                .amount(totalAmountBeforeUsePromotion)
                .pdfUrl(request.getPdfUrl())
                .productPromotionId(null)
                .status(PENDING)
                .userPromotionId(request.getUserPromotionId())
                .purchaseOrderDetails(new HashSet<>())
                .build();

        for (Cart cart : cartItems) {
            PurchaseOrderDetailId detailId = new PurchaseOrderDetailId(purchaseOrder.getPurchaseOrderId(), cart.getProductDetail().getProductDetailId());
            PurchaseOrderDetail orderDetail = PurchaseOrderDetail.builder()
                    .purchaseOrderDetailId(detailId)
                    .quantity(cart.getQuantity())
                    .productDetail(cart.getProductDetail())
                    .purchaseOrder(purchaseOrder)
                    .build();

            ProductDetail productDetail = cart.getProductDetail();
            int quantity = cart.getQuantity();
            BigDecimal discountPrice = new BigDecimal(productDetail.getDiscountPrice());
            BigDecimal totalPriceWithQuantity = discountPrice.multiply(BigDecimal.valueOf(quantity));
            totalAmountBeforeUsePromotion = totalAmountBeforeUsePromotion.add(totalPriceWithQuantity);

            purchaseOrder.getPurchaseOrderDetails().add(orderDetail);
        }


        BigDecimal discountValue = BigDecimal.ZERO;
        if (request.getUserPromotionId() != null) {
            UserPromotion userPromotion = userPromotionRepository.findById(request.getUserPromotionId())
                    .orElseThrow(() -> new RuntimeException("User promotion not found"));
            if (userPromotion != null && userPromotion.getPromotion() != null
                    && totalAmountBeforeUsePromotion.compareTo(userPromotion.getPromotion().getMinOrderValue()) >= 0) {
                discountValue = calculateDiscount(totalAmountBeforeUsePromotion, userPromotion.getPromotion());
            }

        }
        System.out.println("totalAmountBeforeUsePromotion: "+totalAmountBeforeUsePromotion);
        System.out.println("discountValue: "+discountValue);
        BigDecimal finalAmount = totalAmountBeforeUsePromotion.subtract(discountValue);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }

        purchaseOrder.setAmount(finalAmount);
        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        cartRepository.deleteByUser_UserId(userId);

        return new PurchaseOrderResponse(
                purchaseOrder.getPurchaseOrderId(),
                purchaseOrder.getCreatedAt(),
                purchaseOrder.getPdfUrl(),
                purchaseOrder.getProductPromotionId(),
                purchaseOrder.getUserPromotionId(),
                purchaseOrder.getStatus(),
                purchaseOrder.getAmount(),
                purchaseOrder.getPurchaseOrderDetails().stream().map(orderDetail ->
                        new PurchaseOrderDetailResponse(orderDetail.getProductDetail().getProductDetailId(), orderDetail.getQuantity())
                ).collect(Collectors.toList())
        );

    }

    @Override
    public MomoResponse createOrder(PurchaseOrderRequest request) {
        long total = 10000;
        String orderBy = SecurityContextHolder.getContext().getAuthentication().getName();
        String orderId = UUID.randomUUID().toString();
        String orderInfo = "Order information " + orderId;
        String requestId = UUID.randomUUID().toString();
        String extraData = "hello ae";
        String rawSignature = "accessKey=" + accessKey + "&amount=" + total + "&extraData=" + extraData + "&ipnUrl=" + ipnUrl + "&orderId=" + orderId + "&orderInfo=" + orderInfo + "&partnerCode=" + partnerCode + "&redirectUrl=" + redirectUrl + "&requestId=" + requestId + "&requestType=" + requestType;
        String prettySignature = "";

        try {
            prettySignature = generateHmacSHA256(rawSignature, secretKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (prettySignature.isBlank()) {
            throw new RuntimeException("Signature generation failed");
        }
        MomoRequest requestMomo = MomoRequest.builder()
                .partnerCode(partnerCode)
                .requestType(requestType)
                .redirectUrl(redirectUrl)
                .orderId(orderId)
                .orderInfo(orderInfo)
                .requestId(requestId)
                .amount(total)
                .extraData(extraData)
                .ipnUrl(ipnUrl) // callback khi thanh toan thanh cong
                .signature(prettySignature)
                .lang("vi")
                .build();
        System.out.println("Request Momo: " + requestMomo);
        return webClient
                .post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestMomo)
                .retrieve()
                .bodyToMono(MomoResponse.class)
                .block();
    }

    private BigDecimal calculateDiscount(BigDecimal totalAmount, Promotion promotion) {
        if (promotion.getDiscountType() == Promotion.DiscountType.PERCENTAGE) {
            BigDecimal discount = totalAmount.multiply(promotion.getDiscountValue().divide(BigDecimal.valueOf(100)));
            return promotion.getMaxValue() != null ? discount.min(promotion.getMaxValue()) : discount;
        } else {
            return promotion.getDiscountValue();
        }
    }
    public String generateHmacSHA256(String data, String key) throws Exception {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }

}