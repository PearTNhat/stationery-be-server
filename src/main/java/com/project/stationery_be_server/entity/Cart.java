package com.project.stationery_be_server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JsonIgnore
    @MapsId("product_detail_id")  // Liên kết với `product_detail_id` trong `CartDetailId`
    @JoinColumn(name = "product_detail_id", nullable = false)
    private ProductDetail product_detail;
    int quantity;
    private Date created_at;

}
