package com.project.stationery_be_server.dto.response;

import com.project.stationery_be_server.entity.Product;
import com.project.stationery_be_server.entity.ProductDetail;
import com.project.stationery_be_server.entity.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {
    String productDetailId;
    String slug;
    int stockQuantity;
    int soldQuantity;
    String originalPrice;
    String displayPrice;
    Size size;
    ProductDetail productDetail;
}
