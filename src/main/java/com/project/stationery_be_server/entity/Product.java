package com.project.stationery_be_server.entity;

import com.fasterxml.jackson.annotation.*;
import com.project.stationery_be_server.listener.ProductEntityListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(ProductEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private String productId;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "slug", length = 100, nullable = false)
    private String slug;
    @Column(name="min_price")
    private Integer minPrice;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({"icon", "bgColor", "products"})
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ProductColor> productColors;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Review> reviews;

    @Column(name = "total_rating")
    private Double totalRating;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
