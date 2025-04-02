package com.project.stationery_be_server.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "productColorId")
public class ProductColor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_color_id")
    private String productColorId;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;

    @OneToMany(mappedBy = "productColor")
    private Set<ProductDetail> productDetails;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "productColor")
    @OrderBy("priority ASC")
    private Set<Image> images;
}
