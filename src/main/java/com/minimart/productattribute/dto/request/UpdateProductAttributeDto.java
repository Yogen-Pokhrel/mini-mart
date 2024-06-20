package com.minimart.productattribute.dto.request;

import com.minimart.common.RecordType;
import com.minimart.helpers.validators.ValidEnum;
import lombok.Data;

@Data
public class UpdateProductAttributeDto {
    private String name;

    @ValidEnum(enumClass = RecordType.class, message = "Please choose a valid data type")
    private String dataType;
    private RecordType type;

    private String unit;
}
