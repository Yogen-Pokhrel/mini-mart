package com.minimart.productattribute.dto.response;

import com.minimart.common.RecordType;
import lombok.Data;

@Data
public class ProductAttributeResponseDto {
    private int id;
    private String name;
    private RecordType type;
    private String unit;
}
