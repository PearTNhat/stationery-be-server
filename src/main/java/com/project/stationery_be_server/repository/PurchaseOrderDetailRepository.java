package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.PurchaseOrderDetail;
import com.project.stationery_be_server.entity.PurchaseOrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, PurchaseOrderDetailId> {
    long countByProductDetail_ProductDetailId(String productDetailId);
}