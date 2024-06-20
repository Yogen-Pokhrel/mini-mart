package com.minimart.category.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AssignAttributesDto {
    private List<Integer> attributeIds;
}
