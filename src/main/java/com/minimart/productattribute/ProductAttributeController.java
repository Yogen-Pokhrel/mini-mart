package com.minimart.productattribute;

import com.minimart.common.ApiResponse;
import com.minimart.productattribute.dto.request.CreateProductAttributeDto;
import com.minimart.productattribute.dto.request.UpdateProductAttributeDto;
import com.minimart.productattribute.dto.response.ProductAttributeResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-attributes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductAttributeController {

    @Autowired
    private ProductAttributeService productAttributeService;

    @GetMapping
    private ApiResponse<List<ProductAttributeResponseDto>> findAll(){
        List<ProductAttributeResponseDto> productAttributes = productAttributeService.findAll();
        return ApiResponse.success(productAttributes, "Roles fetched successfully");
    }

    @GetMapping("/{id}")
    private ApiResponse<ProductAttributeResponseDto> findById(@PathVariable int id) throws Exception{
        ProductAttributeResponseDto productAttributes = productAttributeService.findById(id);
        return ApiResponse.success(productAttributes, "ProductAttributeResponseDtofetched successfully");
    }

    @PostMapping
    private ApiResponse<ProductAttributeResponseDto> create(@Valid @RequestBody CreateProductAttributeDto createDto) throws Exception{
        ProductAttributeResponseDto newProductAttributeResponseDto= productAttributeService.save(createDto);
        return ApiResponse.success(newProductAttributeResponseDto, "ProductAttributeResponseDtocreated successfully");
    }

    @PutMapping("/{id}")
    private ApiResponse<ProductAttributeResponseDto> update(@PathVariable int id,@Valid @RequestBody UpdateProductAttributeDto updateDto) throws Exception{
        ProductAttributeResponseDto  updatedProductAttributeResponseDto= productAttributeService.update(id, updateDto);
        return ApiResponse.success(updatedProductAttributeResponseDto, "ProductAttributeResponseDtoupdated successfully");
    }

    @DeleteMapping("/{id}")
    private ApiResponse<?> delete(@PathVariable int id) throws Exception{
        productAttributeService.delete(id);
        return ApiResponse.success(null, "ProductAttributeResponseDtodeleted successfully");
    }
}
