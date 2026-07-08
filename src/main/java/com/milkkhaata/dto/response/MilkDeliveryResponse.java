package com.milkkhaata.dto.response;

import com.milkkhaata.entities.Shift;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MilkDeliveryResponse {

    private Long id;

    private Long customerId;

    private String customerName;

    private LocalDate deliveryDate;

    private BigDecimal liters;

    private BigDecimal ratePerLiter;

    private Shift shift;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}