package com.minimart.brand.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateBrandDto {

    @NotBlank(message = "Brand name is mandatory")
    @Size(min = 1, max = 50, message = "Brand name must be between 1 and 50 characters")
    private String name;
}
