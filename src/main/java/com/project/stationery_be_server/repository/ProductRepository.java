package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT p FROM Product p WHERE (:categoryId IS NULL OR p.category.categoryId = :categoryId)")
    Page<Product> findAllByCategoryName(@Param("categoryName") String categoryId, Pageable pageable);
}
