package com.project.stationery_be_server.controller;


import com.project.stationery_be_server.dto.request.PurchaseOrderRequest;
import com.project.stationery_be_server.dto.response.PurchaseOrderResponse;
import com.project.stationery_be_server.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<PurchaseOrderResponse> createOrderFromCart(@RequestBody PurchaseOrderRequest request,@PathVariable String userId) {
        return ResponseEntity.ok(purchaseOrderService.createOrderFromCart(request,userId));
    }
}
