package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.ProductDetail;
import com.project.stationery_be_server.entity.ProductPromotion;
import com.project.stationery_be_server.entity.PurchaseOrder;
import com.project.stationery_be_server.entity.UserPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPromotionRepository extends JpaRepository<ProductPromotion, String> {

}
