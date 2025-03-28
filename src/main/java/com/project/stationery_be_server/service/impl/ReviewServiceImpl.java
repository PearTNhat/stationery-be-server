package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.Error.NotExistedErrorCode;
import com.project.stationery_be_server.dto.request.ReviewRequest;
import com.project.stationery_be_server.entity.Product;
import com.project.stationery_be_server.entity.Review;
import com.project.stationery_be_server.exception.AppException;
import com.project.stationery_be_server.repository.ProductRepository;
import com.project.stationery_be_server.repository.ReviewRepository;
import com.project.stationery_be_server.service.ProductService;
import com.project.stationery_be_server.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.PackagePrivate;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepository;
    ProductService productService;


    @Override
    @Transactional
    public void creatReview(ReviewRequest request) {
        int rating = request.getRating();
        String product_id = request.getProductId();
        String parentId = request.getParentId();
        String content = request.getContent();
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        if (rating > 0 && parentId != null) {
            throw new IllegalArgumentException("Rating and parent id cannot be present at the same time");
        }
        if (rating > 0) {
            if (rating > 5) {
                throw new IllegalArgumentException("Rating must be between 0 and 5");
            }
            var isRated = reviewRepository.findByProductProductIdAndUserUserIdAndRatingIsNotNull(product_id, userId);
            if (isRated.isPresent()) {
                throw new IllegalArgumentException("You have already rated this product");
            }
            // khắc phục lỗi k kích hoạt đc transaction
            productService.handleUpdateTotalProductRating(product_id, "create", rating);
        }
        if (rating > 0 || parentId != null) {
            reviewRepository.createReview(product_id, userId, content, rating, parentId);
        } else {
            throw new IllegalArgumentException("This review is not include rating");
        }

    }
}
