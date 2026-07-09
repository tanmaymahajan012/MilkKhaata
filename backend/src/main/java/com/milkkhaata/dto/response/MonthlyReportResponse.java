package com.milkkhaata.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class MonthlyReportResponse {

    private Integer month;

    private Integer year;

    private BigDecimal totalMilkProduced;

    private BigDecimal totalMilkDelivered;

    private BigDecimal totalSales;

    private BigDecimal paymentsReceived;

    private BigDecimal totalExpenses;

    private BigDecimal profit;

    private BigDecimal outstandingAmount;
}