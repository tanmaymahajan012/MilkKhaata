package com.milkkhaata.controller;

import com.milkkhaata.dto.request.ExpenseRequest;
import com.milkkhaata.dto.response.ExpenseResponse;
import com.milkkhaata.service.ExpenseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Validated
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseRequest request,
            Authentication authentication
    ) {

        ExpenseResponse response = expenseService.createExpense(
                request,
                authentication.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses(
            Authentication authentication
    ) {

        List<ExpenseResponse> expenses =
                expenseService.getAllExpenses(
                        authentication.getName()
                );

        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long expenseId,
            Authentication authentication
    ) {

        ExpenseResponse response = expenseService.getExpenseById(
                expenseId,
                authentication.getName()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long expenseId,
            @Valid @RequestBody ExpenseRequest request,
            Authentication authentication
    ) {

        ExpenseResponse response = expenseService.updateExpense(
                expenseId,
                request,
                authentication.getName()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long expenseId,
            Authentication authentication
    ) {

        expenseService.deleteExpense(
                expenseId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByMonth(
            @RequestParam Integer month,
            @RequestParam Integer year,
            Authentication authentication
    ) {

        List<ExpenseResponse> expenses =
                expenseService.getExpensesByMonth(
                        month,
                        year,
                        authentication.getName()
                );

        return ResponseEntity.ok(expenses);
    }
}