package com.minimart.product.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadProductImagesDto {
    private List<MultipartFile> images;
}
