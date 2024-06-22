package com.minimart.order.entity;

import lombok.Getter;

@Getter
public enum OrderLineStatus {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    RETURN_REQUEST,
    RETURN_PROCESSING,
    RETURNED
}
