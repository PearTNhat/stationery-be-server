package com.project.stationery_be_server.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {
    private Integer category_id;
    private String category_name;
    private String icon;
    private String bg_color;
}
