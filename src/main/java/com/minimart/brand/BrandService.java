package com.minimart.brand;

import com.minimart.brand.dto.request.CreateBrandDto;
import com.minimart.brand.dto.request.UpdateBrandDto;
import com.minimart.brand.dto.response.BrandResponseDto;
import com.minimart.brand.entity.Brand;
import com.minimart.common.CommonService;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.helpers.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService implements CommonService<CreateBrandDto, UpdateBrandDto, BrandResponseDto, Integer> {
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    ListMapper listMapper;

    @Override
    @SuppressWarnings("unchecked")
    public List<BrandResponseDto> findAll() {
        return (List<BrandResponseDto>) listMapper.mapList(brandRepository.findAll(),new BrandResponseDto());
    }

    @Override
    public BrandResponseDto findById(Integer id) throws Exception {
        Brand recordData = brandRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No Category found with provided id"));
        return modelMapper.map(recordData, BrandResponseDto.class);
    }

    @Override
    public BrandResponseDto save(CreateBrandDto createDto) throws Exception {
        Brand newRecord = brandRepository.save(modelMapper.map(createDto, Brand.class));
        return modelMapper.map(newRecord, BrandResponseDto.class);
    }

    @Override
    public BrandResponseDto update(Integer id, UpdateBrandDto updateDto) throws Exception {
        Brand existingRecord = brandRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No brand found with provided id"));
        existingRecord.setName(updateDto.getName());
        Brand updatedRecord = brandRepository.save(existingRecord);
        return modelMapper.map(updatedRecord, BrandResponseDto.class);
    }

    @Override
    public void delete(Integer id) throws Exception {
        findById(id);
        brandRepository.deleteById(id);
    }
}
