package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
