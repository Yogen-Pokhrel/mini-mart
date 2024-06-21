package com.minimart.transactions;

import com.minimart.transactions.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t from Transaction t where t.order.id = :orderId")
    Optional<Transaction> findByOrderId(@Param("orderId") int orderId);
}
