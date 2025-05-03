package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository  extends JpaRepository<Image, String> {
    List<Image> findByProduct_ProductIdAndColor_ColorIdOrderByPriorityAsc(String productId,String colorId);
    Image findFirstByProduct_ProductIdAndColor_ColorIdOrderByPriorityAsc(String productId, String colorId);
    long countByProduct_ProductId(String productId);
}
