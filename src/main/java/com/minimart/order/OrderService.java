package com.minimart.order;

import com.minimart.cart.entity.Cart;
import com.minimart.cart.entity.CartItem;
import com.minimart.cart.repository.CartItemRepository;
import com.minimart.cart.repository.CartRepository;
import com.minimart.common.dto.PaginationDto;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.configuration.Constants;
import com.minimart.order.dto.response.OrderLineItemResponseDto;
import com.minimart.order.dto.response.OrderResponseDto;
import com.minimart.order.entity.Order;
import com.minimart.order.entity.OrderLineItem;
import com.minimart.order.entity.OrderLineStatus;
import com.minimart.order.entity.OrderStatus;
import com.minimart.order.repository.OrderLineItemRepository;
import com.minimart.order.repository.OrderRepository;
import com.minimart.product.dto.response.ProductResponseDto;
import com.minimart.product.entity.Product;
import com.minimart.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    OrderLineItemRepository orderLineItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    public OrderResponseDto addOrder(int customerId) throws Exception {
        Cart cart = cartRepository.findByUserId(customerId).orElseThrow(() -> new NoResourceFoundException("No Cart found for the user"));
        if(cart.getTotalPrice() == 0 || cart.getItems().isEmpty()){
            throw new Exception("Cart is empty");
        }

        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(cart.getUser());
        order.setShippingAmount(10);
        order.setDiscountAmount(0);

        for(CartItem cartItem : cart.getItems()){
            OrderLineItem newOrderLineItem = new OrderLineItem();
            newOrderLineItem.setOrder(order);
            newOrderLineItem.setStatus(OrderLineStatus.PENDING);
            newOrderLineItem.setProduct(cartItem.getProduct());
            newOrderLineItem.setQuantity(cartItem.getQuantity());
            float taxAmount = (Constants.TAX / 100) * cartItem.getPrice();
            newOrderLineItem.setTaxAmount(taxAmount);
            newOrderLineItem.setUnitPrice(cartItem.getProduct().getPrice());
            newOrderLineItem.setTotalPrice((newOrderLineItem.getUnitPrice() * newOrderLineItem.getQuantity()) + newOrderLineItem.getTaxAmount());
            order.getOrderLineItems().add(newOrderLineItem);
            order.setTaxAmount(order.getTaxAmount() + newOrderLineItem.getTaxAmount());
            order.setAmount(order.getAmount() + (newOrderLineItem.getUnitPrice() * newOrderLineItem.getQuantity()));
            order.setTotalAmount(order.getTotalAmount() + newOrderLineItem.getTotalPrice());
        }

        order = orderRepository.save(order);

        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cart.setTotalPrice(0);

        cartRepository.save(cart);

        return modelMapper.map(order, OrderResponseDto.class);
    }


    @SuppressWarnings("unchecked")
    public Page<OrderResponseDto> findAll(PaginationDto paginationDto, int userId) {
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize());
        Page<Order> paginatedOrders;
        paginatedOrders = orderRepository.findByUserId(userId, pageable);
        return paginatedOrders.map(order -> modelMapper.map(order, OrderResponseDto.class));
    }

    @SuppressWarnings("unchecked")
    public Page<OrderLineItemResponseDto> findSellerOrders(PaginationDto paginationDto, int sellerId) {
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize());
        Page<OrderLineItem> paginatedProducts;
        paginatedProducts = orderLineItemRepository.findOrderedProducts(sellerId, pageable);
        return paginatedProducts.map(order -> modelMapper.map(order, OrderLineItemResponseDto.class));
    }

    OrderResponseDto changeStatus(int id, OrderStatus status) throws Exception {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No Order found for id " + id));
        order.setStatus(status);
        order = orderRepository.save(order);
        return modelMapper.map(order, OrderResponseDto.class);
    }

    OrderLineItemResponseDto changeLineStatus(int id, OrderLineStatus status) throws Exception {
        OrderLineItem orderLineItem = orderLineItemRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No Order line found for id " + id));
        orderLineItem.setStatus(status);
        orderLineItem = orderLineItemRepository.save(orderLineItem);
        return modelMapper.map(orderLineItem, OrderLineItemResponseDto.class);
    }

    OrderResponseDto cancelOrder(int id) throws Exception {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No Order found for id " + id));
        if(order.getStatus() != OrderStatus.PROCESSING){
            throw new Exception("This order cannot be cancelled as it is already shipped.");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);
        return modelMapper.map(order, OrderResponseDto.class);
    }

    OrderResponseDto returnOrder(int id) throws Exception {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No Order found for id " + id));
        if(order.getStatus() == OrderStatus.CANCELLED){
            throw new Exception("This order cannot be returned as it is already cancelled.");
        }
        if(order.getStatus() == OrderStatus.RETURNED){
            throw new Exception("This order cannot be returned as it is already returned.");
        }
        if(order.getStatus() != OrderStatus.DELIVERED){
            throw new Exception("This order cannot be returned as it is not yet delivered.");
        }
        order.setStatus(OrderStatus.RETURN_REQUEST);
        order = orderRepository.save(order);
        return modelMapper.map(order, OrderResponseDto.class);
    }
}
