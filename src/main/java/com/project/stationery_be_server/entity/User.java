package com.project.stationery_be_server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
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

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @OneToMany(mappedBy="user")
    List<Address> addresses;
}
