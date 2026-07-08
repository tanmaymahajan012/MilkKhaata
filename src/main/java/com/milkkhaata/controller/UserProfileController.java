package com.milkkhaata.controller;

import com.milkkhaata.dto.request.UpdateProfileRequest;
import com.milkkhaata.dto.response.UserProfileResponse;
import com.milkkhaata.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.milkkhaata.dto.request.ChangePasswordRequest;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<UserProfileResponse> getMyProfile(
            Authentication authentication
    ) {

        UserProfileResponse response =
                userProfileService.getMyProfile(
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UserProfileResponse> updateMyProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication
    ) {

        UserProfileResponse response =
                userProfileService.updateMyProfile(
                        request,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {

        userProfileService.changePassword(
                request,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}