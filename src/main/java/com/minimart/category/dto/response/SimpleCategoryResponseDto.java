package com.minimart.category.dto.response;

import com.minimart.productattribute.entity.ProductAttribute;
import lombok.Data;

import java.util.List;

@Data
public class SimpleCategoryResponseDto {
    private int id;
    private String title;
    private String slug;
}
