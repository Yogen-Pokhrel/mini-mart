package com.minimart.order;

import com.minimart.cart.entity.Cart;
import com.minimart.cart.entity.CartItem;
import com.minimart.cart.repository.CartRepository;
import com.minimart.common.dto.PaginationDto;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.configuration.Constants;
import com.minimart.order.dto.response.OrderResponseDto;
import com.minimart.order.entity.Order;
import com.minimart.order.entity.OrderLineItem;
import com.minimart.order.entity.OrderStatus;
import com.minimart.product.repository.ProductRepository;
import com.minimart.user.dto.response.UserDetailDto;
import com.minimart.user.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    public OrderResponseDto addOrder(int customerId) throws Exception {
        Cart cart = cartRepository.findByUserId(customerId).orElseThrow(() -> new NoResourceFoundException("No Cart found for the user"));
        if(cart.getTotalPrice() == 0 || cart.getItems().isEmpty()){
            throw new Exception("Cart is empty");
        }

        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(cart.getUser());
        order.setShippingAmount(10);

        for(CartItem cartItem : cart.getItems()){
            OrderLineItem newOrderLineItem = new OrderLineItem();
            newOrderLineItem.setOrder(order);
            newOrderLineItem.setProduct(cartItem.getProduct());
            newOrderLineItem.setQuantity(cartItem.getQuantity());
            float taxAmount = (Constants.TAX / 100) * cartItem.getPrice();
            newOrderLineItem.setTaxAmount(taxAmount);
            newOrderLineItem.setUnitPrice(cartItem.getProduct().getPrice());
            newOrderLineItem.setTotalPrice((newOrderLineItem.getUnitPrice() * newOrderLineItem.getQuantity()) + newOrderLineItem.getTaxAmount());
            order.getOrderLineItems().add(newOrderLineItem);
            order.setTaxAmount(order.getTaxAmount() + newOrderLineItem.getTaxAmount());
            order.setTotalAmount(order.getTotalAmount() + newOrderLineItem.getTotalPrice());
        }

        order = orderRepository.save(order);
        return modelMapper.map(order, OrderResponseDto.class);
    }


    @SuppressWarnings("unchecked")
    public Page<OrderResponseDto> findAll(PaginationDto paginationDto) {
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize());
        Page<Order> paginatedUser = orderRepository.findAll(pageable);
        return paginatedUser.map(user -> modelMapper.map(user, OrderResponseDto.class));
    }

}
