package com.minimart.order;

import com.minimart.common.ApiResponse;
import com.minimart.common.ResponseMeta;
import com.minimart.common.dto.PaginationDto;
import com.minimart.order.dto.response.OrderResponseDto;
import com.minimart.user.dto.response.UserDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{id}")
    public ApiResponse<OrderResponseDto> createOrder(@PathVariable int id) throws Exception {
        OrderResponseDto newOrder = orderService.addOrder(id);
        return ApiResponse.success(newOrder, "Order created successfully");
    }

    @GetMapping
    public ApiResponse<List<OrderResponseDto>> getAllOrders(PaginationDto paginationDto) {
        Page<OrderResponseDto> orders = orderService.findAll(paginationDto);
        return ApiResponse.success(
                orders.getContent(),
                "Orders fetched successfully",
                new ResponseMeta(
                        orders.getNumber(),
                        orders.getSize(),
                        orders.getTotalElements(),
                        orders.getTotalPages())
        );

    }


}
