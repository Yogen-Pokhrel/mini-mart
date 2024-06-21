package com.minimart.transactions.entity;

import com.minimart.order.entity.Order;
import com.minimart.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private float amount;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TransactionStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TransactionType type;

    @Column(nullable = false, updatable = false)
    private LocalDateTime addedOn;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User updatedBy;

    private LocalDateTime updatedOn;

    @PrePersist
    protected void onCreate() {
        addedOn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedOn = LocalDateTime.now();
    }
}
