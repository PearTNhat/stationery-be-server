package com.project.stationery_be_server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String image_id;


    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;  // Đường dẫn ảnh

    @Column(nullable = false)
    private int priority;  // Độ ưu tiên hiển thị

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductDetail product_type;
}
