package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail,String> {
    ProductDetail findBySlug(String slug);
}
