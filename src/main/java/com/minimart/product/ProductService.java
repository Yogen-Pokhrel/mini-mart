package com.minimart.product;

import com.minimart.brand.BrandRepository;
import com.minimart.brand.entity.Brand;
import com.minimart.category.CategoryRepository;
import com.minimart.category.entity.ProductCategory;
import com.minimart.common.CommonService;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.helpers.ListMapper;
import com.minimart.product.dto.request.CreateProductDto;
import com.minimart.product.dto.request.UpdateProductDto;
import com.minimart.product.dto.response.ProductImageResponseDto;
import com.minimart.product.dto.response.ProductResponseDto;
import com.minimart.product.entity.Product;
import com.minimart.product.entity.ProductImage;
import com.minimart.product.entity.ProductStatus;
import com.minimart.product.repository.ProductImageRepository;
import com.minimart.product.repository.ProductRepository;
import com.minimart.user.entity.User;
import com.minimart.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements CommonService<CreateProductDto, UpdateProductDto, ProductResponseDto, Integer> {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    ListMapper listMapper;

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductResponseDto> findAll() {
        return (List<ProductResponseDto>) listMapper.mapList(productRepository.findAll(),new ProductResponseDto());
    }

    @Override
    public ProductResponseDto findById(Integer id) throws Exception {
        Product recordData = productRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No product found with provided id"));
        return modelMapper.map(recordData, ProductResponseDto.class);
    }

    @Override
    public ProductResponseDto save(CreateProductDto createDto) throws Exception {
        ProductCategory category = categoryRepository.findById(createDto.getCategory_id()).orElseThrow(() -> new NoResourceFoundException("No Category found with provided id"));
        User user = userRepository.findById(createDto.getSeller_id()).orElseThrow(() -> new NoResourceFoundException("No user found with provided id"));
        Brand brand = brandRepository.findById(createDto.getSeller_id()).orElseThrow(() -> new NoResourceFoundException("No brand found with provided id"));
        ProductStatus recordType = ProductStatus.valueOf(createDto.getProductStatus());

        createDto.setStatus(recordType);
        createDto.setProductCategory(category);
        createDto.setBrand(brand);
        createDto.setSeller(user);

        Product newRecord = productRepository.save(modelMapper.map(createDto, Product.class));
        return modelMapper.map(newRecord, ProductResponseDto.class);
    }

    @Override
    public ProductResponseDto update(Integer id, UpdateProductDto updateDto) throws Exception {
        Product existingRecord = productRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No product found with provided id"));
        Product updatedRecord = productRepository.save(existingRecord);
        return modelMapper.map(updatedRecord, ProductResponseDto.class);
    }

    @Override
    public void delete(Integer id) throws Exception {
        findById(id);
        productRepository.deleteById(id);
    }

    public List<ProductImageResponseDto> uploadImage(int productId, List<File> files) throws Exception {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NoResourceFoundException("No product found with provided id"));
        List<ProductImageResponseDto> productImages = new ArrayList<>();
        files.forEach(image -> {
            try {

                ProductImage productImage = new ProductImage();
                productImage.setTitle(image.getName());
                productImage.setPath(image.getPath());
                productImage.setProduct(product);

                ProductImage savedImage = productImageRepository.save(productImage);
                productImages.add(modelMapper.map(savedImage, ProductImageResponseDto.class));

            } catch (Exception e) {
                System.out.println("Failed to upload image: " + e.getMessage());
            }
        });
        return productImages;
    }
}
