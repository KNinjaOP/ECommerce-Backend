package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.PaymentRequest;
import com.krishna.ecommerce.dto.PaymentResponse;
import com.krishna.ecommerce.security.SecurityUtils;
import com.krishna.ecommerce.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse createPayment(
            @Valid @RequestBody PaymentRequest request) {

        return paymentService.createPayment(
                SecurityUtils.getCurrentUser().getId(),
                request
        );
    }

    @PatchMapping("/{paymentId}/process")
    public PaymentResponse processPayment(
            @PathVariable Long paymentId) {

        return paymentService.processPayment(paymentId);
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse getPaymentById(
            @PathVariable Long paymentId) {

        return paymentService.getPaymentById(
                paymentId,
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @GetMapping("/order/{orderId}")
    public PaymentResponse getPaymentByOrderId(
            @PathVariable Long orderId) {

        return paymentService.getPaymentByOrderId(
                orderId,
                SecurityUtils.getCurrentUser().getId()
        );
    }
}