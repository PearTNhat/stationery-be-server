package com.project.stationery_be_server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "imageId")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "image_id")
    String imageId;

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    String url;  // Đường dẫn ảnh

    @Column(name = "priority", nullable = false)
    int priority;  // Độ ưu tiên hiển thị

    @ManyToOne
    @JoinColumn(name = "product_color_id", nullable = false)
    ProductColor productColor;
}
