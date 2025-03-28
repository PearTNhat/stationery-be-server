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
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_detail_id")
    private String productDetailId;

    @Column(length = 100)
    private String slug;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @Column(name = "original_price")
    private int originalPrice;

    @Column(name = "discount_price")
    private int discountPrice;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "size_id")
    private Size size;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "product_color_id", nullable = false)
    private ProductColor productColor;

    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PurchaseOrderDetail> purchaseOrderDetails;
}
