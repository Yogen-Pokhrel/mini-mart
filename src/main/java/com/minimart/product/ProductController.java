package com.minimart.product;

import com.minimart.common.ApiResponse;
import com.minimart.common.ResponseMeta;
import com.minimart.common.dto.PaginationDto;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.helpers.FileUploaderService;
import com.minimart.helpers.Utilities;
import com.minimart.product.dto.request.CreateProductDto;
import com.minimart.product.dto.request.CreateProductReviewDto;
import com.minimart.product.dto.request.UpdateProductDto;
import com.minimart.product.dto.request.UploadProductImagesDto;
import com.minimart.product.dto.response.ProductDetailResponseDto;
import com.minimart.product.dto.response.ProductImageResponseDto;
import com.minimart.product.dto.response.ProductResponseDto;
import com.minimart.product.dto.response.ReviewResponseDto;
import com.minimart.product.entity.ProductImage;
import com.minimart.user.dto.request.CreateUserDto;
import com.minimart.user.dto.response.UserDetailDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    ProductService productService;

    private final FileUploaderService fileUploaderService;
    private final String uploadPath = "public/minimart/products/";


    @GetMapping
    private ApiResponse<List<ProductResponseDto>> findAll(PaginationDto paginationDto){
        Page<ProductResponseDto> userPaginated = productService.findAll(paginationDto);
        return ApiResponse.success(
                userPaginated.getContent(),
                "Products fetched successfully",
                new ResponseMeta(
                        userPaginated.getNumber(),
                        userPaginated.getSize(),
                        userPaginated.getTotalElements(),
                        userPaginated.getTotalPages())
        );
    }

    @GetMapping("/{id}")
    private ApiResponse<ProductResponseDto> findById(@PathVariable int id) throws Exception{
        ProductResponseDto product  = productService.findById(id);
        return ApiResponse.success(product , "Product fetched successfully");
    }

    @GetMapping("/find-by-slug/{slug}")
    private ApiResponse<ProductDetailResponseDto> findBySlug(@PathVariable String slug) throws Exception{
        ProductDetailResponseDto product  = productService.findBySlug(slug);
        return ApiResponse.success(product , "Product fetched successfully");
    }

    @GetMapping("/{id}/reviews")
    private ApiResponse<List<ReviewResponseDto>> findProductReviews(@PathVariable int id) throws Exception{
        List<ReviewResponseDto> reviews  = productService.findAllProductReviews(id);
        return ApiResponse.success(reviews , "Product fetched successfully");
    }

    @PostMapping
    private ApiResponse<ProductResponseDto> create(@Valid @RequestBody CreateProductDto createDto) throws Exception{
        createDto.setSlug(Utilities.slugify(createDto.getSlug(), "-"));
        ProductResponseDto newCategory = productService.save(createDto);
        return ApiResponse.success(newCategory, "Category created successfully");
    }

    @PostMapping(value = "/{id}/upload-images", consumes = "multipart/form-data", produces = {"application/json"} )
    private ApiResponse<List<ProductImageResponseDto>> uploadImages(@PathVariable int id, @Valid @ModelAttribute UploadProductImagesDto uploadProductImagesDto) throws Exception{

        if(uploadProductImagesDto.getImages().isEmpty()){
            throw new NoResourceFoundException("No image found for product to upload");
        }

        List<File> uploadedFiles= new ArrayList<>();
        uploadProductImagesDto.getImages().forEach(image->{
            try{
                File uploadedFile = fileUploaderService.upload(image, uploadPath);
                uploadedFiles.add(uploadedFile);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        });
        List<ProductImageResponseDto> uploadedProductImages = productService.uploadImage(id, uploadedFiles);
        return ApiResponse.success(uploadedProductImages, "Category created successfully");
    }

    @PostMapping("/reviews")
    private ApiResponse<ReviewResponseDto> addReviews(@Valid @RequestBody CreateProductReviewDto createDto) throws Exception{
        ReviewResponseDto newRecord = productService.addReview(createDto);
        return ApiResponse.success(newRecord, "Review added successfully");
    }

    @PutMapping("/{id}")
    private ApiResponse<ProductResponseDto> update(@PathVariable int id,@Valid @RequestBody UpdateProductDto updateDto) throws Exception{
        ProductResponseDto newCategory = productService.update(id, updateDto);
        return ApiResponse.success(newCategory, "Product updated successfully");
    }

    @DeleteMapping("/{id}")
    private ApiResponse<?> delete(@PathVariable int id) throws Exception{
        productService.delete(id);
        return ApiResponse.success(null, "Product deleted successfully");
    }

    @DeleteMapping("/{id}/review")
    private ApiResponse<?> deleteReview(@PathVariable int id) throws Exception{
        productService.deleteReview(id);
        return ApiResponse.success(null, "Product review deleted successfully");
    }
}
