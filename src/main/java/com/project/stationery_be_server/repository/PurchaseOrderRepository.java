package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {
    Optional<PurchaseOrder> findByPurchaseOrderId(String purchaseOrderId);

    @Query("SELECT po FROM PurchaseOrder po LEFT JOIN FETCH po.purchaseOrderDetails WHERE po.status = :status")
    List<PurchaseOrder> findByStatusWithDetails(@Param("status") PurchaseOrder.Status status);

    @Query("SELECT po FROM PurchaseOrder po LEFT JOIN FETCH po.purchaseOrderDetails WHERE po.status != :status")
    List<PurchaseOrder> findByStatusNotWithDetails(@Param("status") PurchaseOrder.Status status);

    // Truy vấn đơn hàng theo userId và trạng thái kèm chi tiết
    @Query("SELECT po FROM PurchaseOrder po LEFT JOIN FETCH po.purchaseOrderDetails WHERE po.user.userId = :userId AND po.status = :status")
    List<PurchaseOrder> findByUser_UserIdAndStatusWithDetails(@Param("userId") String userId, @Param("status") PurchaseOrder.Status status);
}