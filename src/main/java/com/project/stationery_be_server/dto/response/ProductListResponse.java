package com.project.stationery_be_server.dto.response;

import com.project.stationery_be_server.entity.Category;
import com.project.stationery_be_server.entity.Product;
import com.project.stationery_be_server.entity.ProductColor;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductListResponse {
    String productId;
    String name;
    String description;
    CategoryProductResponse category;
    Set<ProductColor> productColors;
    String slug;
    Double totalRating;
    LocalDateTime createdAt;
}
