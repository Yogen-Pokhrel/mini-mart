package com.minimart.product.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minimart.brand.entity.Brand;
import com.minimart.category.entity.ProductCategory;
import com.minimart.helpers.validators.ValidEnum;
import com.minimart.product.entity.ProductStatus;
import com.minimart.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProductDto {

    @Size(min = 1, max = 50, message = "Product title must be between 1 and 120 characters")
    private String title;

    @Size(min = 1, max = 50, message = "Product slug must be between 1 and 120 characters")
    private String slug;

    private String description;

    @Positive(message = "Price can be 0 or more")
    private float price;

    @Positive(message = "Stock can be 0 or more")
    private int stock = 0;

    @ValidEnum(enumClass = ProductStatus.class, message = "Please provide a valid status")
    private String productStatus;

    @Positive(message = "Please provide a valid category id")
    private int category_id;

    @Positive(message = "Please provide a valid brand id")
    private int brand_id;
}
