package com.milkkhaata.service;

import com.milkkhaata.dto.request.CustomerPaymentRequest;
import com.milkkhaata.dto.response.CustomerPaymentResponse;
import com.milkkhaata.entities.*;
import com.milkkhaata.exception.ResourceAlreadyExistsException;
import com.milkkhaata.exception.ResourceNotFoundException;
import com.milkkhaata.repository.BillRepository;
import com.milkkhaata.repository.CustomerPaymentRepository;
import com.milkkhaata.repository.CustomerRepository;
import com.milkkhaata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerPaymentService {

    private final CustomerPaymentRepository customerPaymentRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerPaymentResponse recordPayment(
            CustomerPaymentRequest request,
            String email
    ) {

        User user = getUserByEmail(email);

        Bill bill = billRepository
                .findByIdAndUser(request.getBillId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bill not found")
                );

        if (customerPaymentRepository.existsByBillId(bill.getId())) {
            throw new ResourceAlreadyExistsException(
                    "Payment already recorded for this bill"
            );
        }

        if (bill.getStatus() == BillStatus.PAID) {
            throw new ResourceAlreadyExistsException(
                    "Bill is already marked as paid"
            );
        }

        CustomerPayment payment = CustomerPayment.builder()
                .amount(bill.getTotalAmount())
                .paymentDate(request.getPaymentDate())
                .paymentMethod(request.getPaymentMethod())
                .note(request.getNote())
                .customer(bill.getCustomer())
                .bill(bill)
                .user(user)
                .build();

        CustomerPayment savedPayment =
                customerPaymentRepository.save(payment);

        bill.setStatus(BillStatus.PAID);
        billRepository.save(bill);

        return mapToResponse(savedPayment);
    }

    public List<CustomerPaymentResponse> getAllPayments(
            String email
    ) {

        User user = getUserByEmail(email);

        return customerPaymentRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CustomerPaymentResponse getPaymentById(
            Long paymentId,
            String email
    ) {

        User user = getUserByEmail(email);

        CustomerPayment payment = customerPaymentRepository
                .findByIdAndUser(paymentId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer payment not found"
                        )
                );

        return mapToResponse(payment);
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private CustomerPaymentResponse mapToResponse(
            CustomerPayment payment
    ) {

        return new CustomerPaymentResponse(
                payment.getId(),
                payment.getBill().getId(),
                payment.getCustomer().getId(),
                payment.getCustomer().getName(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getPaymentMethod(),
                payment.getNote(),
                payment.getCreatedAt()
        );
    }

    public List<CustomerPaymentResponse> getPaymentsByCustomer(
            Long customerId,
            String email
    ) {

        User user = getUserByEmail(email);

        Customer customer = customerRepository
                .findByIdAndUser(customerId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found")
                );

        return customerPaymentRepository
                .findByCustomerAndUser(customer, user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}