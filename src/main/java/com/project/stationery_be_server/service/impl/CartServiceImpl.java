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
        System.out.println("request.getProductId() = " + request.getProductId());
        ProductDetail productDetail =  productDetailRepository.findByProductDetailId(request.getProductId());
        if(productDetail == null) {
            throw new IllegalArgumentException("Product variant not found");
        }
        if (productDetail.getStockQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for this variant");
        }

        CartId cartId = new CartId(userId, productDetail.getProductDetailId());
        Optional<Cart> existingCart = cartRepository.findByCartId(cartId);

        Cart cart;
        System.out.println(" request.getQuantity() "+ request.getQuantity());
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

        CartId cartId = new CartId(userId, productDetailId);
        Cart cart = cartRepository.findByCartId(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));

        Product product = cart.getProductDetail().getProduct();

        // Lấy color và size mới (có thể null)
        Color color = null;
        if (request.getColorId() != null) {
            color = colorRepository.findById(request.getColorId())
                    .orElseThrow(() -> new IllegalArgumentException("Color not found"));
        }

        Size size = null;
        if (request.getSizeId() != null) {
            size = sizeRepository.findById(request.getSizeId())
                    .orElseThrow(() -> new IllegalArgumentException("Size not found"));
        }

        // Tìm ProductDetail mới theo variant có thể có hoặc không có color/size
        ProductDetail newProductDetail = productDetailRepository
                .findByProductIdAndOptionalColorIdAndOptionalSizeId(
                        product.getProductId(),
                        request.getColorId(),
                        request.getSizeId())
                .orElseThrow(() -> new IllegalArgumentException("Product variant not found"));

        if (!cart.getProductDetail().getProductDetailId().equals(newProductDetail.getProductDetailId())) {
            cartRepository.delete(cart);

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
//    private CartResponse mapToCartResponse(Cart cart) {
//        ProductDetail productDetail = cart.getProductDetail();
//        Product product = productDetail.getProductColor().getProduct(); // fix chỗ này
//        Color color = productDetail.getProductColor().getColor();       // fix chỗ này
//        Size size = productDetail.getSize();
//
//        return CartResponse.builder()
//                .userId(cart.getCartId().getUserId())
//                .productId(product.getProductId())
//                .productDetailId(productDetail.getProductDetailId())
//                .productName(product.getName())
//                .colorName(color.getName())
//                .sizeName(size != null ? size.getName() : null)
//                .quantity(cart.getQuantity())
//                .originalPrice(productDetail.getOriginalPrice())
//                .discountPrice(productDetail.getDiscountPrice())
//                .createdAt(cart.getCreatedAt())
//                .build();
//    }
    private CartResponse mapToCartResponse(Cart cart) {
//        ProductDetail productDetail = cart.getProductDetail();
//        ProductColor productColor = productDetail.getProductColor();
//
//        // Lấy ảnh đầu tiên (ưu tiên ảnh có priority thấp nhất nếu đã @OrderBy)
//        String imageUrl = productColor.getImages().stream()
//                .findFirst()
//                .map(image -> image.getUrl()) // hoặc image.getImageUrl() tùy theo tên trường
//                .orElse(null);

//        return CartResponse.builder()
//                .userId(cart.getUser().getUserId())
//                .productId(productColor.getProduct().getProductId())
//                .productDetailId(productDetail.getProductDetailId())
//                .productName(productColor.getProduct().getName())
//                .colorName(productColor.getColor().getName())
//                .sizeName(productDetail.getSize() != null ? productDetail.getSize().getName() : null)
//                .quantity(cart.getQuantity())
//                .originalPrice(productDetail.getOriginalPrice())
//                .discountPrice(productDetail.getDiscountPrice())
//                .createdAt(cart.getCreatedAt())
//                .imageUrl(imageUrl)
//                .build();
        return  CartResponse.builder()
                .sizeName("123")
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