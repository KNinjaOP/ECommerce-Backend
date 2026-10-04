package com.krishna.ecommerce.dto;

import com.krishna.ecommerce.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusRequest {

    @NotNull
    private OrderStatus status;
}
