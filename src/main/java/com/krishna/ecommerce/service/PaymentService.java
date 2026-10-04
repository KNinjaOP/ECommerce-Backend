package com.krishna.ecommerce.service;

import com.krishna.ecommerce.dto.PaymentRequest;
import com.krishna.ecommerce.dto.PaymentResponse;
import com.krishna.ecommerce.exception.DuplicateResourceException;
import com.krishna.ecommerce.exception.InvalidPaymentStatusException;
import com.krishna.ecommerce.exception.ResourceNotFoundException;
import com.krishna.ecommerce.model.Order;
import com.krishna.ecommerce.model.OrderStatus;
import com.krishna.ecommerce.model.Payment;
import com.krishna.ecommerce.model.PaymentStatus;
import com.krishna.ecommerce.repository.OrderRepository;
import com.krishna.ecommerce.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public PaymentResponse createPayment(
            Long userId,
            PaymentRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + request.getOrderId()
                        ));

        if (!order.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Order not found with id: " + request.getOrderId()
            );
        }

        if (paymentRepository.findByOrderId(request.getOrderId()).isPresent()) {
            throw new DuplicateResourceException(
                    "Payment already exists for the order id: " + order.getId()
            );
        }

        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        return toPaymentResponse(payment);
    }

    @Transactional
    public PaymentResponse processPayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + paymentId
                        ));

        if (payment.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new InvalidPaymentStatusException(
                    "Payment cannot be processed because its current status is: "
                        + payment.getPaymentStatus()
            );
        }

        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.CONFIRMED);

        paymentRepository.save(payment);
        orderRepository.save(order);

        return toPaymentResponse(payment);
    }

    public PaymentResponse getPaymentById(
            Long paymentId,
            Long userId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + paymentId
                        )
                );

        if (!payment.getOrder().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Payment not found with id: " + paymentId
            );
        }

        return toPaymentResponse(payment);
    }

    public PaymentResponse getPaymentByOrderId(
            Long orderId,
            Long userId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for order id: " + orderId
                        )
                );

        if (!payment.getOrder().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Payment not found for order id: " + orderId
            );
        }

        return toPaymentResponse(payment);
    }

    private PaymentResponse toPaymentResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setId(payment.getId());
        response.setOrderId(payment.getOrder().getId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentStatus(payment.getPaymentStatus());
        response.setCreatedAt(payment.getCreatedAt());

        return response;
    }

}