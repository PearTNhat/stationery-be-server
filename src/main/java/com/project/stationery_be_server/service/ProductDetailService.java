package com.project.stationery_be_server.service;

import com.project.stationery_be_server.dto.request.DeleteProductDetailRequest;
import com.project.stationery_be_server.dto.response.product.ProductDetailResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductDetailService {
    void deleteProductDetail(DeleteProductDetailRequest request);
}
