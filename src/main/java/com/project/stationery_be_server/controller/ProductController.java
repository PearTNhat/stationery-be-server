package com.project.stationery_be_server.controller;

import com.cloudinary.Api;
import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.entity.Product;
import com.project.stationery_be_server.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<Product>> getAllProducts() {
        return ApiResponse.<List<Product>>builder()
                .result(productService.getAllProducts())
                .build();
    }
}
