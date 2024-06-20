package com.minimart.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;

@Data
public class PaginationDto {

    @Min(value = 0, message = "Page number must be at least 1")
    private int page = 0;

    @Positive(message = "Size must be a positive number")
    private int size = 25;
}
