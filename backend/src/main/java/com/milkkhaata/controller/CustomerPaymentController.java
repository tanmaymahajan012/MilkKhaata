package com.milkkhaata.controller;

import com.milkkhaata.dto.request.CustomerPaymentRequest;
import com.milkkhaata.dto.response.CustomerPaymentResponse;
import com.milkkhaata.service.CustomerPaymentService;
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
@RequestMapping("/api/customer-payments")
@RequiredArgsConstructor
@Validated
public class CustomerPaymentController {

    private final CustomerPaymentService customerPaymentService;

    @PostMapping
    public ResponseEntity<CustomerPaymentResponse> recordPayment(
            @Valid @RequestBody CustomerPaymentRequest request,
            Authentication authentication
    ) {

        CustomerPaymentResponse response =
                customerPaymentService.recordPayment(
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerPaymentResponse>> getAllPayments(
            Authentication authentication
    ) {

        List<CustomerPaymentResponse> payments =
                customerPaymentService.getAllPayments(
                        authentication.getName()
                );

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<CustomerPaymentResponse> getPaymentById(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long paymentId,
            Authentication authentication
    ) {

        CustomerPaymentResponse response =
                customerPaymentService.getPaymentById(
                        paymentId,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerPaymentResponse>> getPaymentsByCustomer(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long customerId,
            Authentication authentication
    ) {

        List<CustomerPaymentResponse> payments =
                customerPaymentService.getPaymentsByCustomer(
                        customerId,
                        authentication.getName()
                );

        return ResponseEntity.ok(payments);
    }
}