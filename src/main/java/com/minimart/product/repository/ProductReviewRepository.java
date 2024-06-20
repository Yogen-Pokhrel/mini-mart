package com.minimart.product.repository;

import com.minimart.product.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {

    @Query("SELECT pr FROM ProductReview pr WHERE pr.product.id = :productId")
    List<ProductReview> findAllByProductId(@Param("productId") int productId);

}
