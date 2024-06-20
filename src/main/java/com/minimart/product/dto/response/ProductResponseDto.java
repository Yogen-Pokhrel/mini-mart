package com.minimart.product.dto.response;

import com.minimart.brand.dto.response.BrandResponseDto;
import com.minimart.brand.entity.Brand;
import com.minimart.category.dto.response.CategoryResponseDto;
import com.minimart.category.dto.response.SimpleCategoryResponseDto;
import com.minimart.category.entity.ProductCategory;
import com.minimart.product.entity.ProductStatus;
import com.minimart.user.dto.response.SimpleUserDto;
import com.minimart.user.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponseDto {
    private int id;
    private String title;
    private String slug;
    private float price;
    private int stock;
    private String description;
    private SimpleCategoryResponseDto category;
    private BrandResponseDto brand;
    private SimpleUserDto seller;
    private ProductStatus status;
    private List<ProductImageResponseDto> images;
}
