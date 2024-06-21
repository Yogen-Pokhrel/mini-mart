package com.minimart.transactions;

import com.minimart.common.ApiResponse;
import com.minimart.transactions.dto.request.CreateTransactionDto;
import com.minimart.transactions.dto.response.TransactionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping
    public ApiResponse<List<TransactionResponseDto>> getTransactions() {
        List<TransactionResponseDto> transactions = transactionService.findAll();
        return ApiResponse.success(transactions, "Transactions fetched successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<TransactionResponseDto> getTransactions(@PathVariable int id) throws Exception {
        TransactionResponseDto transactions = transactionService.findById(id);
        return ApiResponse.success(transactions, "Transaction fetched successfully");
    }

    @GetMapping("/order/{orderId}")
    public ApiResponse<TransactionResponseDto> getTransactionsByOrderId(@PathVariable int orderId) throws Exception {
        TransactionResponseDto transactions = transactionService.findByOrderId(orderId);
        return ApiResponse.success(transactions, "Order Transaction fetched successfully");
    }

    @PostMapping
    public ApiResponse<TransactionResponseDto> createTransaction(@RequestBody CreateTransactionDto createTransactionDto) throws Exception {
        TransactionResponseDto newTransaction = transactionService.createTransaction(createTransactionDto);
        return ApiResponse.success(newTransaction, "Transaction created successfully");

    }
}
