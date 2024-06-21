package com.minimart.order.dto.response;

import com.minimart.order.entity.OrderLineStatus;
import com.minimart.product.dto.response.ProductResponseDto;
import lombok.Data;

@Data
public class OrderLineItemResponseDto {
    private int id;
    private ProductResponseDto product;
    private int quantity;
    private float unitPrice;
    private float taxAmount;
    private float totalPrice;
    private OrderLineStatus status;
}
