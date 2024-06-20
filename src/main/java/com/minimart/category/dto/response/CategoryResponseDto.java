package com.minimart.category.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.minimart.category.entity.ProductCategory;
import com.minimart.productattribute.entity.ProductAttribute;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponseDto {
    private int id;
    private String title;
    private String slug;
    private List<CategoryResponseDto> children;
    private List<ProductAttribute> attributes;
}
