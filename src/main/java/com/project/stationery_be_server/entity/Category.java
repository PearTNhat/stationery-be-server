package com.project.stationery_be_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String category_id;

    @Column(length = 30, nullable = false,unique = true)
    private String category_name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
