package com.minimart.category.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minimart.category.entity.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryDto {

    @NotBlank(message = "Title is mandatory")
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")

    private String title;

    @NotBlank(message = "Slug is mandatory")
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    private String slug;

    private String description;

    @Positive(message = "Parent category Id should be positive integer")
    private Integer parentCategoryId;

    @JsonIgnore
    private ProductCategory parent;
}
