package com.project.stationery_be_server.specification;

import com.project.stationery_be_server.dto.request.ProductFilterRequest;
import com.project.stationery_be_server.entity.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> filterProducts(ProductFilterRequest filter) {
        return (root, query, criteriaBuilder) -> {
            //predicates là danh sách các điều kiện (WHERE ...).
            List<Predicate> predicates = new ArrayList<>();
            // Join Product → ProductColor → ProductDetail
            Join<Object, Object> productColorJoin = root.join("productColors", JoinType.INNER);
            Join<Object, Object> productDetailJoin = productColorJoin.join("productDetails", JoinType.INNER);
            if (filter.getCategoryId() != null && !filter.getCategoryId().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("categoryId"), filter.getCategoryId()));
            }

            if (filter.getMinPrice() != null && !filter.getMinPrice().isBlank()) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(productDetailJoin.get("discountPrice"), filter.getMinPrice()));
            }

            if (filter.getMaxPrice() != null && !filter.getMaxPrice().isBlank()) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(productDetailJoin.get("discountPrice"), filter.getMaxPrice()));
            }

            if (filter.getSearch() != null && !filter.getSearch().isBlank()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getSearch().trim() + "%"));
            }

            if (filter.getTotalRating() != null && !filter.getTotalRating().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("totalRating"), filter.getTotalRating()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}