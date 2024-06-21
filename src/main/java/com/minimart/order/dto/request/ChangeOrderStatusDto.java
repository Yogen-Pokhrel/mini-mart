package com.minimart.order.dto.request;

import com.minimart.helpers.validators.ValidEnum;
import com.minimart.order.entity.OrderStatus;
import com.minimart.product.entity.ProductStatus;
import lombok.Data;

@Data
public class ChangeOrderStatusDto {

    @ValidEnum(enumClass = OrderStatus.class, message = "Please provide a valid status")
    private String status;
}
