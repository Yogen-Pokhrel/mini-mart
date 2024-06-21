package com.minimart.cart;

import com.minimart.cart.dto.response.CartResponseDto;
import com.minimart.cart.entity.Cart;
import com.minimart.cart.entity.CartItem;
import com.minimart.cart.repository.CartItemRepository;
import com.minimart.cart.repository.CartRepository;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.product.entity.Product;
import com.minimart.product.repository.ProductRepository;
import com.minimart.user.entity.User;
import com.minimart.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Cart assignCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(0);
        cart = cartRepository.save(cart);
        return cart;
    }

    @Transactional
    public CartResponseDto addItemToCart(int userId, int productId, int quantity) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoResourceFoundException("No user found"));
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if(cart == null) {
            cart = assignCart(user);
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new NoResourceFoundException("Cannot find product with id " + productId));
        if (product.getStock() < quantity) {
            throw new Exception("Not enough stock for product with id " + productId);
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(product.getPrice() * quantity);
        cart.getItems().add(cartItem);

        cart.setTotalPrice(cart.getTotalPrice() + cartItem.getPrice());
        cart = cartRepository.save(cart);
        return modelMapper.map(cart, CartResponseDto.class);
    }

    @Transactional
    public CartResponseDto removeItemFromCart(int userId, int cartItemId) throws Exception {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new NoResourceFoundException("No cart found for the user"));
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new NoResourceFoundException("Cannot find cartItem with id " + cartItemId));

        cart.getItems().remove(cartItem);
        cart.setTotalPrice(cart.getTotalPrice() - cartItem.getPrice());
        cartItemRepository.delete(cartItem);

        cart = cartRepository.save(cart);
        return modelMapper.map(cart, CartResponseDto.class);
    }

    CartResponseDto getUserCart(int userId) throws Exception{
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if(cart == null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new NoResourceFoundException("No user found"));
            cart = assignCart(user);
        }
        return modelMapper.map(cart, CartResponseDto.class);
    }

    @Transactional
    public CartResponseDto clearCart(int userId) throws Exception {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if(cart == null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new NoResourceFoundException("No user found"));
            cart = assignCart(user);
        }
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cart.setTotalPrice(0);

        cart = cartRepository.save(cart);
        return modelMapper.map(cart, CartResponseDto.class);
    }
}
