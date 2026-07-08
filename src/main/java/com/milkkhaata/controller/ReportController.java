package com.milkkhaata.controller;

import com.milkkhaata.dto.response.MonthlyReportResponse;
import com.milkkhaata.dto.response.YearlyReportResponse;
import com.milkkhaata.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyReportResponse> getMonthlyReport(
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

        MonthlyReportResponse response =
                reportService.getMonthlyReport(
                        month,
                        year,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/yearly")
    public ResponseEntity<YearlyReportResponse> getYearlyReport(
            @RequestParam
            @Min(value = 2000, message = "Year must be 2000 or later")
            @Max(value = 2100, message = "Year must be 2100 or earlier")
            Integer year,
            Authentication authentication
    ) {

        YearlyReportResponse response =
                reportService.getYearlyReport(
                        year,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }
}