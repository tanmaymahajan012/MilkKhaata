package com.milkkhaata.service;

import com.milkkhaata.dto.response.DashboardSummaryResponse;
import com.milkkhaata.entities.Bill;
import com.milkkhaata.entities.BillStatus;
import com.milkkhaata.entities.MilkDelivery;
import com.milkkhaata.entities.MilkProductionEntry;
import com.milkkhaata.entities.User;
import com.milkkhaata.exception.ResourceNotFoundException;
import com.milkkhaata.repository.BillRepository;
import com.milkkhaata.repository.CustomerRepository;
import com.milkkhaata.repository.ExpenseRepository;
import com.milkkhaata.repository.MilkDeliveryRepository;
import com.milkkhaata.repository.MilkProductionEntryRepository;
import com.milkkhaata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final MilkProductionEntryRepository milkProductionEntryRepository;
    private final MilkDeliveryRepository milkDeliveryRepository;
    private final ExpenseRepository expenseRepository;
    private final BillRepository billRepository;

    public DashboardSummaryResponse getDashboardSummary(
            Integer month,
            Integer year,
            String email
    ) {

        User user = getUserByEmail(email);

        LocalDate startDate = LocalDate.of(year, month, 1);

        LocalDate endDate = startDate.withDayOfMonth(
                startDate.lengthOfMonth()
        );

        long activeCustomers =
                customerRepository.countByUserAndActiveTrue(user);

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

        BigDecimal totalRevenue = deliveries.stream()
                .map(delivery ->
                        delivery.getLiters()
                                .multiply(delivery.getRatePerLiter())
                )
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
                totalRevenue.subtract(totalExpenses);

        List<Bill> unpaidBills =
                billRepository.findByUserAndStatus(
                        user,
                        BillStatus.UNPAID
                );

        long unpaidBillCount = unpaidBills.size();

        BigDecimal outstandingAmount = unpaidBills.stream()
                .map(Bill::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardSummaryResponse(
                month,
                year,
                activeCustomers,
                totalMilkProduced,
                totalMilkDelivered,
                totalRevenue,
                totalExpenses,
                profit,
                unpaidBillCount,
                outstandingAmount
        );
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }
}