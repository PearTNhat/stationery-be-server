package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductColorRepository extends JpaRepository<ProductColor, String> {
    List<ProductColor> findByProduct_ProductId(String productId);
}
