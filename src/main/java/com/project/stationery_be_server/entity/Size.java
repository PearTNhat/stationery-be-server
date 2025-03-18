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
public class Size {
    @Id
    @Column(length = 10)
    String size_id;

    @Column(nullable = false, length = 3,unique = true)
    String name;

    @OneToMany(mappedBy = "size",fetch = FetchType.LAZY)
    Set<ProductDetail> product_detail;

}
