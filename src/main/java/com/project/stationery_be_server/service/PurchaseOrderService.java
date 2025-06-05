package com.project.stationery_be_server.service;

import com.project.stationery_be_server.dto.request.order.PurchaseOrderRequest;
import com.project.stationery_be_server.dto.response.momo.MomoResponse;
import com.project.stationery_be_server.dto.response.PurchaseOrderResponse;
import com.project.stationery_be_server.dto.response.product.ProductDetailResponse;
import com.project.stationery_be_server.entity.PurchaseOrder;

import java.util.List;
import java.util.Map;

public interface PurchaseOrderService {
    MomoResponse createOrderWithMomo(PurchaseOrderRequest request);
    MomoResponse transactionStatus(String orderId,Integer status);
    List<PurchaseOrderResponse> getAllPendingOrders();
    List<PurchaseOrderResponse> getAllNonPendingOrders();
    List<PurchaseOrderResponse> getUserOrdersByStatus(String userId, String status);
    List<ProductDetailResponse> getProductDetailsByOrderId(String purchaseOrderId);
    Map<PurchaseOrder.Status, Long> getOrderStatusStatistics(String userId);
    void cancelOrder(String userId, String purchaseOrderId, String cancelReason);
    PurchaseOrderResponse editPurchaseOrder(String userId, String purchaseOrderId, PurchaseOrderRequest request);
}