package com.project.stationery_be_server.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "category_id")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String category_id;

    @Column(length = 30, nullable = false, unique = true)
    private String category_name;

    @Column(length = 10, nullable = false)
    private String icon;

    @Column(length = 20, nullable = false)
    private String bg_color;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;
}
