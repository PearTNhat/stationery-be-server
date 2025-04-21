package com.project.stationery_be_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    String userId;
    String productId;
    String productDetailId;
    String productName;
    String colorName;
    String sizeName;
    int quantity;
    int originalPrice;
    int discountPrice;
    Date createdAt;

    String imageUrl;
}
