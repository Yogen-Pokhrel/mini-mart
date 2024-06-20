package com.minimart.cart.dto.response;

import com.minimart.user.dto.response.SimpleUserDto;
import lombok.Data;

import java.util.List;

@Data
public class CartResponseDto {
    private SimpleUserDto user;
    private List<CartItemResponseDto> items;
    private float totalPrice;
}
