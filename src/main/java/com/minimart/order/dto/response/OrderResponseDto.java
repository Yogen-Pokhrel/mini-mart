package com.minimart.order.dto.response;

import com.minimart.user.dto.response.SimpleUserDto;
import lombok.Data;

@Data
public class OrderResponseDto {
    private int id;
    private SimpleUserDto customer;
}
