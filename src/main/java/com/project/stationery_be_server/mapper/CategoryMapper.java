package com.project.stationery_be_server.mapper;

import com.project.stationery_be_server.dto.response.CategoryResponse;
import com.project.stationery_be_server.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);
}
