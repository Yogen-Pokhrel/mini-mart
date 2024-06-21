package com.minimart.order.entity;

import com.minimart.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private float unitPrice;
    private float taxAmount;
    private float totalPrice;

    @Column(nullable = true)
    @Enumerated(EnumType.ORDINAL)
    private OrderLineStatus status;

}
