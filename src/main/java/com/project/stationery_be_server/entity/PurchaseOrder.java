package com.project.stationery_be_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "purchase_order_id")
    private String purchaseOrderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PurchaseOrderDetail> purchaseOrderDetails;

    @Column(name = "pdf_url", length = 500)
    private String pdfUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private Status status;

    @Column(name = "product_promotion_id", length = 255)
    private String productPromotionId;

    @Column(name = "user_promotion_id", length = 255)
    private String userPromotionId;

    @Column(name = "amount", precision = 19, scale = 4)
    private BigDecimal amount;

    public enum Status {
        PENDING,        // Chờ xác nhận
        PROCESSING,     // Đang xử lý
        SHIPPING,       // Đang giao
        COMPLETED,      // Hoàn thành
        CANCELED        // Đã hủy
    }
}
