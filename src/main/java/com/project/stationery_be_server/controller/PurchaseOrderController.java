//package com.project.stationery_be_server.controller;
//
//
//import com.project.stationery_be_server.dto.request.PurchaseOrderRequest;
//import com.project.stationery_be_server.dto.response.PurchaseOrderResponse;
//import com.project.stationery_be_server.service.PurchaseOrderService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/orders")
//public class PurchaseOrderController {
//    private final PurchaseOrderService purchaseOrderService;
//
//    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
//        this.purchaseOrderService = purchaseOrderService;
//    }
//
//    @PostMapping("/from-cart")
//    public ResponseEntity<PurchaseOrderResponse> createOrderFromCart(@RequestBody PurchaseOrderRequest request) {
//        return ResponseEntity.ok(purchaseOrderService.createOrderFromCart(request));
//    }
//}
