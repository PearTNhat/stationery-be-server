package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, String> {
    Optional<Promotion> findByPromoCode(String promoCode);

    @Modifying
    @Query("UPDATE Promotion p " +
           "SET p.usageLimit = p.usageLimit - 1 " +
           "WHERE p.usageLimit is not null and  p.promotionId = :promotionId")
    int reduceUsageCountByPromotionId(String promotionId);
}
