package com.krishna.ecommerce.dto;

import com.krishna.ecommerce.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private PaymentMethod paymentMethod;

}