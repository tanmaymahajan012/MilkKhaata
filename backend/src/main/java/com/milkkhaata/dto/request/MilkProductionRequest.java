package com.milkkhaata.dto.request;

import com.milkkhaata.entities.Shift;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class MilkProductionRequest {

    @NotNull(message = "Entry date is required")
    private LocalDate entryDate;

    @NotNull(message = "Milk quantity is required")
    @DecimalMin(
            value = "0.1",
            message = "Milk quantity must be greater than 0"
    )
    private BigDecimal liters;

    @NotNull(message = "Rate per liter is required")
    @DecimalMin(
            value = "0.01",
            message = "Rate must be greater than 0"
    )
    private BigDecimal ratePerLiter;

    @NotNull(message = "Shift is required")
    private Shift shift;
}