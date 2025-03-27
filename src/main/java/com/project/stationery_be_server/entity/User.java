package com.project.stationery_be_server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String user_id;

    @Column(length = 50)
    String first_name;

    @Column(length = 50)
    String last_name;

    @Column(length = 100)
    String email;

    @Column(length = 15)
    String phone;

    @Column(length = 100)
    String password;

    @Temporal(TemporalType.DATE)
    Date dob;

    @Column(length = 255)
    String avatar;

    boolean isBlock;

    Integer otp;

    @Temporal(TemporalType.TIMESTAMP)
    Date otpCreatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonManagedReference
    Role role;

    @OneToMany(mappedBy="user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonManagedReference
    Set<Address> addresses;


    @OneToMany(mappedBy="user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonManagedReference
    Set<Cart> carts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    Set<PurchaseOrder> orders;
}
