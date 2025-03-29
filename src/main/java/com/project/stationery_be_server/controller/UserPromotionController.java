package com.project.stationery_be_server.controller;
import com.project.stationery_be_server.dto.response.PromotionResponse;
import com.project.stationery_be_server.service.UserPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user-promotions")
@RequiredArgsConstructor
public class UserPromotionController {

    private final UserPromotionService userPromotionService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<PromotionResponse>> getUserPromotions(@PathVariable String userId) {
        List<PromotionResponse> promotions = userPromotionService.getVouchersForUser(userId);
        return ResponseEntity.ok(promotions);
    }
}
