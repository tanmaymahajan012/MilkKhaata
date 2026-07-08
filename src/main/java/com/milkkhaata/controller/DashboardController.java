package com.milkkhaata.controller;

import com.milkkhaata.dto.response.DashboardSummaryResponse;
import com.milkkhaata.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Validated
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary(
            @RequestParam
            @Min(value = 1, message = "Month must be between 1 and 12")
            @Max(value = 12, message = "Month must be between 1 and 12")
            Integer month,

            @RequestParam
            @Min(value = 2000, message = "Year must be 2000 or later")
            @Max(value = 2100, message = "Year must be 2100 or earlier")
            Integer year,
            Authentication authentication
    ) {

        DashboardSummaryResponse response =
                dashboardService.getDashboardSummary(
                        month,
                        year,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }
}