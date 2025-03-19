package com.project.stationery_be_server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Cart {
    @Id
    @EmbeddedId
    CartId cart_id;
    @ManyToOne
    @MapsId("user_id")  // Liên kết với `cart_id` trong `CartDetailId`
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("product_detail_id")  // Liên kết với `product_detail_id` trong `CartDetailId`
    @JoinColumn(name = "product_detail_id", nullable = false)
    private ProductDetail product_detail;
    int quantity;

    private Date created_at;

}
