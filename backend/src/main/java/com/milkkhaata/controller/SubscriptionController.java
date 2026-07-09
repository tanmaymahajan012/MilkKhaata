package com.milkkhaata.controller;

import com.milkkhaata.dto.request.SubscriptionRequest;
import com.milkkhaata.dto.response.SubscriptionResponse;
import com.milkkhaata.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> subscribe(
            @Valid @RequestBody SubscriptionRequest request,
            Authentication authentication
    ) {

        SubscriptionResponse response =
                subscriptionService.subscribe(
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<SubscriptionResponse> getMySubscription(
            Authentication authentication
    ) {

        SubscriptionResponse response =
                subscriptionService.getMySubscription(
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }
}