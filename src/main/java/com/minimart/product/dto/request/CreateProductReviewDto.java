package com.minimart.product.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minimart.product.entity.Product;
import com.minimart.user.entity.User;
import lombok.Data;

@Data
public class CreateProductReviewDto {
    private String comment;
    private int productId;
    private int customerId;
}
