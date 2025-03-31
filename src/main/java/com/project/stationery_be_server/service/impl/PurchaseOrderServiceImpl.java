package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.dto.request.PurchaseOrderRequest;
import com.project.stationery_be_server.dto.response.PurchaseOrderDetailResponse;
import com.project.stationery_be_server.dto.response.PurchaseOrderResponse;
import com.project.stationery_be_server.entity.*;
import com.project.stationery_be_server.repository.CartRepository;
import com.project.stationery_be_server.repository.PurchaseOrderRepository;
import com.project.stationery_be_server.repository.UserPromotionRepository;
import com.project.stationery_be_server.repository.UserRepository;
import com.project.stationery_be_server.service.PurchaseOrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.stationery_be_server.entity.PurchaseOrder.Status.PENDING;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    PurchaseOrderRepository purchaseOrderRepository;
    CartRepository cartRepository;
    UserRepository userRepository;
    UserPromotionRepository userPromotionRepository;

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
            PurchaseOrderDetail orderDetail = new PurchaseOrderDetail(detailId, purchaseOrder, cart.getProductDetail(), cart.getQuantity());

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
    private BigDecimal calculateDiscount(BigDecimal totalAmount, Promotion promotion) {
        if (promotion.getDiscountType() == Promotion.DiscountType.PERCENTAGE) {
            BigDecimal discount = totalAmount.multiply(promotion.getDiscountValue().divide(BigDecimal.valueOf(100)));
            return promotion.getMaxValue() != null ? discount.min(promotion.getMaxValue()) : discount;
        } else {
            return promotion.getDiscountValue();
        }
    }

}