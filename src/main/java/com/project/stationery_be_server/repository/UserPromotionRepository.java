package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.User;
import com.project.stationery_be_server.entity.UserPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPromotionRepository extends JpaRepository<UserPromotion, String> {
    List<UserPromotion> findByUserUserId(String userId);

}
