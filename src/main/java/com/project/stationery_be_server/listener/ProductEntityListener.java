package com.project.stationery_be_server.listener;

import com.project.stationery_be_server.entity.Product;
import com.project.stationery_be_server.service.ProductService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductEntityListener {
//    ProductService productService;
//
//    @PostPersist
//    @PostUpdate
//    public void afterSave(Product product) {
//        productService.updateMinPrice(product);
//    }
}
