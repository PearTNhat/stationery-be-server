package com.project.stationery_be_server.service;

import com.project.stationery_be_server.dto.request.PurchaseOrderRequest;
import com.project.stationery_be_server.dto.response.MomoResponse;
import com.project.stationery_be_server.dto.response.PurchaseOrderResponse;

public interface PurchaseOrderService {
    PurchaseOrderResponse createOrderFromCart(PurchaseOrderRequest request);
    MomoResponse createOrder(PurchaseOrderRequest request);
}