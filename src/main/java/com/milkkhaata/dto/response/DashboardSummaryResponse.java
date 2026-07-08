package com.milkkhaata.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DashboardSummaryResponse {

    private Integer month;

    private Integer year;

    private Long activeCustomers;

    private BigDecimal totalMilkProduced;

    private BigDecimal totalMilkDelivered;

    private BigDecimal totalRevenue;

    private BigDecimal totalExpenses;

    private BigDecimal profit;

    private Long unpaidBills;

    private BigDecimal outstandingAmount;
}