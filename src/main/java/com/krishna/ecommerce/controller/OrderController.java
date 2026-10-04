package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.AddressRequest;
import com.krishna.ecommerce.dto.OrderResponse;
import com.krishna.ecommerce.dto.OrderStatusRequest;
import com.krishna.ecommerce.security.SecurityUtils;
import com.krishna.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse placeOrder(
            @Valid @RequestBody AddressRequest request) {

        return orderService.placeOrder(
                SecurityUtils.getCurrentUser().getId(),
                request
        );
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(
            @PathVariable Long orderId) {

        return orderService.getOrderById(
                orderId,
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @GetMapping("/my-orders")
    public List<OrderResponse> getMyOrders() {

        return orderService.getOrdersByUserId(
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @PatchMapping("/{orderId}/status")
    public OrderResponse updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusRequest request) {

        return orderService.updateOrderStatus(orderId, request);
    }
}