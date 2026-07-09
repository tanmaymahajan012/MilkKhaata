package com.milkkhaata.controller;

import com.milkkhaata.dto.request.MilkProductionRequest;
import com.milkkhaata.dto.response.MilkProductionResponse;
import com.milkkhaata.service.MilkProductionService;
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
@RequestMapping("/api/milk-production")
@RequiredArgsConstructor
@Validated
public class MilkProductionController {

    private final MilkProductionService milkProductionService;

    @PostMapping
    public ResponseEntity<MilkProductionResponse> createProductionEntry(
            @Valid @RequestBody MilkProductionRequest request,
            Authentication authentication
    ) {

        MilkProductionResponse response =
                milkProductionService.createProductionEntry(
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MilkProductionResponse>> getAllProductionEntries(
            Authentication authentication
    ) {

        List<MilkProductionResponse> entries =
                milkProductionService.getAllProductionEntries(
                        authentication.getName()
                );

        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{entryId}")
    public ResponseEntity<MilkProductionResponse> getProductionEntryById(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long entryId,
            Authentication authentication
    ) {

        MilkProductionResponse response =
                milkProductionService.getProductionEntryById(
                        entryId,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{entryId}")
    public ResponseEntity<MilkProductionResponse> updateProductionEntry(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long entryId,
            @Valid @RequestBody MilkProductionRequest request,
            Authentication authentication
    ) {

        MilkProductionResponse response =
                milkProductionService.updateProductionEntry(
                        entryId,
                        request,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{entryId}")
    public ResponseEntity<Void> deleteProductionEntry(
            @PathVariable
            @Positive(message = "ID must be greater than 0")
            Long entryId,
            Authentication authentication
    ) {

        milkProductionService.deleteProductionEntry(
                entryId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}