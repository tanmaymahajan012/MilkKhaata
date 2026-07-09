package com.milkkhaata.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SubscriptionResponse {

    private Long id;

    private Long planId;

    private String planName;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean active;

    private Integer customerLimit;

    private Boolean whatsappBillingEnabled;

    private Boolean reportsEnabled;
}