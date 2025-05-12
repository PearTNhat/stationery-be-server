package com.project.stationery_be_server.controller;


import com.project.stationery_be_server.dto.request.order.PurchaseOrderRequest;
import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.dto.response.momo.MomoResponse;
import com.project.stationery_be_server.dto.response.PurchaseOrderResponse;
import com.project.stationery_be_server.dto.response.product.ProductDetailResponse;
import com.project.stationery_be_server.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;


    @PostMapping("/payment-momo")
    public ApiResponse<MomoResponse> createOrderWithMomo(@RequestBody PurchaseOrderRequest request) {
        System.out.println("Request: " + request);
        return ApiResponse.<MomoResponse>builder()
                .message("Order created successfully")
                .result(purchaseOrderService.createOrderWithMomo(request))
                .build();

    }
    @GetMapping("/payment-momo/transaction-status/{orderId}")
    public ApiResponse<MomoResponse> transactionStatus(@PathVariable String orderId) {
        return ApiResponse.<MomoResponse>builder()
                .message("Transaction status retrieved successfully")
                .result(purchaseOrderService.transactionStatus(orderId))
                .build();
    }

    @GetMapping("/pending")
    public ApiResponse<List<PurchaseOrderResponse>> getAllPendingOrders() {
        List<PurchaseOrderResponse> orders = purchaseOrderService.getAllPendingOrders();
        return ApiResponse.<List<PurchaseOrderResponse>>builder()
                .message(orders.isEmpty() ? "No with pending orders" : "Pending orders retrieved successfully")
                .result(orders)
                .build();
    }

    @GetMapping("/completed")
    public ApiResponse<List<PurchaseOrderResponse>> getAllNonPendingOrders() {
        List<PurchaseOrderResponse> orders = purchaseOrderService.getAllNonPendingOrders();
        return ApiResponse.<List<PurchaseOrderResponse>>builder()
                .message(orders.isEmpty() ? "No orders" : "Completed orders retrieved successfully")
                .result(orders)
                .build();
    }

    @GetMapping("/user/pending")
    public ApiResponse<List<PurchaseOrderResponse>> getUserPendingOrders() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PurchaseOrderResponse> orders = purchaseOrderService.getUserOrdersByStatus(userId, "PENDING");
        return ApiResponse.<List<PurchaseOrderResponse>>builder()
                .message(orders.isEmpty() ? "No with pending orders" : "User pending orders retrieved successfully")
                .result(orders)
                .build();
    }

    @GetMapping("/user/processing")
    public ApiResponse<List<PurchaseOrderResponse>> getUserProcessingOrders() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PurchaseOrderResponse> orders = purchaseOrderService.getUserOrdersByStatus(userId, "PROCESSING");
        return ApiResponse.<List<PurchaseOrderResponse>>builder()
                .message(orders.isEmpty() ? "No with processing orders" : "User processing orders retrieved successfully")
                .result(orders)
                .build();
    }

    @GetMapping("/user/shipping")
    public ApiResponse<List<PurchaseOrderResponse>> getUserShippingOrders() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PurchaseOrderResponse> orders = purchaseOrderService.getUserOrdersByStatus(userId, "SHIPPING");
        return ApiResponse.<List<PurchaseOrderResponse>>builder()
                .message(orders.isEmpty() ? "No with shipping orders" : "User shipping orders retrieved successfully")
                .result(orders)
                .build();
    }

    @GetMapping("/user/completed")
    public ApiResponse<List<PurchaseOrderResponse>> getUserCompletedOrders() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PurchaseOrderResponse> orders = purchaseOrderService.getUserOrdersByStatus(userId, "COMPLETED");
        return ApiResponse.<List<PurchaseOrderResponse>>builder()
                .message(orders.isEmpty() ? "No with completed orders" : "User completed orders retrieved successfully")
                .result(orders)
                .build();
    }

    @GetMapping("/user/canceled")
    public ApiResponse<List<PurchaseOrderResponse>> getUserCanceledOrders() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PurchaseOrderResponse> orders = purchaseOrderService.getUserOrdersByStatus(userId, "CANCELED");
        return ApiResponse.<List<PurchaseOrderResponse>>builder()
                .message(orders.isEmpty() ? "No with canceled orders" : "User canceled orders retrieved successfully")
                .result(orders)
                .build();
    }

    @GetMapping("/{purchaseOrderId}/product-details")
    public ApiResponse<List<ProductDetailResponse>> getProductDetailsByOrderId(@PathVariable String purchaseOrderId) {
        List<ProductDetailResponse> productDetails = purchaseOrderService.getProductDetailsByOrderId(purchaseOrderId);
        return ApiResponse.<List<ProductDetailResponse>>builder()
                .code(200)
                .message("Product details for order retrieved successfully")
                .result(productDetails)
                .build();
    }
}
