package com.project.stationery_be_server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id") // Liên kết đến User.userId
    User user;

    String title;
    String message;

    @Enumerated(EnumType.STRING)
    NotificationType type;

    Boolean isRead = false;
    LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "user_promotion_id")
    UserPromotion userPromotion;

    public enum NotificationType {
        ORDER_UPDATE,
        PROMOTION
    }
}
