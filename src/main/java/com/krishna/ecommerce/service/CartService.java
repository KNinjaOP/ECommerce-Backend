package com.krishna.ecommerce.service;

import com.krishna.ecommerce.dto.CartItemRequest;
import com.krishna.ecommerce.dto.CartItemResponse;
import com.krishna.ecommerce.dto.CartResponse;
import com.krishna.ecommerce.exception.ResourceNotFoundException;
import com.krishna.ecommerce.model.Cart;
import com.krishna.ecommerce.model.CartItem;
import com.krishna.ecommerce.model.Product;
import com.krishna.ecommerce.model.User;
import com.krishna.ecommerce.repository.CartItemRepository;
import com.krishna.ecommerce.repository.CartRepository;
import com.krishna.ecommerce.repository.ProductRepository;
import com.krishna.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public CartResponse addItemToCart(Long userId, CartItemRequest request) {

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        ));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + request.getProductId()
                        ));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElseGet(() -> {

                    CartItem newItem = new CartItem();

                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);

                    return cartItemRepository.save(newItem);
                });

        cartItem.setQuantity(
                cartItem.getQuantity() + request.getQuantity()
        );

        cartItemRepository.save(cartItem);

        return toCartResponse(cart);
    }

    public CartResponse getCartByUserId(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found for user id: " + userId
                        )
                );

        return toCartResponse(cart);
    }

    private CartResponse toCartResponse(Cart cart) {

        List<CartItem> items =
            cartItemRepository.findByCartId(cart.getId());

        List<CartItemResponse> itemResponses = items.stream()
                .map(item -> {

                    BigDecimal subTotal =
                            item.getProduct()
                                    .getPrice()
                                    .multiply(
                                            BigDecimal.valueOf(
                                                    item.getQuantity()
                                            )
                                    );

                    CartItemResponse response = new CartItemResponse();

                    response.setId(item.getId());
                    response.setProductId(item.getProduct().getId());
                    response.setProductName(item.getProduct().getName());
                    response.setProductPrice(item.getProduct().getPrice());
                    response.setQuantity(item.getQuantity());
                    response.setSubtotal(subTotal);

                    return response;
                })
                .toList();

        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartResponse response = new CartResponse();

        response.setId(cart.getId());
        response.setUserId(cart.getUser().getId());
        response.setItems(itemResponses);
        response.setTotalAmount(totalAmount);

        return response;
    }

    public CartResponse updateCartItem(
            Long userId,
            Long itemId,
            Integer quantity) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found for user id: " + userId
                        )
                );

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found with id: " + itemId
                        )
                );

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new ResourceNotFoundException(
                    "Cart item does not belong to this user's cart"
            );
        }

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0"
            );
        }

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);

        return toCartResponse(cart);
    }

    public CartResponse removeCartItem(Long userId, Long itemId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found with user id: " + userId
                        ));

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found with id: " + itemId
                        ));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new ResourceNotFoundException(
                    "Cart item doesn't belong to this user's cart"
            );
        }

        cartItemRepository.delete(cartItem);

        return toCartResponse(cart);
    }
}