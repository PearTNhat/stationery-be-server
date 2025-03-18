package com.project.stationery_be_server.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ProductColor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String product_color_id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "color_id")
    Color color;

    @OneToMany(mappedBy = "product_color")
    Set<Image> images;
}
