package com.project.stationery_be_server.controller;


import com.project.stationery_be_server.dto.request.PurchaseOrderRequest;
import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.dto.response.MomoResponse;
import com.project.stationery_be_server.dto.response.PurchaseOrderResponse;
import com.project.stationery_be_server.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @PostMapping()
    public ApiResponse<PurchaseOrderResponse> createOrderFromCart(@RequestBody PurchaseOrderRequest request) {
        return ApiResponse.<PurchaseOrderResponse>builder()
                .message("Order created successfully")
                .result(purchaseOrderService.createOrderFromCart(request))
                .build();
    }

    @PostMapping("/payment")
    public ApiResponse<MomoResponse> createOrderFromPayment(@RequestBody PurchaseOrderRequest request) {
        System.out.println("Request: " + request);
        return ApiResponse.<MomoResponse>builder()
                .message("Order created successfully")
                .result(purchaseOrderService.createOrder(request))
                .build();

    }

}
