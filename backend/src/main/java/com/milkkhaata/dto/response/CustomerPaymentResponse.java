package com.milkkhaata.dto.response;

import com.milkkhaata.entities.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CustomerPaymentResponse {

    private Long id;

    private Long billId;

    private Long customerId;

    private String customerName;

    private BigDecimal amount;

    private LocalDate paymentDate;

    private PaymentMethod paymentMethod;

    private String note;

    private LocalDateTime createdAt;
}