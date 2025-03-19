package com.project.stationery_be_server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String review_id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String content;
    private double rating;
    private String review_image;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "review_id",unique = true)
    private Review parent_review;

    @OneToMany(mappedBy = "parent_review", cascade = CascadeType.ALL)
    private Set<Review> child_reviews;

    private Integer reply_onUser;
    private Date create_at;
}
