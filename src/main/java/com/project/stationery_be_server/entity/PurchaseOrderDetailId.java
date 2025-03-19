package com.project.stationery_be_server.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class PurchaseOrderDetailId {

    private String purchase_order_id;

    private String product_detail_id;
}