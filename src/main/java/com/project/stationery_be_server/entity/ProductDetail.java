package com.project.stationery_be_server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class    ProductDetail {
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
    @JoinColumn(name = "product_color_id", nullable = false)
    private ProductColor product_color;

    @OneToMany(mappedBy = "product_detail", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PurchaseOrderDetail> purchase_purchased;

}
