package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, String> {
    @Query("SELECT pd FROM ProductDetail pd " +
            "WHERE pd.productColor.product.productId = :productId " +
            "AND pd.productColor.color.colorId = :colorId " +
            "AND pd.size.sizeId = :sizeId")
    Optional<ProductDetail> findByProductIdAndColorIdAndSizeId(
            @Param("productId") String productId,
            @Param("colorId") String colorId,
            @Param("sizeId") String sizeId);
}
