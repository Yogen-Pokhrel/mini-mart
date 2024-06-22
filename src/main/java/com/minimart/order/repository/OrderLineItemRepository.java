package com.minimart.order.repository;

import com.minimart.order.entity.OrderLineItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Integer> {

    @Query("SELECT oi FROM OrderLineItem oi JOIN oi.product p WHERE p.seller.id = :sellerId")
    Page<OrderLineItem> findOrderedProducts(@Param("sellerId") int sellerId, Pageable pageable);

}
