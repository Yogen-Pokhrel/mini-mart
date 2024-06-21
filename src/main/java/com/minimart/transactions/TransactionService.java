package com.minimart.transactions;

import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.order.repository.OrderRepository;
import com.minimart.order.entity.Order;
import com.minimart.order.entity.OrderStatus;
import com.minimart.transactions.dto.request.CreateTransactionDto;
import com.minimart.transactions.dto.response.TransactionResponseDto;
import com.minimart.transactions.entity.Transaction;
import com.minimart.transactions.entity.TransactionStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ModelMapper modelMapper;

    @SuppressWarnings("unchecked")
    public List<TransactionResponseDto> findAll(){
        return (List<TransactionResponseDto>)modelMapper.map(transactionRepository.findAll(), TransactionResponseDto.class);
    }

    public TransactionResponseDto findById(int id) throws Exception {
        Transaction t = transactionRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("Transaction not found"));
        return modelMapper.map(t, TransactionResponseDto.class);
    }

    public TransactionResponseDto findByOrderId(int orderId) throws Exception {
        Transaction t = transactionRepository.findByOrderId(orderId).orElseThrow(() -> new NoResourceFoundException("Transaction not found for the order"));
        return modelMapper.map(t, TransactionResponseDto.class);
    }

    public TransactionResponseDto createTransaction(CreateTransactionDto createTransactionDto) throws Exception {
        Order order = orderRepository.findById(createTransactionDto.getOrderId()).orElseThrow(() -> new NoResourceFoundException("No order exist with provided id"));
        if(order.getStatus() != OrderStatus.PROCESSING){
            throw new Exception("This order cannot be processed further as it is already processed or canceled");
        }
        //server verification for transaction validity started
        //server verification for transaction validity ends

        Transaction transaction = new Transaction();
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setOrder(order);
        transaction.setAmount(createTransactionDto.getAmount());
        transaction.setCustomer(order.getCustomer());
        transactionRepository.save(transaction);
        return modelMapper.map(transaction, TransactionResponseDto.class);
    }
}
