package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.AddressRequest;
import com.krishna.ecommerce.dto.OrderResponse;
import com.krishna.ecommerce.dto.OrderStatusRequest;
import com.krishna.ecommerce.security.SecurityUtils;
import com.krishna.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Orders",
        description = "APIs for placing and managing customer orders"
)
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Place an order",
            description = "Creates a new order using the currently authenticated user's cart and shipping address."
    )
    @PostMapping
    public OrderResponse placeOrder(
            @Valid @RequestBody AddressRequest request) {

        return orderService.placeOrder(
                SecurityUtils.getCurrentUser().getId(),
                request
        );
    }

    @Operation(
            summary = "Get order by ID",
            description = "Returns a specific order belonging to the currently authenticated user."
    )
    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(
            @PathVariable Long orderId) {

        return orderService.getOrderById(
                orderId,
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @Operation(
            summary = "Get my orders",
            description = "Returns all orders placed by the currently authenticated user."
    )
    @GetMapping("/my-orders")
    public List<OrderResponse> getMyOrders() {

        return orderService.getOrdersByUserId(
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @Operation(
            summary = "Update order status",
            description = "Updates an order's status. This endpoint is restricted to administrators."
    )
    @PatchMapping("/{orderId}/status")
    public OrderResponse updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusRequest request) {

        return orderService.updateOrderStatus(orderId, request);
    }
}