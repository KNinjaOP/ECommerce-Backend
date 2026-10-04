package com.krishna.ecommerce.service;

import com.krishna.ecommerce.dto.*;
import com.krishna.ecommerce.exception.InsufficientStockException;
import com.krishna.ecommerce.exception.InvalidOrderStatusException;
import com.krishna.ecommerce.exception.ResourceNotFoundException;
import com.krishna.ecommerce.model.*;
import com.krishna.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponse placeOrder(
            Long userId,
            AddressRequest addressRequest
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        ));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found for user with id: " + userId
                        ));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Cannot place order because cart is empty"
            );
        }

        AddressSnapshot addressSnapshot = new AddressSnapshot();

        addressSnapshot.setCity(addressRequest.getCity());
        addressSnapshot.setCountry(addressRequest.getCountry());
        addressSnapshot.setPostalCode(addressRequest.getPostalCode());
        addressSnapshot.setState(addressRequest.getState());
        addressSnapshot.setStreet(addressRequest.getStreet());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            if (cartItem.getQuantity() > product.getStock()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.getName()
                );
            }

            BigDecimal price = product.getPrice();

            BigDecimal subtotal = price.multiply(
                    BigDecimal.valueOf(cartItem.getQuantity())
            );

            totalAmount = totalAmount.add(subtotal);
        }

        Order order = new Order();

        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);
        order.setShippingAddress(addressSnapshot);
        order.setTotalAmount(totalAmount);

        orderRepository.save(order);

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItemRepository.save(orderItem);

            product.setStock(
                    product.getStock() - cartItem.getQuantity()
            );

            productRepository.save(product);
        }

        cartItemRepository.deleteAll(cartItems);

        return toOrderResponse(order);
    }

    public OrderResponse getOrderById(Long orderId, Long userId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId
                        ));

        if (!order.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Order not found with id: " + orderId
            );
        }

        return toOrderResponse(order);
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        ));

        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(this::toOrderResponse)
                .toList();
    }

    private OrderResponse toOrderResponse(Order order) {

        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(order.getId());

        List<OrderItemResponse> itemResponses =
                orderItems.stream()
                        .map(item -> {

                            BigDecimal subtotal =
                                    item.getPrice().multiply(
                                            BigDecimal.valueOf(
                                                    item.getQuantity()
                                            )
                                    );

                            OrderItemResponse response =
                                    new OrderItemResponse();

                            response.setId(item.getId());
                            response.setProductId(
                                    item.getProduct().getId()
                            );
                            response.setProductName(
                                    item.getProduct().getName()
                            );
                            response.setQuantity(
                                    item.getQuantity()
                            );
                            response.setPrice(
                                    item.getPrice()
                            );
                            response.setSubtotal(subtotal);

                            return response;
                        })
                        .toList();

        AddressSnapshot address =
                order.getShippingAddress();

        AddressSnapshotResponse addressResponse =
                new AddressSnapshotResponse(
                        address.getStreet(),
                        address.getCity(),
                        address.getState(),
                        address.getPostalCode(),
                        address.getCountry()
                );

        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setUserId(order.getUser().getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setShippingAddress(addressResponse);
        response.setItems(itemResponses);

        return response;
    }

    public OrderResponse updateOrderStatus(
            Long orderId,
            OrderStatusRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId
                        ));

        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();

        if (!isValidTransition(currentStatus, newStatus)) {
            throw new InvalidOrderStatusException(
                    "Cannot change order status from "
                        + currentStatus
                        + " to "
                        + newStatus
            );
        }

        order.setStatus(request.getStatus());

        orderRepository.save(order);

        return toOrderResponse(order);
    }

    private boolean isValidTransition(OrderStatus currentStatus, OrderStatus newStatus) {

        if (currentStatus == newStatus)
            return true;

        return switch (currentStatus) {
            case PLACED ->
                newStatus == OrderStatus.CONFIRMED
                        || newStatus == OrderStatus.CANCELLED;

            case CONFIRMED ->
                    newStatus == OrderStatus.SHIPPED
                            || newStatus == OrderStatus.CANCELLED;

            case SHIPPED ->
                    newStatus == OrderStatus.DELIVERED;

            case DELIVERED , CANCELLED ->
                false;
        };
    }

}
