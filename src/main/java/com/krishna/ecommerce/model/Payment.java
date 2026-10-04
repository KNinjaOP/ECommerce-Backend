package com.krishna.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
