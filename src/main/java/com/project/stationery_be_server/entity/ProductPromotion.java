package com.project.stationery_be_server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_promotion_id", length = 255, nullable = false)
    private String userPromotionId;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "product_detail_id", nullable = false)
    private ProductDetail productDetail;

    @OneToMany(mappedBy = "productPromotion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PurchaseOrderDetail> purchaseOrderDetail;
}
