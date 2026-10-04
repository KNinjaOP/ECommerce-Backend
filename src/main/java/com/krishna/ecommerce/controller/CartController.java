package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.CartItemRequest;
import com.krishna.ecommerce.dto.CartResponse;
import com.krishna.ecommerce.security.SecurityUtils;
import com.krishna.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public CartResponse addItemToCart(
            @Valid @RequestBody CartItemRequest request) {

        return cartService.addItemToCart(
                SecurityUtils.getCurrentUser().getId(),
                request
        );
    }

    @GetMapping
    public CartResponse getMyCart() {

        return cartService.getCartByUserId(
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @PutMapping("/items/{itemId}")
    public CartResponse updateCartItem(
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {

        return cartService.updateCartItem(
                SecurityUtils.getCurrentUser().getId(),
                itemId,
                quantity
        );
    }

    @DeleteMapping("/items/{itemId}")
    public CartResponse removeCartItem(
            @PathVariable Long itemId) {

        return cartService.removeCartItem(
                SecurityUtils.getCurrentUser().getId(),
                itemId
        );
    }
}