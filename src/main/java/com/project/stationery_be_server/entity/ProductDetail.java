package com.project.stationery_be_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String product_detail_id;

    @Column(length = 100)
    private String slug;

    private int stock_quantity;
    private int original_price;
    private int discount_price;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

}
