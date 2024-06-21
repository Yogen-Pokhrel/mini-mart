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
public class CreateProductDto {

    @NotBlank(message = "Product title name is mandatory")
    @Size(min = 1, max = 50, message = "Product title must be between 1 and 120 characters")
    private String title;

    @NotBlank(message = "Product slug name is mandatory")
    @Size(min = 1, max = 50, message = "Product slug must be between 1 and 120 characters")
    private String slug;

    private String description;

    @Positive(message = "Price can be 0 or more")
    private float price;

    @Positive(message = "Stock can be 0 or more")
    private int stock = 0;

    @ValidEnum(enumClass = ProductStatus.class, message = "Please provide a valid status")
    private String productStatus = ProductStatus.OFFLINE.toString();

    @Positive(message = "Please provide a valid category id")
    private int category_id;

    @Positive(message = "Please provide a valid brand id")
    private int brand_id;

    @JsonIgnore
    private int seller_id = 0;

    @JsonIgnore
    private Brand brand;

    @JsonIgnore
    private User seller;

    @JsonIgnore
    private ProductCategory productCategory;

    @JsonIgnore
    private ProductStatus status;
}
