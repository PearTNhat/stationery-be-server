package com.project.stationery_be_server.controller;

import com.cloudinary.Api;
import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.entity.Product;
import com.project.stationery_be_server.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ApiResponse<Page<Product>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int limit,
                                                     @RequestParam(defaultValue = "createdAt") String sortBy,
                                                     @RequestParam(defaultValue = "true") boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        return ApiResponse.<Page<Product>>builder()
                .result(productService.getAllProducts(pageable))
                .build();
    }
}
