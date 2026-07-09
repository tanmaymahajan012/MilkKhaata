package com.milkkhaata.controller;

import com.milkkhaata.dto.request.MilkDeliveryRequest;
import com.milkkhaata.dto.response.MilkDeliveryResponse;
import com.milkkhaata.service.MilkDeliveryService;
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
@RequestMapping("/api/milk-deliveries")
@RequiredArgsConstructor
@Validated
public class MilkDeliveryController {

    private final MilkDeliveryService milkDeliveryService;

    @PostMapping
    public ResponseEntity<MilkDeliveryResponse> createDelivery(
            @Valid @RequestBody MilkDeliveryRequest request,
            Authentication authentication
    ) {

        MilkDeliveryResponse response =
                milkDeliveryService.createDelivery(
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MilkDeliveryResponse>> getAllDeliveries(
            Authentication authentication
    ) {

        List<MilkDeliveryResponse> deliveries =
                milkDeliveryService.getAllDeliveries(
                        authentication.getName()
                );

        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<MilkDeliveryResponse> getDeliveryById(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long deliveryId,
            Authentication authentication
    ) {

        MilkDeliveryResponse response =
                milkDeliveryService.getDeliveryById(
                        deliveryId,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{deliveryId}")
    public ResponseEntity<MilkDeliveryResponse> updateDelivery(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long deliveryId,
            @Valid @RequestBody MilkDeliveryRequest request,
            Authentication authentication
    ) {

        MilkDeliveryResponse response =
                milkDeliveryService.updateDelivery(
                        deliveryId,
                        request,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<Void> deleteDelivery(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long deliveryId,
            Authentication authentication
    ) {

        milkDeliveryService.deleteDelivery(
                deliveryId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<MilkDeliveryResponse>> getDeliveriesByCustomer(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long customerId,
            Authentication authentication
    ) {

        List<MilkDeliveryResponse> deliveries =
                milkDeliveryService.getDeliveriesByCustomer(
                        customerId,
                        authentication.getName()
                );

        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/customer/{customerId}/monthly")
    public ResponseEntity<List<MilkDeliveryResponse>>
    getDeliveriesByCustomerAndMonth(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long customerId,
            @RequestParam Integer month,
            @RequestParam Integer year,
            Authentication authentication
    ) {

        List<MilkDeliveryResponse> deliveries =
                milkDeliveryService.getDeliveriesByCustomerAndMonth(
                        customerId,
                        month,
                        year,
                        authentication.getName()
                );

        return ResponseEntity.ok(deliveries);
    }
}