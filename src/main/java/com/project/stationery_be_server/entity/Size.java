package com.project.stationery_be_server.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "size_id", length = 10) // Giữ snake_case trong DB
    String sizeId;

    @Column(name = "name", nullable = false, length = 3, unique = true)
    String name;

    @OneToMany(mappedBy = "size", fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<ProductDetail> productDetails;
}
