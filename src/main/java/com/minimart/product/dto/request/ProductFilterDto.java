package com.minimart.product.dto.request;

import lombok.Data;

@Data
public class ProductFilterDto {
    private String name;
    private int brandId;
    private int categoryId;
    private float maxPrice;
    private float minPrice;
    private int sellerId;
    private String status;
}
