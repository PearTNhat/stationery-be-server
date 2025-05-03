package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.Error.NotExistedErrorCode;
import com.project.stationery_be_server.dto.request.MomoRequest;
import com.project.stationery_be_server.dto.request.order.PurchaseOrderProductRequest;
import com.project.stationery_be_server.dto.request.order.PurchaseOrderRequest;
import com.project.stationery_be_server.dto.response.momo.MomoResponse;
import com.project.stationery_be_server.dto.response.PurchaseOrderDetailResponse;
import com.project.stationery_be_server.dto.response.PurchaseOrderResponse;
import com.project.stationery_be_server.entity.*;
import com.project.stationery_be_server.exception.AppException;
import com.project.stationery_be_server.repository.*;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.project.stationery_be_server.entity.PurchaseOrder.Status.PENDING;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    WebClient webClient;
    ProductDetailRepository productDetailRepository;
    CartRepository cartRepository;
    PurchaseOrderRepository purchaseOrderRepository;
    UserRepository userRepository;
    UserPromotionRepository userPromotionRepository;
    ProductPromotionRepository productPromotionRepository;
    AddressRepository addressRepository;
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
    public void handleRequestPurchaseOrder(PurchaseOrderRequest request, String orderId) {
        List<PurchaseOrderDetail> listOderDetail = new ArrayList<>();
        List<PurchaseOrderProductRequest> pdRequest = request.getOrderDetails();
        String userPromotionId = request.getUserPromotionId();

        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setUser(user);
        purchaseOrder.setPurchaseOrderId(orderId);
        purchaseOrder.setStatus(PENDING);
        purchaseOrder.setAddress(addressRepository.findByAddressId(request.getAddressId()).orElseThrow(() -> new AppException(NotExistedErrorCode.ADDRESS_NOT_FOUND)));
        purchaseOrder.setExpiredTime(LocalDateTime.now().plusMinutes(9));
        purchaseOrder.setRecipient(request.getRecipient());

        purchaseOrderRepository.save(purchaseOrder);
        Long totalAmount = 0L;
        for (PurchaseOrderProductRequest orderDetail : pdRequest) {
            ProductDetail pd = productDetailRepository.findByProductDetailId(orderDetail.getProductDetailId());
            if (pd == null) {
                throw new RuntimeException("Product detail not found");
            }
            int disCountPrice = pd.getDiscountPrice();
            cartRepository.deleteByUser_UserIdAndProductDetail_ProductDetailId(user.getUserId(), orderDetail.getProductDetailId());
            ProductPromotion promotion = null;
            if (orderDetail.getProductPromotionId() != null) {
                promotion = productPromotionRepository.getValidPromotionForProductDetail(orderDetail.getProductPromotionId(), pd.getDiscountPrice()).orElseThrow(() -> new AppException(NotExistedErrorCode.PRODUCT_PROMOTION_NOT_EXISTED));
                if (promotion.getPromotion().getDiscountType() == Promotion.DiscountType.PERCENTAGE) {
                    // giam %
                    int valueDisCount = (pd.getDiscountPrice() * promotion.getPromotion().getDiscountValue()) / 100;
                    if (valueDisCount > promotion.getPromotion().getMaxValue()) { // neu so tien  vuot qua max value
                        disCountPrice -= promotion.getPromotion().getMaxValue();
                    } else {
                        disCountPrice -= valueDisCount;
                    }
                } else {
                    // giam theo gia tri
                    disCountPrice -= promotion.getPromotion().getDiscountValue();
                }
            }

            pd.setAvailableQuantity(pd.getAvailableQuantity() - orderDetail.getQuantity());
            if(pd.getAvailableQuantity() < 0){
                throw new AppException(NotExistedErrorCode.PRODUCT_NOT_ENOUGH);
            }
            productDetailRepository.save(pd);
            totalAmount += disCountPrice;
            PurchaseOrderDetailId id = new PurchaseOrderDetailId();
            id.setPurchaseOrderId(orderId);  // Chính là orderId được truyền vào
            id.setProductDetailId(pd.getProductDetailId());  // Lấy từ productDetail
            PurchaseOrderDetail purchaseOrderDetail = PurchaseOrderDetail.builder()
                    .purchaseOrderDetailId(id)
                    .quantity(orderDetail.getQuantity())
                    .productPromotion(promotion)
                    .productDetail(pd)
                    .originalPrice(pd.getDiscountPrice())
                    .discountPrice(disCountPrice)
                    .purchaseOrder(purchaseOrder)
                    .build();
            listOderDetail.add(purchaseOrderDetail);

        }
        UserPromotion userPromotion = null;
        if (userPromotionId != null) {
            userPromotion = userPromotionRepository.getValidPromotionForUser(userPromotionId, totalAmount).orElseThrow(() -> new AppException(NotExistedErrorCode.PRODUCT_PROMOTION_NOT_EXISTED));
            if (userPromotion.getPromotion().getDiscountType() == Promotion.DiscountType.PERCENTAGE) {
                // giam %
                Long valueDisCount = (totalAmount * userPromotion.getPromotion().getDiscountValue()) / 100;
                if (valueDisCount > userPromotion.getPromotion().getMaxValue()) { // neu so tien  vuot qua max value
                    totalAmount -= userPromotion.getPromotion().getMaxValue();
                } else {
                    totalAmount -= valueDisCount;
                }
            } else {
                // giam theo gia tri
                totalAmount -= userPromotion.getPromotion().getDiscountValue();
            }
        }
        purchaseOrder.setPurchaseOrderDetails(listOderDetail);
        purchaseOrder.setUserPromotion(userPromotion);
        purchaseOrder.setAmount(totalAmount);

        purchaseOrderRepository.save(purchaseOrder);
    }

    @Override
    @Transactional
    public MomoResponse createOrderWithMomo(PurchaseOrderRequest request) {
        long total = 10000;
        String orderId = generateOrderId();
        String orderInfo = "Order information " + orderId;
        String requestId = UUID.randomUUID().toString();
        String extraData = "hello ae";
        String rawSignature = "accessKey=" + accessKey + "&amount=" + total + "&extraData=" + extraData + "&ipnUrl=" + ipnUrl + "&orderId=" + orderId + "&orderInfo=" + orderInfo + "&partnerCode=" + partnerCode + "&redirectUrl=" + redirectUrl + "&requestId=" + requestId + "&requestType=" + requestType;
        handleRequestPurchaseOrder(request,orderId);
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


    public String generateHmacSHA256(String data, String key) throws Exception {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }

    public String generateOrderId() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

}