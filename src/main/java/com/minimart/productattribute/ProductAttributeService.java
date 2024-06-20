package com.minimart.productattribute;

import com.minimart.common.CommonService;
import com.minimart.common.RecordType;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.helpers.ListMapper;
import com.minimart.productattribute.dto.request.CreateProductAttributeDto;
import com.minimart.productattribute.dto.request.UpdateProductAttributeDto;
import com.minimart.productattribute.dto.response.ProductAttributeResponseDto;
import com.minimart.productattribute.entity.ProductAttribute;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAttributeService implements CommonService<CreateProductAttributeDto, UpdateProductAttributeDto, ProductAttributeResponseDto, Integer> {
    @Autowired
    ProductAttributeRepository productAttributeRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ListMapper listMapper;

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductAttributeResponseDto> findAll() {
        return (List<ProductAttributeResponseDto>) listMapper.mapList(productAttributeRepository.findAll(),new ProductAttributeResponseDto());
    }


    @Override
    public ProductAttributeResponseDto findById(Integer id) throws Exception {
        return modelMapper.map(productAttributeRepository.findById(id),ProductAttributeResponseDto.class);
    }

    @Override
    public ProductAttributeResponseDto save(CreateProductAttributeDto createDto) throws Exception {
        RecordType recordType = RecordType.valueOf(createDto.getDataType());
        createDto.setType(recordType);
        ProductAttribute productAttribute = modelMapper.map(createDto, ProductAttribute.class);
        ProductAttribute newProductAttribute = productAttributeRepository.save(productAttribute);
        return modelMapper.map(newProductAttribute,ProductAttributeResponseDto.class);
    }

    @Override
    public ProductAttributeResponseDto update(Integer id, UpdateProductAttributeDto updateDto) throws Exception {
        ProductAttribute existingRecord = productAttributeRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No Attribute found with provided id"));
        if(updateDto.getDataType() != null){
            RecordType recordType = RecordType.valueOf(updateDto.getDataType());
            existingRecord.setType(recordType);
        }
        if(updateDto.getName() != null){
            existingRecord.setName(updateDto.getName());
        }
        if(updateDto.getUnit() != null){
            existingRecord.setUnit(updateDto.getUnit());
        }

        existingRecord.setType(updateDto.getType());
        ProductAttribute updatedRecord = productAttributeRepository.save(existingRecord);
        return modelMapper.map(updatedRecord, ProductAttributeResponseDto.class);
    }

    @Override
    public void delete(Integer id) throws Exception {
        findById(id);
        productAttributeRepository.deleteById(id);
    }
}
