package com.minimart.transactions.dto.response;

import com.minimart.user.dto.response.SimpleUserDto;
import lombok.Data;

@Data
public class TransactionResponseDto {

    private int id;
    private float amount;
    private SimpleUserDto customer;

}
