package com.minimart.product;

import com.minimart.auth.AuthDetails;
import com.minimart.common.ApiResponse;
import com.minimart.common.ResponseMeta;
import com.minimart.common.dto.PaginationDto;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.helpers.FileUploaderService;
import com.minimart.helpers.Utilities;
import com.minimart.product.dto.request.*;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {
    @Autowired
    ProductService productService;

    private final FileUploaderService fileUploaderService;
    private final String uploadPath = "public/minimart/products/";


    @GetMapping
    private ApiResponse<List<ProductResponseDto>> findAll(PaginationDto paginationDto, ProductFilterDto productFilterDto){

        Page<ProductResponseDto> userPaginated = productService.findAll(paginationDto, productFilterDto);
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

//    @PreAuthorize("hasAnyAuthority('SELLER')")
    @GetMapping("/mine")
    private ApiResponse<List<ProductResponseDto>> findSellerProduct(PaginationDto paginationDto, ProductFilterDto productFilterDto, @AuthenticationPrincipal AuthDetails authDetails){
        productFilterDto.setSellerId(authDetails.getId());
        Page<ProductResponseDto> userPaginated = productService.findAll(paginationDto, productFilterDto);
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

//    @PreAuthorize("hasAnyAuthority('SELLER')")
    @PostMapping
    private ApiResponse<ProductResponseDto> create(@AuthenticationPrincipal AuthDetails authDetails, @Valid @RequestBody CreateProductDto createDto) throws Exception{
        createDto.setSlug(Utilities.slugify(createDto.getSlug(), "-"));
        createDto.setSeller_id(authDetails.getId());
        ProductResponseDto newCategory = productService.save(createDto);
        return ApiResponse.success(newCategory, "Product created successfully");
    }

//    @PreAuthorize("hasAnyAuthority('SELLER')")
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
        return ApiResponse.success(uploadedProductImages, "Images uploaded successfully");
    }

//    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @PostMapping("/reviews")
    private ApiResponse<ReviewResponseDto> addReviews(@Valid @RequestBody CreateProductReviewDto createDto) throws Exception{
        ReviewResponseDto newRecord = productService.addReview(createDto);
        return ApiResponse.success(newRecord, "Review added successfully");
    }

//    @PreAuthorize("hasAnyAuthority('SELLER')")
    @PutMapping("/{id}")
    private ApiResponse<ProductResponseDto> update(@PathVariable int id, @RequestBody UpdateProductDto updateDto) throws Exception{
        ProductResponseDto newCategory = productService.update(id, updateDto);
        return ApiResponse.success(newCategory, "Product updated successfully");
    }

//    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('SELLER')")
    @DeleteMapping("/{id}")
    private ApiResponse<?> delete(@PathVariable int id) throws Exception{
        productService.delete(id);
        return ApiResponse.success(null, "Product deleted successfully");
    }

//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}/review")
    private ApiResponse<?> deleteReview(@PathVariable int id) throws Exception{
        productService.deleteReview(id);
        return ApiResponse.success(null, "Product review deleted successfully");
    }
}
