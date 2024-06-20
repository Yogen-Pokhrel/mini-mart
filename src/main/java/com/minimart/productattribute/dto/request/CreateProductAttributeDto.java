package com.minimart.productattribute.dto.request;

import com.minimart.common.RecordType;
import com.minimart.helpers.validators.ValidEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProductAttributeDto {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @ValidEnum(enumClass = RecordType.class, message = "Please choose a valid data type")
    private String dataType;
    private RecordType type;

    private String unit;

}
