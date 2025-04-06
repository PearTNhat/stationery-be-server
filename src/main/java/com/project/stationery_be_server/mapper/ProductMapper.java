package com.project.stationery_be_server.mapper;

import com.project.stationery_be_server.dto.response.ProductListResponse;
import com.project.stationery_be_server.dto.response.ProductResponse;
import com.project.stationery_be_server.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.categoryId", target = "category.categoryId")
    @Mapping(source = "category.categoryName", target = "category.categoryName")
    ProductListResponse toProductListResponse(Product product);

    @Mapping(source = "reviews", target = "reviews")
    ProductResponse toProductResponse(Product product);
}
