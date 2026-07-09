package com.milkkhaata.controller;

import com.milkkhaata.dto.request.BillRequest;
import com.milkkhaata.dto.response.BillResponse;
import com.milkkhaata.service.BillService;
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
@RequestMapping("/api/bills")
@RequiredArgsConstructor
@Validated
public class BillController {

    private final BillService billService;

    @PostMapping
    public ResponseEntity<BillResponse> generateBill(
            @Valid @RequestBody BillRequest request,
            Authentication authentication
    ) {

        BillResponse response = billService.generateBill(
                request,
                authentication.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<BillResponse>> getAllBills(
            Authentication authentication
    ) {

        List<BillResponse> bills =
                billService.getAllBills(authentication.getName());

        return ResponseEntity.ok(bills);
    }

    @GetMapping("/{billId}")
    public ResponseEntity<BillResponse> getBillById(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long billId,
            Authentication authentication
    ) {

        BillResponse response = billService.getBillById(
                billId,
                authentication.getName()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{billId}/pay")
    public ResponseEntity<BillResponse> markBillAsPaid(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long billId,
            Authentication authentication
    ) {

        BillResponse response = billService.markBillAsPaid(
                billId,
                authentication.getName()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BillResponse>> getBillsByCustomer(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long customerId,
            Authentication authentication
    ) {

        List<BillResponse> bills =
                billService.getBillsByCustomer(
                        customerId,
                        authentication.getName()
                );

        return ResponseEntity.ok(bills);
    }
}