package com.project.stationery_be_server.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String address_id;

    @Column(length = 100)
    private String addressName;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "user_id")
    User user;
}
