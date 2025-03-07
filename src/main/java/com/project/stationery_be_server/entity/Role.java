package com.project.stationery_be_server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "role_id")
    private String role_id;

    @Column(name = "role_name", length = 50)
    private String role_name;

    @Column(name = "description", length = 500)
    private String description;

    @OneToMany(mappedBy = "role",cascade = CascadeType.ALL)
    private List<Account>  account;
}
