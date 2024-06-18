package com.minimart.product.entity;

import lombok.Getter;

@Getter
public enum ProductStatus {
    AVAILABLE(1),
    OFFLINE(2);

    private final int status;

    ProductStatus(int status) {
        this.status = status;
    }
}
