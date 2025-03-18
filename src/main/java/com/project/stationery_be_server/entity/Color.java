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
public class Color {
    @Id
    @Column(length = 10)
    String color_id;

    @Column(nullable = false, length = 10,unique = true)
    String name;

    @Column(nullable = false, length = 7)
    String hex;

    @OneToMany(mappedBy = "color",fetch = FetchType.LAZY)
    Set<ProductDetail> product_detail;
}
