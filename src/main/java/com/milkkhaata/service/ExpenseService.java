package com.milkkhaata.service;

import com.milkkhaata.dto.request.ExpenseRequest;
import com.milkkhaata.dto.response.ExpenseResponse;
import com.milkkhaata.entities.Expense;
import com.milkkhaata.entities.User;
import com.milkkhaata.exception.ResourceNotFoundException;
import com.milkkhaata.repository.ExpenseRepository;
import com.milkkhaata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseResponse createExpense(
            ExpenseRequest request,
            String email
    ) {

        User user = getUserByEmail(email);

        Expense expense = Expense.builder()
                .expenseDate(request.getExpenseDate())
                .amount(request.getAmount())
                .description(request.getDescription())
                .category(request.getCategory())
                .user(user)
                .build();

        Expense savedExpense = expenseRepository.save(expense);

        return mapToResponse(savedExpense);
    }

    public List<ExpenseResponse> getAllExpenses(String email) {

        User user = getUserByEmail(email);

        return expenseRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ExpenseResponse getExpenseById(
            Long expenseId,
            String email
    ) {

        User user = getUserByEmail(email);

        Expense expense = expenseRepository
                .findByIdAndUser(expenseId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Expense not found"
                        )
                );

        return mapToResponse(expense);
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private ExpenseResponse mapToResponse(Expense expense) {

        return new ExpenseResponse(
                expense.getId(),
                expense.getExpenseDate(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getCategory()
        );
    }

    public ExpenseResponse updateExpense(
            Long expenseId,
            ExpenseRequest request,
            String email
    ) {

        User user = getUserByEmail(email);

        Expense expense = expenseRepository
                .findByIdAndUser(expenseId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Expense not found"
                        )
                );

        expense.setExpenseDate(request.getExpenseDate());
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setCategory(request.getCategory());

        Expense updatedExpense = expenseRepository.save(expense);

        return mapToResponse(updatedExpense);
    }

    public void deleteExpense(
            Long expenseId,
            String email
    ) {

        User user = getUserByEmail(email);

        Expense expense = expenseRepository
                .findByIdAndUser(expenseId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Expense not found"
                        )
                );

        expenseRepository.delete(expense);
    }

    public List<ExpenseResponse> getExpensesByMonth(
            Integer month,
            Integer year,
            String email
    ) {

        User user = getUserByEmail(email);

        LocalDate startDate = LocalDate.of(year, month, 1);

        LocalDate endDate = startDate.withDayOfMonth(
                startDate.lengthOfMonth()
        );

        return expenseRepository
                .findByUserAndExpenseDateBetween(
                        user,
                        startDate,
                        endDate
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}