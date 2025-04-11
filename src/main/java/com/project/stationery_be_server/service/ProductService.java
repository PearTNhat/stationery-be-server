package com.project.stationery_be_server.service;

import com.project.stationery_be_server.Error.NotExistedErrorCode;
import com.project.stationery_be_server.dto.request.ProductFilterRequest;
import com.project.stationery_be_server.dto.response.ProductListResponse;
import com.project.stationery_be_server.dto.response.ProductResponse;
import com.project.stationery_be_server.entity.Product;
import com.project.stationery_be_server.entity.ProductDetail;
import com.project.stationery_be_server.exception.AppException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;
import java.util.Set;

public interface ProductService {
    Page<ProductListResponse> getAllProducts(Pageable pageable , ProductFilterRequest filter);
    ProductResponse getProductDetail(String slug);
    void updateMinPrice(Product product);
    @Transactional
    void handleUpdateTotalProductRating(String productId, String type, Integer rating);
}
