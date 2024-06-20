package com.minimart.cart.dto.request;

import lombok.Data;

@Data
public class RemoveCartItemDto {
    private int userId;
    private int cartItemId;
}
