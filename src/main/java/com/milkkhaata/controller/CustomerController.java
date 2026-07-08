package com.milkkhaata.controller;

import com.milkkhaata.dto.request.CustomerRequest;
import com.milkkhaata.dto.response.CustomerResponse;
import com.milkkhaata.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerRequest request,
            Authentication authentication
    ) {

        CustomerResponse response = customerService.createCustomer(
                request,
                authentication.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(
            Authentication authentication
    ) {

        List<CustomerResponse> customers =
                customerService.getAllCustomers(authentication.getName());

        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @PathVariable
            @Positive(message = "Customer ID must be greater than 0")
            Long customerId,
            Authentication authentication
    ) {

        CustomerResponse response = customerService.getCustomerById(
                customerId,
                authentication.getName()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable
            @Positive(message = "Customer ID must be greater than 0")
            Long customerId,
            @Valid @RequestBody CustomerRequest request,
            Authentication authentication
    ) {

        CustomerResponse response = customerService.updateCustomer(
                customerId,
                request,
                authentication.getName()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable
            @Positive(message = "Customer ID must be greater than 0")
            Long customerId,
            Authentication authentication
    ) {

        customerService.deleteCustomer(
                customerId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}