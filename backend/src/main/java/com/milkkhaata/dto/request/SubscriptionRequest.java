package com.milkkhaata.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionRequest {

    @NotNull(message = "Plan ID is required")
    @Positive(message = "Plan ID must be greater than 0")
    private Long planId;
}