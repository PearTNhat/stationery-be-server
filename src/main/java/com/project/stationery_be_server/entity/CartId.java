package com.project.stationery_be_server.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class CartId {

    private String user_id;

    private String product_detail_id;
}