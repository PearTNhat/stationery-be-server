package com.project.stationery_be_server.dto.request;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ColorRequest {
    String color_id;
    String name;
    String hex;
}
