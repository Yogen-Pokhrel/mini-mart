package com.minimart.brand;

import com.minimart.brand.dto.request.CreateBrandDto;
import com.minimart.brand.dto.request.UpdateBrandDto;
import com.minimart.brand.dto.response.BrandResponseDto;
import com.minimart.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BrandController {

    @Autowired
    BrandService brandService;


    @GetMapping
    private ApiResponse<List<BrandResponseDto>> findAll(){
        List<BrandResponseDto> roles = brandService.findAll();
        return ApiResponse.success(roles, "Brands fetched successfully");
    }

    @GetMapping("/{id}")
    private ApiResponse<BrandResponseDto> findById(@PathVariable int id) throws Exception{
        BrandResponseDto roles = brandService.findById(id);
        return ApiResponse.success(roles, "Brand fetched successfully");
    }

    @PostMapping
    private ApiResponse<BrandResponseDto> create(@Valid @RequestBody CreateBrandDto createDto) throws Exception{
        BrandResponseDto newCategory = brandService.save(createDto);
        return ApiResponse.success(newCategory, "Category created successfully");
    }

    @PutMapping("/{id}")
    private ApiResponse<BrandResponseDto> update(@PathVariable int id,@Valid @RequestBody UpdateBrandDto updateDto) throws Exception{
        BrandResponseDto newCategory = brandService.update(id, updateDto);
        return ApiResponse.success(newCategory, "Brand updated successfully");
    }

    @DeleteMapping("/{id}")
    private ApiResponse<?> delete(@PathVariable int id) throws Exception{
        brandService.delete(id);
        return ApiResponse.success(null, "Brand deleted successfully");
    }
}
