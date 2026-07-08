package com.milkkhaata.service;

import com.milkkhaata.dto.request.BillRequest;
import com.milkkhaata.dto.response.BillResponse;
import com.milkkhaata.entities.*;
import com.milkkhaata.exception.ResourceAlreadyExistsException;
import com.milkkhaata.exception.ResourceNotFoundException;
import com.milkkhaata.repository.BillRepository;
import com.milkkhaata.repository.CustomerRepository;
import com.milkkhaata.repository.MilkDeliveryRepository;
import com.milkkhaata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final CustomerRepository customerRepository;
    private final MilkDeliveryRepository milkDeliveryRepository;
    private final UserRepository userRepository;

    public BillResponse generateBill(
            BillRequest request,
            String email
    ) {

        User user = getUserByEmail(email);

        Customer customer = customerRepository
                .findByIdAndUser(request.getCustomerId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found")
                );

        billRepository
                .findByCustomerAndMonthAndYearAndUser(
                        customer,
                        request.getMonth(),
                        request.getYear(),
                        user
                )
                .ifPresent(existingBill -> {
                    throw new ResourceAlreadyExistsException(
                            "Bill already exists for this customer and month"
                    );
                });

        LocalDate startDate = LocalDate.of(
                request.getYear(),
                request.getMonth(),
                1
        );

        LocalDate endDate = startDate.withDayOfMonth(
                startDate.lengthOfMonth()
        );

        List<MilkDelivery> deliveries = milkDeliveryRepository
                .findByCustomerAndUserAndDeliveryDateBetween(
                        customer,
                        user,
                        startDate,
                        endDate
                );

        if (deliveries.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No milk deliveries found for this customer and month"
            );
        }

        BigDecimal totalAmount = deliveries.stream()
                .map(delivery ->
                        delivery.getLiters()
                                .multiply(delivery.getRatePerLiter())
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Bill bill = Bill.builder()
                .month(request.getMonth())
                .year(request.getYear())
                .totalAmount(totalAmount)
                .generatedDate(LocalDate.now())
                .status(BillStatus.UNPAID)
                .customer(customer)
                .user(user)
                .build();

        Bill savedBill = billRepository.save(bill);

        return mapToResponse(savedBill);
    }

    public List<BillResponse> getAllBills(String email) {

        User user = getUserByEmail(email);

        return billRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public BillResponse getBillById(
            Long billId,
            String email
    ) {

        User user = getUserByEmail(email);

        Bill bill = billRepository
                .findByIdAndUser(billId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bill not found")
                );

        return mapToResponse(bill);
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private BillResponse mapToResponse(Bill bill) {

        return new BillResponse(
                bill.getId(),
                bill.getCustomer().getId(),
                bill.getCustomer().getName(),
                bill.getMonth(),
                bill.getYear(),
                bill.getTotalAmount(),
                bill.getGeneratedDate(),
                bill.getStatus(),
                bill.getCreatedAt(),
                bill.getUpdatedAt()
        );
    }

    public BillResponse markBillAsPaid(
            Long billId,
            String email
    ) {

        User user = getUserByEmail(email);

        Bill bill = billRepository
                .findByIdAndUser(billId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bill not found")
                );

        bill.setStatus(BillStatus.PAID);

        Bill updatedBill = billRepository.save(bill);

        return mapToResponse(updatedBill);
    }

    public List<BillResponse> getBillsByCustomer(
            Long customerId,
            String email
    ) {

        User user = getUserByEmail(email);

        Customer customer = customerRepository
                .findByIdAndUser(customerId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found")
                );

        return billRepository.findByCustomerAndUser(customer, user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}