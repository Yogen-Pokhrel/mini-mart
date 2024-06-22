package com.minimart.order.dto.request;

import com.minimart.helpers.validators.ValidEnum;
import com.minimart.order.entity.OrderLineStatus;
import lombok.Data;

@Data
public class ChangeOrderLineStatusDto {

    @ValidEnum(enumClass = OrderLineStatus.class, message = "Please provide a valid status")
    private String status;
}
