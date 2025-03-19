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
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String purchase_order_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Date created_at;
    @OneToMany(mappedBy = "purchase_order", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<PurchaseOrderDetail> purchase_orders;
}
