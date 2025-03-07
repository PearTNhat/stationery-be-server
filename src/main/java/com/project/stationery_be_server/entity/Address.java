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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String address_id;

    @Column(length = 100)
    private String addressName;

    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "user_id")
    User user;
}
