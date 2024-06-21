package com.minimart.transactions.dto.request;

import lombok.Data;

@Data
public class CreateTransactionDto {
    private int orderId;
    private int amount;
}
