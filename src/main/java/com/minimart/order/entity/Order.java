package com.minimart.order.entity;

import com.minimart.common.BaseEntity;
import com.minimart.common.RecordType;
import com.minimart.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="\"order\"")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0.0")
    private float amount;

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0.0")
    private float discountAmount;

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0.0")
    private float taxAmount;

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0.0")
    private float shippingAmount;

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0.0")
    private float totalAmount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();
}
