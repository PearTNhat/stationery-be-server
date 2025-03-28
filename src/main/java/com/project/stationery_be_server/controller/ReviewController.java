package com.project.stationery_be_server.controller;


import com.project.stationery_be_server.dto.request.ReviewRequest;
import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @PostMapping
    public ApiResponse<Void> createReview(@RequestBody ReviewRequest reviewRequest) {
        reviewService.creatReview(reviewRequest);
        return ApiResponse.<Void>builder()
                .message("Review created successfully")
                .build();
    }
}
