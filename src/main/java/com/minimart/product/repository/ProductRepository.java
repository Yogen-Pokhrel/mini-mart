package com.minimart.product.repository;

import com.minimart.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p where p.slug = :slug")
    Optional<Product> findBySlug(@Param("slug") String slug);


    @Query("SELECT p FROM Order o JOIN o.orderLineItems oi JOIN oi.product p WHERE p.seller.id = :sellerId")
    Page<Product> findOrderedProducts(@Param("sellerId") int sellerId, Pageable pageable);
}
