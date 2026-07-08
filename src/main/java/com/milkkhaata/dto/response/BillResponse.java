package com.milkkhaata.dto.response;

import com.milkkhaata.entities.BillStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BillResponse {

    private Long id;

    private Long customerId;

    private String customerName;

    private Integer month;

    private Integer year;

    private BigDecimal totalAmount;

    private LocalDate generatedDate;

    private BillStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}