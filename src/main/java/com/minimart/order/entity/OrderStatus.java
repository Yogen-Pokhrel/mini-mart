package com.minimart.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("pending"),
    PROCESSING("processing"),
    SHIPPED("shipped"),
    DELIVERED("delivered"),
    CANCELLED("cancelled");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
