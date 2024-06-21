package com.minimart.cart;

import com.minimart.auth.AuthDetails;
import com.minimart.cart.dto.request.AddCartItemDto;
import com.minimart.cart.dto.response.CartResponseDto;
import com.minimart.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @GetMapping
    public ApiResponse<CartResponseDto> getUserCart(@AuthenticationPrincipal AuthDetails authDetails) throws Exception{
        CartResponseDto cartData = cartService.getUserCart(authDetails.getId());
        return ApiResponse.success(cartData, "Cart fetched successfully");
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @PostMapping
    public ApiResponse<CartResponseDto> addItemToCart(@AuthenticationPrincipal AuthDetails authDetails, @RequestBody AddCartItemDto cartItem) throws Exception{
        CartResponseDto updatedCart = cartService.addItemToCart(authDetails.getId(), cartItem.getProductId(), cartItem.getQuantity());
        return ApiResponse.success(updatedCart, "Cart fetched successfully");
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @DeleteMapping("/item/{cartItemId}")
    public ApiResponse<CartResponseDto> removeItemFromCart(@AuthenticationPrincipal AuthDetails authDetails, @PathVariable int cartItemId) throws Exception{
        CartResponseDto updatedCart = cartService.removeItemFromCart(authDetails.getId(), cartItemId);
        return ApiResponse.success(updatedCart, "Item removed from cart successfully");
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @DeleteMapping("/items")
    public ApiResponse<CartResponseDto> clearCart(@AuthenticationPrincipal AuthDetails authDetails) throws Exception{
        CartResponseDto updatedCart = cartService.clearCart(authDetails.getId());
        return ApiResponse.success(updatedCart, "Cart cleared successfully");
    }
}
