package com.project.stationery_be_server.dto.response;

import com.project.stationery_be_server.entity.ProductColor;
import com.project.stationery_be_server.entity.Review;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String productId;
    String name;
    String description;
    CategoryProductResponse category;
    Set<ProductColor> productColors;
    String slug;
    Double totalRating;
    LocalDateTime createdAt;
    List<Review> reviews;
}
