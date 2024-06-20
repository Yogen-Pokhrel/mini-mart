package com.minimart.cart.dto.request;

import lombok.Data;

@Data
public class AddCartItemDto {
    private int productId;
    private int quantity;
}
