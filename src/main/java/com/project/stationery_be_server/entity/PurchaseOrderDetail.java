package com.project.stationery_be_server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class PurchaseOrderDetail {
    @EmbeddedId
    PurchaseOrderDetailId purchase_order_detail_id;
    @ManyToOne
    @MapsId("purchase_order_id")  // Liên kết với `cart_id` trong `CartDetailId`
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchase_order;

    @ManyToOne
    @MapsId("product_detail_id")  // Liên kết với `product_detail_id` trong `CartDetailId`
    @JoinColumn(name = "product_detail_id", nullable = false)
    private ProductDetail product_detail;
    int quantity;
}
