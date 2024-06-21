package com.minimart.category;

import com.minimart.category.dto.request.CreateCategoryDto;
import com.minimart.category.dto.request.UpdateCategoryDto;
import com.minimart.category.dto.response.CategoryResponseDto;
import com.minimart.category.entity.ProductCategory;
import com.minimart.common.CommonService;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.helpers.ListMapper;
import com.minimart.productattribute.ProductAttributeRepository;
import com.minimart.productattribute.entity.ProductAttribute;
import com.minimart.user.entity.User;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements CommonService<CreateCategoryDto, UpdateCategoryDto, CategoryResponseDto, Integer> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    ListMapper listMapper;

    @Override
    @SuppressWarnings("unchecked")
    public List<CategoryResponseDto> findAll() {
        return (List<CategoryResponseDto>) listMapper.mapList(categoryRepository.findAll(),new CategoryResponseDto());
    }

    @Override
    public CategoryResponseDto findById(Integer id) throws Exception {
        ProductCategory recordData = categoryRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No Category found with provided id"));
        return modelMapper.map(recordData, CategoryResponseDto.class);
    }

    @Override
    public CategoryResponseDto save(CreateCategoryDto createCategoryDto) throws Exception {
        Integer parentCategoryId = createCategoryDto.getParentCategoryId();
        createCategoryDto.setParentCategoryId(-1);
        ProductCategory newProductCategory = modelMapper.map(createCategoryDto, ProductCategory.class);
        ProductCategory parentCategory = null;
        if(parentCategoryId != null){
            parentCategory = categoryRepository.findById(parentCategoryId).orElseThrow(() -> new NoResourceFoundException("No Category found with provided id"));
        }
        ProductCategory productCategory = categoryRepository.save(newProductCategory);
        if (parentCategory != null) {
            productCategory.setParent(parentCategory);
            categoryRepository.save(productCategory);
        }

        return modelMapper.map(productCategory, CategoryResponseDto.class);
    }

    @Override
    public CategoryResponseDto update(Integer id, UpdateCategoryDto updateDto) throws Exception {
        ProductCategory existingRole = categoryRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No Category found with provided id"));
        existingRole.setTitle(updateDto.getTitle());
        ProductCategory updatedRole = categoryRepository.save(existingRole);
        return modelMapper.map(updatedRole, CategoryResponseDto.class);
    }

    @Override
    public void delete(Integer id) throws Exception {
        findById(id);
        categoryRepository.deleteById(id);
    }

    public void assignAttributes(Integer id, List<Integer> attributeIds) throws Exception{
        ProductCategory recordData = categoryRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No Category found with provided id"));
        List<ProductAttribute> attributeData = productAttributeRepository.findAllByIds(attributeIds);
        recordData.setAttributes(attributeData);
        categoryRepository.save(recordData);
    }
}
