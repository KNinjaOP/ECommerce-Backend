package com.krishna.ecommerce.dto;

import com.krishna.ecommerce.model.PaymentMethod;
import com.krishna.ecommerce.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
}