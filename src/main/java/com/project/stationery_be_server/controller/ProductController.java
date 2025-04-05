package com.project.stationery_be_server.controller;

import com.project.stationery_be_server.dto.request.ProductFilterRequest;
import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.dto.response.ProductListResponse;
import com.project.stationery_be_server.entity.Product;
import com.project.stationery_be_server.entity.ProductDetail;
import com.project.stationery_be_server.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;
    PagedResourcesAssembler<ProductListResponse> pagedResourcesAssembler; // Inject

    @GetMapping
    public ApiResponse<PagedModel<EntityModel<ProductListResponse>>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int limit,
                                                                        @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                        @RequestParam(defaultValue = "true") boolean ascending,
                                                                        @RequestParam(required = false) String minPrice,
                                                                        @RequestParam(required = false) String maxPrice,
                                                                        @RequestParam(required = false) String search,
                                                                        @RequestParam(required = false) String categoryId,
                                                                        @RequestParam(required = false) String totalRating
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        ProductFilterRequest filterRequest = ProductFilterRequest.builder()
                .categoryId(categoryId)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .search(search)
                .totalRating(totalRating)
                .build();
        Page<ProductListResponse> pageResult = productService.getAllProducts(pageable, filterRequest);
        PagedModel<EntityModel<ProductListResponse>> result = pagedResourcesAssembler.toModel(pageResult);
        return ApiResponse.<PagedModel<EntityModel<ProductListResponse>>>builder()
                .result(result)
                .build();
    }
    @GetMapping("/{slug}")
    public ApiResponse<Product> getProductDetailProduct(@PathVariable String slug) {
        return ApiResponse.<Product>builder()
                .result(productService.getProductDetail(slug))
                .build();
    }
}
