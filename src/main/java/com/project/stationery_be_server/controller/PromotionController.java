package com.project.stationery_be_server.controller;

import com.project.stationery_be_server.dto.request.DeletePromotionRequest;
import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.dto.response.ColorResponse;
import com.project.stationery_be_server.entity.ProductPromotion;
import com.project.stationery_be_server.service.PromotionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromotionController {
    final PromotionService promotionService;

    @DeleteMapping
    public ApiResponse<String> deletePromotion(@RequestBody DeletePromotionRequest request){
        promotionService.deletePromotion(request);
        return ApiResponse.<String>builder()
                .result("Promotion deleted successfully")
                .build();


    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/admin/get-product-promotions")
    public ApiResponse<Page<ProductPromotion>> getAllProductsForAdmin(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int limit,
                                                                   @RequestParam(required = false) String search

    ) {
        // sử lý ở FE page 1 là BE page 0, page 2 là page 1, ..
        page = page <= 1 ? 0 : page - 1;
        Pageable pageable;

        pageable = PageRequest.of(page, limit);
        Page<ProductPromotion> pageResult = promotionService.getAllProductPromotionPagination(pageable, search);
        return ApiResponse.<Page<ProductPromotion>>builder()
                .result(pageResult)
                .build();
    }
//    @DeleteMapping("/{id}")
//    public ApiResponse<Void> deleteColor(@PathVariable String id) {
//        colorService.deleteColor(id);
//        return ApiResponse.<Void>builder()
//                .message("Color deleted successfully")
//                .build();
//    }
}