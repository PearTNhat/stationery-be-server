package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.dto.request.AddCartItemRequest;
import com.project.stationery_be_server.dto.request.UpdateCartItemRequest;
import com.project.stationery_be_server.dto.response.CartResponse;
import com.project.stationery_be_server.entity.*;
import com.project.stationery_be_server.repository.*;
import com.project.stationery_be_server.service.CartService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {

    CartRepository cartRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    ProductDetailRepository productDetailRepository;
    ColorRepository colorRepository;
    SizeRepository sizeRepository;

    // Lấy userId từ SecurityContext
    private String getCurrentUserId() {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User not authenticated");
        }
        String userId = authentication.getName();
        System.out.println("Authentication.getName() returns: " + userId); // Log giá trị
        return userId;
    }

    // 1. Thêm hoặc tạo giỏ hàng (gộp createCart và addItemToCart)
    @Override
    @Transactional
    public CartResponse addItemToCart(AddCartItemRequest request) {
        String userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Color color = colorRepository.findById(request.getColorId())
                .orElseThrow(() -> new IllegalArgumentException("Color not found"));

        Size size = sizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> new IllegalArgumentException("Size not found"));

        ProductDetail productDetail = productDetailRepository
                .findByProductIdAndColorIdAndSizeId(request.getProductId(), request.getColorId(), request.getSizeId())
                .orElseThrow(() -> new IllegalArgumentException("Product variant not found"));

        if (productDetail.getStockQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for this variant");
        }

        CartId cartId = new CartId(userId, productDetail.getProductDetailId());
        Optional<Cart> existingCart = cartRepository.findByCartId(cartId);

        Cart cart;
        if (existingCart.isPresent()) {
            cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
        } else {
            cart = Cart.builder()
                    .cartId(cartId)
                    .user(user)
                    .productDetail(productDetail)
                    .quantity(request.getQuantity())
                    .createdAt(new Date())
                    .build();
        }

        cart = cartRepository.save(cart);
        return mapToCartResponse(cart);
    }

    // 2. Cập nhật item trong giỏ hàng (sử dụng AddCartItemRequest)
    @Override
    @Transactional
    public CartResponse updateItemInCart(String productDetailId, UpdateCartItemRequest request) {
        String userId = getCurrentUserId();

        // Tìm cart hiện có theo userId + productDetailId
        CartId cartId = new CartId(userId, productDetailId);
        Cart cart = cartRepository.findByCartId(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));

        // Tìm ProductDetail mới nếu có thay đổi
        ProductDetail newProductDetail = productDetailRepository
                .findByProductIdAndColorIdAndSizeId(cart.getProductDetail().getProductColor().getProduct().getProductId(), request.getColorId(), request.getSizeId())
                .orElseThrow(() -> new IllegalArgumentException("Product variant not found"));

        // Kiểm tra xem có đổi variant không
        if (!cart.getProductDetail().getProductDetailId().equals(newProductDetail.getProductDetailId())) {
            // Xóa cart cũ
            cartRepository.delete(cart);

            // Kiểm tra tồn kho mới
            if (newProductDetail.getStockQuantity() < request.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for this variant");
            }

            CartId newCartId = new CartId(userId, newProductDetail.getProductDetailId());
            cart = Cart.builder()
                    .cartId(newCartId)
                    .user(cart.getUser())
                    .productDetail(newProductDetail)
                    .quantity(request.getQuantity())
                    .createdAt(new Date())
                    .build();
        } else {
            // Không đổi variant, chỉ cập nhật số lượng
            if (newProductDetail.getStockQuantity() < request.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for this variant");
            }
            cart.setQuantity(request.getQuantity());
        }

        cart = cartRepository.save(cart);
        return mapToCartResponse(cart);
    }


    // 3. Xóa item khỏi giỏ hàng
    @Override
    @Transactional
    public void removeItemFromCart(String productDetailId) {
        String userId = getCurrentUserId();

        CartId cartId = new CartId(userId, productDetailId);
        Cart cart = cartRepository.findByCartId(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));

        cartRepository.delete(cart);
    }


    // 4. Xem giỏ hàng của user
    @Override
    public List<CartResponse> viewCart() {
        String userId = getCurrentUserId();
        List<Cart> carts = cartRepository.findByUserId(userId);
        return carts.stream()
                .map(this::mapToCartResponse)
                .collect(Collectors.toList());
    }

    // 5. Xem tất cả giỏ hàng (admin)
    @Override
    public List<CartResponse> viewAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream()
                .map(this::mapToCartResponse)
                .collect(Collectors.toList());
    }

    // Hàm ánh xạ Cart sang CartResponse
    private CartResponse mapToCartResponse(Cart cart) {
        ProductDetail productDetail = cart.getProductDetail();
        Product product = productDetail.getProductColor().getProduct(); // fix chỗ này
        Color color = productDetail.getProductColor().getColor();       // fix chỗ này
        Size size = productDetail.getSize();

        return CartResponse.builder()
                .userId(cart.getCartId().getUserId())
                .productId(product.getProductId())
                .productDetailId(productDetail.getProductDetailId())
                .productName(product.getName())
                .colorName(color.getName())
                .sizeName(size.getName())
                .quantity(cart.getQuantity())
                .originalPrice(productDetail.getOriginalPrice())
                .discountPrice(productDetail.getDiscountPrice())
                .createdAt(cart.getCreatedAt())
                .build();
    }

    @Override
    public int calculateCartTotal() {
        String userId = getCurrentUserId();
        List<Cart> carts = cartRepository.findByUserId(userId);

        return carts.stream()
                .mapToInt(cart -> {
                    ProductDetail productDetail = cart.getProductDetail();
                    return productDetail.getDiscountPrice() * cart.getQuantity();
                })
                .sum();
    }
}