package com.minimart.category;

import com.minimart.category.dto.request.AssignAttributesDto;
import com.minimart.category.dto.request.CreateCategoryDto;
import com.minimart.category.dto.request.UpdateCategoryDto;
import com.minimart.category.dto.response.CategoryResponseDto;
import com.minimart.common.ApiResponse;
import com.minimart.helpers.Utilities;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {

    @Autowired
    CategoryService categoryService;


    @GetMapping
    private ApiResponse<List<CategoryResponseDto>> findAll(){
        List<CategoryResponseDto> roles = categoryService.findAll();
        return ApiResponse.success(roles, "Categories fetched successfully");
    }

    @GetMapping("/{id}")
    private ApiResponse<CategoryResponseDto> findById(@PathVariable int id) throws Exception{
        CategoryResponseDto roles = categoryService.findById(id);
        return ApiResponse.success(roles, "Category fetched successfully");
    }

    @PostMapping
    private ApiResponse<CategoryResponseDto> create(@Valid @RequestBody CreateCategoryDto createDto) throws Exception{
//        createDto.setSlug(Utilities.slugify(createDto.getSlug()));
        CategoryResponseDto newCategory = categoryService.save(createDto);
        return ApiResponse.success(newCategory, "Category created successfully");
    }

    @PutMapping("/{id}")
    private ApiResponse<CategoryResponseDto> update(@PathVariable int id,@Valid @RequestBody UpdateCategoryDto updateDto) throws Exception{
        CategoryResponseDto newCategory = categoryService.update(id, updateDto);
        return ApiResponse.success(newCategory, "Category updated successfully");
    }

    @DeleteMapping("/{id}")
    private ApiResponse<?> delete(@PathVariable int id) throws Exception{
        categoryService.delete(id);
        return ApiResponse.success(null, "Category deleted successfully");
    }

    @PutMapping("/{id}/assign-attributes")
    private ApiResponse<CategoryResponseDto> assignAttributes(@PathVariable int id,@Valid @RequestBody AssignAttributesDto assignAttributesDto) throws Exception{
        categoryService.assignAttributes(id, assignAttributesDto.getAttributeIds());
        CategoryResponseDto updatedCategory = categoryService.findById(id);
        return ApiResponse.success(updatedCategory, "Attributes assigned successfully");
    }

}
