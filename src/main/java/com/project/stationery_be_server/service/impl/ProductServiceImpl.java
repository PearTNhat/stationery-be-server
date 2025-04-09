package com.project.stationery_be_server.service.impl;


import com.project.stationery_be_server.Error.NotExistedErrorCode;
import com.project.stationery_be_server.dto.request.ProductFilterRequest;
import com.project.stationery_be_server.dto.response.ProductListResponse;
import com.project.stationery_be_server.dto.response.ProductResponse;
import com.project.stationery_be_server.entity.Product;
import com.project.stationery_be_server.entity.ProductColor;
import com.project.stationery_be_server.entity.ProductDetail;
import com.project.stationery_be_server.entity.Review;
import com.project.stationery_be_server.exception.AppException;
import com.project.stationery_be_server.mapper.ProductMapper;
import com.project.stationery_be_server.repository.ProductColorRepository;
import com.project.stationery_be_server.repository.ProductDetailRepository;
import com.project.stationery_be_server.repository.ProductRepository;
import com.project.stationery_be_server.repository.ReviewRepository;
import com.project.stationery_be_server.service.ProductService;
import com.project.stationery_be_server.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ReviewRepository reviewRepository;
    ProductDetailRepository productDetailRepository;
    ProductMapper productMapper;
    ProductColorRepository productColorRepository;

    @Override
        public Page<ProductListResponse> getAllProducts(Pageable pageable , ProductFilterRequest filter) {
        Specification<Product> spec = ProductSpecification.filterProducts(filter);
        Page<Product> products = productRepository.findAll(spec,pageable);
        List<ProductListResponse> productListResponses = products.getContent().stream()
                .map(productMapper::toProductListResponse)
                .toList();
        return new PageImpl<>(productListResponses, pageable, products.getTotalElements());
    }

    @Override
    public ProductResponse getProductDetail(String slug) {  
        Product product =  productRepository.findBySlug(slug);

        List<Review> reviews = reviewRepository.findByProduct_ProductIdAndParentReviewIsNull(product.getProductId());
        Set<ProductColor> pcs = product.getProductColors().stream()
                .filter(pc -> !pc.getProductDetails().isEmpty())
                .collect(Collectors.toSet());
        product.setProductColors(pcs);
        product.setReviews(reviews);
        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public void handleUpdateTotalProductRating(String productId, String type, Integer rating) {
        int countRating = reviewRepository.countByProductId(productId);
        int sumRating = reviewRepository.sumRatingByProductId(productId);

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(NotExistedErrorCode.PRODUCT_NOT_EXISTED));
        int length = countRating;
        if (type.equalsIgnoreCase("create")) {
            length += 1;
        } else if (type.equalsIgnoreCase("update")) {

        } else if (type.equalsIgnoreCase("delete")) {
            length -= 1;
        } else {
            throw new IllegalArgumentException("Type must be create, update or delete");
        }
        if (length == 0) { //  k có rating
            product.setTotalRating(0.0);

        } else {
            double totalRating = (double) (sumRating + rating) / length;
            product.setTotalRating(totalRating);
        }
        productRepository.save(product);
    }
}
