package com.project.stationery_be_server.controller;

import com.project.stationery_be_server.dto.request.ProductFilterRequest;
import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.dto.response.ProductListResponse;
import com.project.stationery_be_server.dto.response.ProductResponse;
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
                                                                                    @RequestParam(required = false) String minPrice,
                                                                                    @RequestParam(required = false) String maxPrice,
                                                                                    @RequestParam(required = false) String search,
                                                                                    @RequestParam(required = false) String categoryId,
                                                                                    @RequestParam(required = false) String totalRating
    ) {
        Sort sort = null;
        if (!sortBy.isBlank() && !sortBy.isEmpty()) {
            String[] parts = sortBy.split("(?<=-)|(?=-)"); // tách dấu tru trong chuoi
            //-abc
            System.out.println(parts.length);
            sort = parts.length == 1 ? Sort.by(parts[0]).ascending() : Sort.by(parts[1]).descending();

        }
        Pageable pageable;
        if (sort != null) {
            pageable = PageRequest.of(page, limit, sort);

        } else {
            pageable = PageRequest.of(page, limit);
        }
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
    public ApiResponse<ProductDetail> getProductDetailProduct(@PathVariable String slug) {
        return ApiResponse.<ProductDetail>builder()
                .result(productService.getProductDetail(slug))
                .build();
    }
}
