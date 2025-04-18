package com.project.stationery_be_server.listener;

import com.project.stationery_be_server.entity.Image;
import com.project.stationery_be_server.entity.Product;
import com.project.stationery_be_server.entity.ProductDetail;
import com.project.stationery_be_server.holder.ImageRepositoryHolder;
import com.project.stationery_be_server.holder.ProductDetailRepositoryHolder;
import com.project.stationery_be_server.repository.ProductDetailRepository;
import com.project.stationery_be_server.service.ProductService;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductEntityListener {
    @PostLoad
    public void filterProductDetail(Product product) {
        log.info("________Call filterProductDetail_______");
        if (product.getProductDetails() != null && product.getMinPrice() != null) {
            ProductDetail filtered = product.getProductDetails().stream()
                    .filter(pd -> pd.getDiscountPrice() == product.getMinPrice())
                    .findFirst()
                    .orElse(null);
            product.setProductDetail(filtered);
            if (filtered != null && ProductDetailRepositoryHolder.productDetailRepository != null) {
                filtered.setFetchColors(
                        ProductDetailRepositoryHolder.productDetailRepository
                                .findColorSlugByProductId(product.getProductId())
                );
            }
            ProductDetail detail = product.getProductDetail();
            if (detail != null && detail.getColor() != null && ImageRepositoryHolder.imageRepository != null) {
                Image image = ImageRepositoryHolder.imageRepository
                        .findFirstByProduct_ProductIdAndColor_ColorIdOrderByPriorityAsc(
                                product.getProductId(),
                                detail.getColor().getColorId()
                        );
                if (image != null) {
                    detail.setImages(List.of(image));
                } else {
                    detail.setImages(List.of()); // hoặc null tùy bạn
                }
            }

        }

    }
}
