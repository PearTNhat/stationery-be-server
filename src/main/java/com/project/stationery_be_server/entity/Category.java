package com.project.stationery_be_server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

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
    @JsonBackReference
    private Set<Product> products;
}
