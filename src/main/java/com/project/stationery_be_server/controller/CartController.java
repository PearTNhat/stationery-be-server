package com.project.stationery_be_server.controller;

import com.project.stationery_be_server.dto.request.AddCartItemRequest;
import com.project.stationery_be_server.dto.request.UpdateCartItemRequest;
import com.project.stationery_be_server.dto.response.CartResponse;
import com.project.stationery_be_server.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 1. Thêm hoặc tạo giỏ hàng
    @PostMapping
    public ResponseEntity<CartResponse> addItemToCart(@RequestBody AddCartItemRequest request) {
        CartResponse cartResponse = cartService.addItemToCart(request);
        return ResponseEntity.ok(cartResponse);
    }

    // 2. Cập nhật item trong giỏ hàng
    @PutMapping("/{productDetailId}")
    public ResponseEntity<CartResponse> updateItemInCart(
            @PathVariable String productDetailId,
            @RequestBody UpdateCartItemRequest request) {
        CartResponse cartResponse = cartService.updateItemInCart(productDetailId, request);
        return ResponseEntity.ok(cartResponse);
    }

    // 3. Xóa item khỏi giỏ hàng
    @DeleteMapping("/{productDetailId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable String productDetailId) {
        cartService.removeItemFromCart(productDetailId);
        return ResponseEntity.noContent().build();
    }
    // 4. Xem giỏ hàng của user
    @GetMapping
    public ResponseEntity<List<CartResponse>> viewCart() {
        List<CartResponse> cartResponses = cartService.viewCart();
        return ResponseEntity.ok(cartResponses);
    }

    // 5. Xem tất cả giỏ hàng (admin)
    @GetMapping("/all")
    public ResponseEntity<List<CartResponse>> viewAllCarts() {
        List<CartResponse> cartResponses = cartService.viewAllCarts();
        return ResponseEntity.ok(cartResponses);
    }
}