package com.project.stationery_be_server.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "promotion_id", length = 255, nullable = false)
    private String promotionId;

    @Column(name = "promo_code", length = 50, nullable = false, unique = true)
    private String promoCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", length = 10, nullable = false)
    private DiscountType discountType;

    @Column(name = "discount_value", precision = 19, scale = 2, nullable = false)
    private BigDecimal discountValue;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "max_value", precision = 19, scale = 2)
    private BigDecimal maxValue;

    @Column(name = "min_order_value", precision = 19, scale = 2)
    private BigDecimal minOrderValue;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "promotion")
    @JsonIgnore
    private Set<ProductDetail> productDetails;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum DiscountType {
        PERCENTAGE, VALUE
    }
}
