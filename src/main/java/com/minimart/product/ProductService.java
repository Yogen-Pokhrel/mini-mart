package com.minimart.product;

import com.minimart.brand.BrandRepository;
import com.minimart.brand.entity.Brand;
import com.minimart.category.CategoryRepository;
import com.minimart.category.entity.ProductCategory;
import com.minimart.common.CommonService;
import com.minimart.common.dto.PaginationDto;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.helpers.ListMapper;
import com.minimart.product.dto.request.CreateProductDto;
import com.minimart.product.dto.request.CreateProductReviewDto;
import com.minimart.product.dto.request.ProductFilterDto;
import com.minimart.product.dto.request.UpdateProductDto;
import com.minimart.product.dto.response.ProductDetailResponseDto;
import com.minimart.product.dto.response.ProductImageResponseDto;
import com.minimart.product.dto.response.ProductResponseDto;
import com.minimart.product.dto.response.ReviewResponseDto;
import com.minimart.product.entity.Product;
import com.minimart.product.entity.ProductImage;
import com.minimart.product.entity.ProductReview;
import com.minimart.product.entity.ProductStatus;
import com.minimart.product.repository.ProductImageRepository;
import com.minimart.product.repository.ProductRepository;
import com.minimart.product.repository.ProductReviewRepository;
import com.minimart.product.repository.ProductSpecification;
import com.minimart.role.dto.RoleResponseDto;
import com.minimart.role.entity.Role;
import com.minimart.user.dto.response.UserDetailDto;
import com.minimart.user.entity.User;
import com.minimart.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private ProductReviewRepository productReviewRepository;

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

    @SuppressWarnings("unchecked")
    public Page<ProductResponseDto> findAll(PaginationDto paginationDto, ProductFilterDto productFilterDto) {
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize());
        System.out.println("productFilterDto" + productFilterDto);
        Specification<Product> spec = new ProductSpecification(productFilterDto);
        Page<Product> paginatedProducts = productRepository.findAll(spec, pageable);
        return paginatedProducts.map(product -> modelMapper.map(product, ProductResponseDto.class));
    }

    @SuppressWarnings("unchecked")
    public List<ReviewResponseDto> findAllProductReviews(int productId) {
        return (List<ReviewResponseDto>) listMapper.mapList(productReviewRepository.findAllByProductId(productId),new ReviewResponseDto());
    }

    @Override
    public ProductResponseDto findById(Integer id) throws Exception {
        Product recordData = productRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No product found with provided id"));
        return modelMapper.map(recordData, ProductResponseDto.class);
    }

    public ProductDetailResponseDto findBySlug(String slug) throws Exception {
        Product recordData = productRepository.findBySlug(slug).orElseThrow(() -> new NoResourceFoundException("No product found with provided id"));
        return modelMapper.map(recordData, ProductDetailResponseDto.class);
    }

    @Override
    public ProductResponseDto save(CreateProductDto createDto) throws Exception {
        ProductCategory category = categoryRepository.findById(createDto.getCategory_id()).orElseThrow(() -> new NoResourceFoundException("No Category found with provided id"));
        User user = userRepository.findById(createDto.getSeller_id()).orElseThrow(() -> new NoResourceFoundException("No user found with provided id"));
        Brand brand = brandRepository.findById(createDto.getBrand_id()).orElseThrow(() -> new NoResourceFoundException("No brand found with provided id"));
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
        if(updateDto != null){
            if(updateDto.getBrand_id() != 0){
                Brand brand = brandRepository.findById(updateDto.getBrand_id()).orElseThrow(() -> new NoResourceFoundException("No brand found with provided id"));
                existingRecord.setBrand(brand);
            }

            if(updateDto.getProductStatus() != null){
                ProductStatus status = ProductStatus.valueOf(updateDto.getProductStatus());
                existingRecord.setStatus(status);
            }

            if(updateDto.getCategory_id() != 0){
                ProductCategory category = categoryRepository.findById(updateDto.getCategory_id()).orElseThrow(() -> new NoResourceFoundException("No Category found with provided id"));
                existingRecord.setCategory(category);
            }

            if(updateDto.getSlug() != null){
                existingRecord.setSlug(updateDto.getSlug());
            }

            if(updateDto.getTitle() != null){
                existingRecord.setTitle(updateDto.getTitle());
            }

            if(updateDto.getDescription() != null){
                existingRecord.setDescription(updateDto.getDescription());
            }

            if(updateDto.getPrice() != 0){
                existingRecord.setPrice(updateDto.getPrice());
            }

            if(updateDto.getStock() != 0){
                existingRecord.setStock(updateDto.getStock());
            }
        }
        Product updatedRecord = productRepository.save(existingRecord);
        return modelMapper.map(updatedRecord, ProductResponseDto.class);
    }

    @Override
    public void delete(Integer id) throws Exception {
        Product product = productRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No product found with provided id"));
        if(!product.getOrderLineItems().isEmpty()){
            throw new Exception("You cannot delete a product that has been purchased already");
        }
        productRepository.deleteById(id);
    }

    public void deleteReview(Integer id) throws Exception {
        productReviewRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No product review found with provided id"));
        productReviewRepository.deleteById(id);
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

    public ReviewResponseDto addReview(CreateProductReviewDto createDto) throws Exception{
        Product product = productRepository.findById(createDto.getProductId()).orElseThrow(() -> new NoResourceFoundException("No product found with provided id"));
        User customer = userRepository.findById(createDto.getCustomerId()).orElseThrow(() -> new NoResourceFoundException("No customer found with provided id"));
        ProductReview review = new ProductReview();
        review.setComment(createDto.getComment());
        review.setProduct(product);
        review.setCustomer(customer);
        ProductReview newRecord = productReviewRepository.save(review);
        return modelMapper.map(newRecord, ReviewResponseDto.class);
    }
}
