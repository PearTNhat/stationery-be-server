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
@Table(name = "product_type")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String product_type_id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(length = 10, nullable = false)
    private String type;

    private int original_price;

    private int discounted_price;

    private int stock_quantity;

    private int sold_quantity;

    @OneToMany(mappedBy = "product_type")
    private List<Image> images;
}
