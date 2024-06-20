package com.minimart.order.dto.response;

import com.minimart.order.entity.OrderStatus;
import com.minimart.user.dto.response.SimpleUserDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponseDto {
    private int id;
    private SimpleUserDto customer;
    private OrderStatus status;
    private List<OrderLineItemResponseDto> orderLineItems;
}
