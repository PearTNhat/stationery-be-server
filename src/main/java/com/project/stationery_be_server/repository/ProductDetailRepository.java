package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.dto.response.ColorSizeSlugResponse;
import com.project.stationery_be_server.dto.response.ColorSlugResponse;
import com.project.stationery_be_server.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, String> {
    @Query("SELECT pd FROM ProductDetail pd " +
            "WHERE pd.product.productId = :productId " +
            "AND pd.color.colorId = :colorId " +
            "AND pd.size.sizeId = :sizeId")
    Optional<ProductDetail> findByProductIdAndColorIdAndSizeId(
            @Param("productId") String productId,
            @Param("colorId") String colorId,
            @Param("sizeId") String sizeId);

    // ✅ Truy vấn mới hỗ trợ nullable colorId & sizeId
    @Query("SELECT pd FROM ProductDetail pd " +
            "WHERE pd.product.productId = :productId " +
            "AND (:colorId IS NULL OR pd.color.colorId = :colorId) " +
            "AND (:sizeId IS NULL OR pd.size.sizeId = :sizeId)")
    Optional<ProductDetail> findByProductIdAndOptionalColorIdAndOptionalSizeId(
            @Param("productId") String productId,
            @Param("colorId") String colorId,
            @Param("sizeId") String sizeId);

    ProductDetail findBySlug(String slug);

    ProductDetail findByProductDetailId(String productDetailId);


   @Query(value= """
             SELECT
               c.color_id AS colorId,
               c.hex AS hex,
               JSON_ARRAYAGG(
                   JSON_OBJECT('slug', pd_sized.slug, 'size', pd_sized.size_id)
               ) AS sizes
           FROM (
               SELECT
                   pd.slug,
                   pd.size_id,
                   pd.color_id
               FROM product_detail pd
               JOIN size s ON pd.size_id = s.size_id
           ) AS pd_sized
           JOIN color c ON pd_sized.color_id = c.color_id
           GROUP BY c.color_id, c.hex
           """,nativeQuery = true)
    List<ColorSizeSlugResponse> findColorSlugByProductId(String productId);

    @Query(value = """
    SELECT
        pd.color_id AS colorId,
        c.hex AS hex,
        MIN(pd.slug) AS slug  -- chọn slug bất kỳ, ở đây là nhỏ nhất theo bảng chữ cái
    FROM
        product_detail pd
    JOIN 
        color c ON pd.color_id = c.color_id
    WHERE 
        pd.product_id = :productId
        and
        pd.color_id != :colorId
    GROUP BY 
        pd.color_id, c.hex
    """, nativeQuery = true)
    List<ColorSlugResponse> findDistinctColorsWithAnySlug(String productId, String colorId);
}
