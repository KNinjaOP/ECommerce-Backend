package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.CartItemRequest;
import com.krishna.ecommerce.dto.CartResponse;
import com.krishna.ecommerce.security.SecurityUtils;
import com.krishna.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Cart",
        description = "APIs for managing the authenticated user's shopping cart"
)
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(
            summary = "Add item to cart",
            description = "Adds a product to the currently authenticated user's cart."
    )
    @PostMapping("/items")
    public CartResponse addItemToCart(
            @Valid @RequestBody CartItemRequest request) {

        return cartService.addItemToCart(
                SecurityUtils.getCurrentUser().getId(),
                request
        );
    }

    @Operation(
            summary = "Get my cart",
            description = "Returns the shopping cart of the currently authenticated user."
    )
    @GetMapping
    public CartResponse getMyCart() {

        return cartService.getCartByUserId(
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @Operation(
            summary = "Update cart item",
            description = "Updates the quantity of an item in the currently authenticated user's cart."
    )
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

    @Operation(
            summary = "Remove cart item",
            description = "Removes an item from the currently authenticated user's cart."
    )
    @DeleteMapping("/items/{itemId}")
    public CartResponse removeCartItem(
            @PathVariable Long itemId) {

        return cartService.removeCartItem(
                SecurityUtils.getCurrentUser().getId(),
                itemId
        );
    }
}