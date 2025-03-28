package com.project.stationery_be_server.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.Data;

@Embeddable
@Data
public class PurchaseOrderDetailId {
    @Column(name = "purchase_order_id")
    private String purchaseOrderId;

    @Column(name = "product_detail_id")
    private String productDetailId;
}
