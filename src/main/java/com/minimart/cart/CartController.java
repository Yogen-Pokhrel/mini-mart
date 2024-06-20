package com.minimart.cart;

import com.minimart.cart.dto.request.AddCartItemDto;
import com.minimart.cart.dto.response.CartResponseDto;
import com.minimart.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ApiResponse<CartResponseDto> getUserCart(@PathVariable int userId) throws Exception{
        CartResponseDto cartData = cartService.getUserCart(userId);
        return ApiResponse.success(cartData, "Cart fetched successfully");
    }

    @PostMapping("/{userId}")
    public ApiResponse<CartResponseDto> addItemToCart(@PathVariable int userId, @RequestBody AddCartItemDto cartItem) throws Exception{
        CartResponseDto updatedCart = cartService.addItemToCart(userId, cartItem.getProductId(), cartItem.getQuantity());
        return ApiResponse.success(updatedCart, "Cart fetched successfully");
    }

    @DeleteMapping("/{userId}/item/{cartItemId}")
    public ApiResponse<CartResponseDto> removeItemFromCart(@PathVariable int userId, @PathVariable int cartItemId) throws Exception{
        CartResponseDto updatedCart = cartService.removeItemFromCart(userId, cartItemId);
        return ApiResponse.success(updatedCart, "Item removed from cart successfully");
    }

    @DeleteMapping("/{userId}/items")
    public ApiResponse<CartResponseDto> clearCart(@PathVariable int userId) throws Exception{
        CartResponseDto updatedCart = cartService.clearCart(userId);
        return ApiResponse.success(updatedCart, "Cart cleared successfully");
    }
}
