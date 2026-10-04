package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.PaymentRequest;
import com.krishna.ecommerce.dto.PaymentResponse;
import com.krishna.ecommerce.security.SecurityUtils;
import com.krishna.ecommerce.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Payments",
        description = "APIs for creating, viewing, and processing payments"
)
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(
            summary = "Create a payment",
            description = "Creates a pending payment for an order belonging to the currently authenticated user."
    )
    @PostMapping
    public PaymentResponse createPayment(
            @Valid @RequestBody PaymentRequest request) {

        return paymentService.createPayment(
                SecurityUtils.getCurrentUser().getId(),
                request
        );
    }

    @Operation(
            summary = "Process a payment",
            description = "Processes a pending payment and marks it as successful. This endpoint is restricted to administrators."
    )
    @PatchMapping("/{paymentId}/process")
    public PaymentResponse processPayment(
            @PathVariable Long paymentId) {

        return paymentService.processPayment(paymentId);
    }

    @Operation(
            summary = "Get payment by ID",
            description = "Returns a specific payment belonging to an order of the currently authenticated user."
    )
    @GetMapping("/{paymentId}")
    public PaymentResponse getPaymentById(
            @PathVariable Long paymentId) {

        return paymentService.getPaymentById(
                paymentId,
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @Operation(
            summary = "Get payment by order ID",
            description = "Returns the payment associated with a specific order belonging to the currently authenticated user."
    )
    @GetMapping("/order/{orderId}")
    public PaymentResponse getPaymentByOrderId(
            @PathVariable Long orderId) {

        return paymentService.getPaymentByOrderId(
                orderId,
                SecurityUtils.getCurrentUser().getId()
        );
    }
}