package com.project.stationery_be_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "size_id", length = 10) //
    String sizeId;

    @Column(name = "name", nullable = false, length = 3, unique = true)
    String name;

    @Column(name = "priority", nullable = false, length = 3, unique = true)
    String priority;

    @OneToMany(mappedBy = "size", fetch = FetchType.LAZY)
    @JsonIgnore
    Set<ProductDetail> productDetails;
}
