package com.milkkhaata.controller;

import com.milkkhaata.dto.response.PlanResponse;
import com.milkkhaata.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    public ResponseEntity<List<PlanResponse>> getActivePlans() {

        return ResponseEntity.ok(
                planService.getActivePlans()
        );
    }
}