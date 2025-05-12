package com.project.stationery_be_server.service;

import com.project.stationery_be_server.dto.request.order.PurchaseOrderRequest;
import com.project.stationery_be_server.dto.response.momo.MomoResponse;
import com.project.stationery_be_server.dto.response.PurchaseOrderResponse;
import com.project.stationery_be_server.dto.response.product.ProductDetailResponse;
import com.project.stationery_be_server.entity.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderService {
    MomoResponse createOrderWithMomo(PurchaseOrderRequest request);
    MomoResponse transactionStatus(String orderId);
    List<PurchaseOrderResponse> getAllPendingOrders();
    List<PurchaseOrderResponse> getAllNonPendingOrders();
    List<PurchaseOrderResponse> getUserOrdersByStatus(String userId, String status);
    List<ProductDetailResponse> getProductDetailsByOrderId(String purchaseOrderId);
}