package com.milkkhaata.service;

import com.milkkhaata.dto.response.MonthlyReportResponse;
import com.milkkhaata.entities.CustomerPayment;
import com.milkkhaata.entities.MilkDelivery;
import com.milkkhaata.entities.MilkProductionEntry;
import com.milkkhaata.entities.Subscription;
import com.milkkhaata.entities.User;
import com.milkkhaata.exception.ResourceNotFoundException;
import com.milkkhaata.exception.AccessDeniedException;
import com.milkkhaata.repository.CustomerPaymentRepository;
import com.milkkhaata.repository.ExpenseRepository;
import com.milkkhaata.repository.MilkDeliveryRepository;
import com.milkkhaata.repository.MilkProductionEntryRepository;
import com.milkkhaata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.milkkhaata.entities.Bill;
import com.milkkhaata.entities.BillStatus;
import com.milkkhaata.repository.BillRepository;
import com.milkkhaata.dto.response.YearlyReportResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;
    private final MilkProductionEntryRepository milkProductionEntryRepository;
    private final MilkDeliveryRepository milkDeliveryRepository;
    private final CustomerPaymentRepository customerPaymentRepository;
    private final ExpenseRepository expenseRepository;
    private final BillRepository billRepository;

    public MonthlyReportResponse getMonthlyReport(
            Integer month,
            Integer year,
            String email
    ) {

        User user = getUserByEmail(email);

        validateReportsAccess(user);

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(
                startDate.lengthOfMonth()
        );

        BigDecimal totalMilkProduced =
                milkProductionEntryRepository
                        .findByUserAndEntryDateBetween(
                                user,
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(MilkProductionEntry::getLiters)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<MilkDelivery> deliveries =
                milkDeliveryRepository
                        .findByUserAndDeliveryDateBetween(
                                user,
                                startDate,
                                endDate
                        );

        BigDecimal totalMilkDelivered = deliveries.stream()
                .map(MilkDelivery::getLiters)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSales = deliveries.stream()
                .map(delivery ->
                        delivery.getLiters()
                                .multiply(delivery.getRatePerLiter())
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal paymentsReceived =
                customerPaymentRepository
                        .findByUserAndPaymentDateBetween(
                                user,
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(CustomerPayment::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses =
                expenseRepository
                        .findByUserAndExpenseDateBetween(
                                user,
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(expense -> expense.getAmount())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profit =
                totalSales.subtract(totalExpenses);

        BigDecimal outstandingAmount =
                billRepository
                        .findByUserAndMonthAndYearAndStatus(
                                user,
                                month,
                                year,
                                BillStatus.UNPAID
                        )
                        .stream()
                        .map(Bill::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MonthlyReportResponse(
                month,
                year,
                totalMilkProduced,
                totalMilkDelivered,
                totalSales,
                paymentsReceived,
                totalExpenses,
                profit,
                outstandingAmount
        );
    }

    private void validateReportsAccess(User user) {

        Subscription subscription =
                subscriptionService.getActiveSubscription(user);

        if (!subscription.getPlan().getReportsEnabled()) {
            throw new AccessDeniedException(
                    "Reports are not available in your current plan"
            );
        }
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    public YearlyReportResponse getYearlyReport(
            Integer year,
            String email
    ) {

        User user = getUserByEmail(email);

        validateReportsAccess(user);

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        BigDecimal totalMilkProduced =
                milkProductionEntryRepository
                        .findByUserAndEntryDateBetween(
                                user,
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(MilkProductionEntry::getLiters)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<MilkDelivery> deliveries =
                milkDeliveryRepository
                        .findByUserAndDeliveryDateBetween(
                                user,
                                startDate,
                                endDate
                        );

        BigDecimal totalMilkDelivered = deliveries.stream()
                .map(MilkDelivery::getLiters)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSales = deliveries.stream()
                .map(delivery ->
                        delivery.getLiters()
                                .multiply(delivery.getRatePerLiter())
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal paymentsReceived =
                customerPaymentRepository
                        .findByUserAndPaymentDateBetween(
                                user,
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(CustomerPayment::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses =
                expenseRepository
                        .findByUserAndExpenseDateBetween(
                                user,
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(expense -> expense.getAmount())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profit =
                totalSales.subtract(totalExpenses);

        BigDecimal outstandingAmount =
                billRepository
                        .findByUserAndYearAndStatus(
                                user,
                                year,
                                BillStatus.UNPAID
                        )
                        .stream()
                        .map(Bill::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new YearlyReportResponse(
                year,
                totalMilkProduced,
                totalMilkDelivered,
                totalSales,
                paymentsReceived,
                totalExpenses,
                profit,
                outstandingAmount
        );
    }
}