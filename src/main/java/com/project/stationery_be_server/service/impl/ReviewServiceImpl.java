package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.dto.request.ReviewRequest;
import com.project.stationery_be_server.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService {

    @Override
    public void creatReview(ReviewRequest request) {
        int rating = request.getRating();
        String product = request.getProduct_id();
        String parentId = request.getParent_id();
        String content = request.getContent();
        if(rating > 0 && parentId.isEmpty()){
            throw new IllegalArgumentException("Rating and parent id cannot be present at the same time");
        }
        if(rating < 0 || rating > 5){
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
    }
}
