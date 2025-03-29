//package com.project.stationery_be_server.service.impl;
//
//import com.project.stationery_be_server.dto.request.PurchaseOrderRequest;
//import com.project.stationery_be_server.dto.response.PurchaseOrderDetailResponse;
//import com.project.stationery_be_server.dto.response.PurchaseOrderResponse;
//import com.project.stationery_be_server.entity.*;
//import com.project.stationery_be_server.repository.CartRepository;
//import com.project.stationery_be_server.repository.PurchaseOrderRepository;
//import com.project.stationery_be_server.repository.UserRepository;
//import com.project.stationery_be_server.service.PurchaseOrderService;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class PurchaseOrderServiceImpl implements PurchaseOrderService {
//    private final PurchaseOrderRepository purchaseOrderRepository;
//    private final CartRepository cartRepository;
//    private final UserRepository userRepository;
//
//    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository,
//                                    CartRepository cartRepository,
//                                    UserRepository userRepository) {
//        this.purchaseOrderRepository = purchaseOrderRepository;
//        this.cartRepository = cartRepository;
//        this.userRepository = userRepository;
//    }
//
//    @Transactional
//    @Override
//    public PurchaseOrderResponse createOrderFromCart(PurchaseOrderRequest request) {
//        User user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        List<Cart> cartItems = cartRepository.findByUser_UserId(request.getUserId());
//        if (cartItems.isEmpty()) {
//            throw new RuntimeException("Cart is empty");
//        }
//
//        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
//                .user(user)
//                .createdAt(new Date())
//                .purchaseOrderDetails(new HashSet<>())
//                .build();
//
//        for (Cart cart : cartItems) {
//            PurchaseOrderDetailId detailId = new PurchaseOrderDetailId(purchaseOrder.getPurchaseOrderId(), cart.getProductDetail().getProductDetailId());
//            PurchaseOrderDetail orderDetail = new PurchaseOrderDetail(detailId, purchaseOrder, cart.getProductDetail(), cart.getQuantity());
//
//            purchaseOrder.getPurchaseOrderDetails().add(orderDetail);
//        }
//
//        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);
//        cartRepository.deleteByUser_UserId(request.getUserId());
//
//        return new PurchaseOrderResponse(
//                purchaseOrder.getPurchaseOrderId(),
//                purchaseOrder.getCreatedAt(),
//                purchaseOrder.getPurchaseOrderDetails().stream().map(orderDetail ->
//                        new PurchaseOrderDetailResponse(orderDetail.getProductDetail().getProductDetailId(), orderDetail.getQuantity())
//                ).collect(Collectors.toList())
//        );
//    }
//}