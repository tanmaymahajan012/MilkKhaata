package com.milkkhaata.dto.response;

import com.milkkhaata.entities.ExpenseCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ExpenseResponse {

    private Long id;

    private LocalDate expenseDate;

    private BigDecimal amount;

    private String description;

    private ExpenseCategory category;
}