package com.project.stationery_be_server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String image_id;


    @Column(nullable = false, columnDefinition = "TEXT")
    String url;  // Đường dẫn ảnh

    @Column(nullable = false)
    int priority;  // Độ ưu tiên hiển thị

    @ManyToOne
    @JoinColumn(name="product_color_id")
    ProductColor product_color;
}
