package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.Cart;
import com.project.stationery_be_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, String> {
    List<Cart> findByUser_UserId(String userId);
    void deleteByUser_UserId(String userId);
}
