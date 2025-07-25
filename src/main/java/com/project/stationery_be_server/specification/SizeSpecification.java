package com.project.stationery_be_server.specification;

import com.project.stationery_be_server.dto.request.UserFilterRequest;
import com.project.stationery_be_server.entity.Size;
import com.project.stationery_be_server.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SizeSpecification {
    public static Specification<Size> filterUsersForSize(String  search) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            // Filter theo roleId
            if (search != null && !search.trim().isEmpty()) {
                String keyword = "%" + search.trim().toLowerCase() + "%";

                Predicate byId = criteriaBuilder.like(criteriaBuilder.lower(root.get("sizeId")), keyword);
                Predicate byName = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keyword);

                predicates.add(criteriaBuilder.or(byId, byName));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
