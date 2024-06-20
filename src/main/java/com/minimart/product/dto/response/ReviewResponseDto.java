package com.minimart.product.dto.response;

import com.minimart.user.dto.response.SimpleUserDto;
import lombok.Data;

@Data
public class ReviewResponseDto {
    private int id;
    private String comment;
    private SimpleUserDto customer;
}
