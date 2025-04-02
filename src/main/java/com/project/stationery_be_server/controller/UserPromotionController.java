package com.project.stationery_be_server.controller;

import com.project.stationery_be_server.dto.response.PromotionResponse;
import com.project.stationery_be_server.service.UserPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-promotions")
@RequiredArgsConstructor
public class UserPromotionController {

    private final UserPromotionService userPromotionService;

    @GetMapping()
    public ResponseEntity<List<PromotionResponse>> getUserPromotions() {
        List<PromotionResponse> promotions = userPromotionService.getVouchersForUser();
        return ResponseEntity.ok(promotions);
    }
}
