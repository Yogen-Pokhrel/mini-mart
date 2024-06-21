package com.minimart.order;

import com.minimart.common.ApiResponse;
import com.minimart.common.ResponseMeta;
import com.minimart.common.dto.PaginationDto;
import com.minimart.order.dto.response.OrderResponseDto;
import com.minimart.order.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ApiResponse<List<OrderResponseDto>> getAllOrders(PaginationDto paginationDto, @RequestParam(required = false)Optional<Integer> userId) {
        Page<OrderResponseDto> orders = orderService.findAll(paginationDto, userId);
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

    @PatchMapping("/{id}")
    public ApiResponse<OrderResponseDto>  changeStatus(@RequestParam OrderStatus orderStatus, @PathVariable int id) throws Exception {
        OrderResponseDto updatedOrder = orderService.changeStatus(id, orderStatus);
        return ApiResponse.success(updatedOrder, "Order status changed successfully");
    }

    @PatchMapping("/{id}/cancel")
    public ApiResponse<OrderResponseDto>  cancelOrder(@PathVariable int id) throws Exception {
        OrderResponseDto updatedOrder = orderService.cancelOrder(id);
        return ApiResponse.success(updatedOrder, "Order cancelled successfully");
    }


    @PatchMapping("/{id}/return")
    public ApiResponse<OrderResponseDto>  returnOrder(@PathVariable int id) throws Exception {
        OrderResponseDto updatedOrder = orderService.returnOrder(id);
        return ApiResponse.success(updatedOrder, "Order requested for return successfully");
    }


}
