package com.minimart.product;

import com.minimart.common.ApiResponse;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.helpers.FileUploaderService;
import com.minimart.helpers.Utilities;
import com.minimart.product.dto.request.CreateProductDto;
import com.minimart.product.dto.request.UpdateProductDto;
import com.minimart.product.dto.request.UploadProductImagesDto;
import com.minimart.product.dto.response.ProductImageResponseDto;
import com.minimart.product.dto.response.ProductResponseDto;
import com.minimart.product.entity.ProductImage;
import com.minimart.user.dto.request.CreateUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ApiResponse<List<ProductResponseDto>> findAll(){
        List<ProductResponseDto> product  = productService.findAll();
        return ApiResponse.success(product , "Products fetched successfully");
    }

    @GetMapping("/{id}")
    private ApiResponse<ProductResponseDto> findById(@PathVariable int id) throws Exception{
        ProductResponseDto product  = productService.findById(id);
        return ApiResponse.success(product , "Product fetched successfully");
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
}
