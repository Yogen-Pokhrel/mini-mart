package com.minimart.cart.dto.response;

import com.minimart.product.dto.response.ProductResponseDto;
import lombok.Data;

@Data
public class CartItemResponseDto {
    private int id;
    private ProductResponseDto product;
    private int quantity;
    private float price;
}
