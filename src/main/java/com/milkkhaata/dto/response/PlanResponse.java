package com.milkkhaata.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PlanResponse {

    private Long id;

    private String name;

    private BigDecimal monthlyPrice;

    private Integer customerLimit;

    private Boolean whatsappBillingEnabled;

    private Boolean reportsEnabled;
}