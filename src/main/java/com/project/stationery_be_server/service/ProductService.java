package com.project.stationery_be_server.service;

import com.project.stationery_be_server.Error.NotExistedErrorCode;
import com.project.stationery_be_server.entity.Product;
import com.project.stationery_be_server.exception.AppException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<Product> getAllProducts(Pageable pageable ,String categoryId);

    @Transactional
    void handleUpdateTotalProductRating(String productId, String type, Integer rating);
}
