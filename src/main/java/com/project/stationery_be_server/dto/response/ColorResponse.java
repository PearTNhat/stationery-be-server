package com.project.stationery_be_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColorResponse {
    String color_id;
    String name;
    String hex;
}
