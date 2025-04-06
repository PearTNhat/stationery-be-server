package com.project.stationery_be_server.service;

import com.project.stationery_be_server.dto.request.ReviewRequest;
import com.project.stationery_be_server.dto.request.UpdateReviewRequest;

public interface ReviewService {
    void creatReview(ReviewRequest request);
    void updateReview(UpdateReviewRequest request);

    void deleteReview(String reviewId);
}
