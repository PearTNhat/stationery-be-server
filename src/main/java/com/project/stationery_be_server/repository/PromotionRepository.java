package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, String> {
    Optional<Promotion> findByPromoCode(String promoCode);
}
