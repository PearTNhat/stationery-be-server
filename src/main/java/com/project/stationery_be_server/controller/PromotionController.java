package com.project.stationery_be_server.controller;

import com.project.stationery_be_server.dto.request.DeletePromotionRequest;
import com.project.stationery_be_server.dto.request.PromotionRequest;
import com.project.stationery_be_server.dto.request.UpdatePromotionRequest;
import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.entity.ProductPromotion;
import com.project.stationery_be_server.entity.Promotion;
import com.project.stationery_be_server.entity.UserPromotion;
import com.project.stationery_be_server.service.PromotionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @PostMapping()
    public ApiResponse<String> createPromotion(@RequestBody PromotionRequest request) {
        promotionService.createPromotion(request);
        return ApiResponse.<String>builder()
                .result("Promotion created successfully" )
                .build();
    }
    @PutMapping("/update")
    public ApiResponse<String> updatePromotion(@RequestBody UpdatePromotionRequest request) {
        promotionService.updatePromotion(request);
        return ApiResponse.<String>builder()
                .result("Promotion updated successfully")
                .build();
    }
    @GetMapping("/my-promotions")
    public ApiResponse<Page<Promotion>> getMyPromotions(Pageable pageable) {
        Page<Promotion> pageResult = promotionService.getMyVouchers(pageable);
        return ApiResponse.<Page<Promotion>>builder()
                .result(pageResult)
                .build();
    }
    @GetMapping("/all-user-promotions")
    public ApiResponse<Page<UserPromotion>> getAllUserPromotions(Pageable pageable) {
        Page<UserPromotion> pageUP = promotionService.getAllUserVouchers(pageable);
        return ApiResponse.<Page<UserPromotion>>builder()
                .result(pageUP)
                .build();
    }
    @GetMapping("/all-product-promotions")
    public ApiResponse<Page<ProductPromotion>> getAllProductPromotions(Pageable pageable) {
        Page<ProductPromotion> pagePP = promotionService.getAllProductPromotions(pageable);
        return ApiResponse.<Page<ProductPromotion>>builder()
                .result(pagePP)
                .build();
    }
    @GetMapping("/{promotionId}")
    public ApiResponse<Promotion> getPromotion(@PathVariable String promotionId) {
        Promotion promo = promotionService.getPromotionById(promotionId);
        return ApiResponse.<Promotion>builder()
                .result(promo)
                .build();
    }
}