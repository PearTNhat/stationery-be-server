package com.project.stationery_be_server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_promotion_id", length = 255, nullable = false)
    private String userPromotionId;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
