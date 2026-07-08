package com.milkkhaata.service;

import com.milkkhaata.dto.request.UpdateProfileRequest;
import com.milkkhaata.dto.response.UserProfileResponse;
import com.milkkhaata.entities.User;
import com.milkkhaata.exception.ResourceAlreadyExistsException;
import com.milkkhaata.exception.ResourceNotFoundException;
import com.milkkhaata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import com.milkkhaata.dto.request.ChangePasswordRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileResponse getMyProfile(String email) {

        User user = getUserByEmail(email);

        return mapToResponse(user);
    }

    public UserProfileResponse updateMyProfile(
            UpdateProfileRequest request,
            String email
    ) {

        User user = getUserByEmail(email);

        userRepository.findByPhone(request.getPhone())
                .filter(existingUser ->
                        !existingUser.getId().equals(user.getId())
                )
                .ifPresent(existingUser -> {
                    throw new ResourceAlreadyExistsException(
                            "Phone number already in use"
                    );
                });

        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());

        User updatedUser = userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private UserProfileResponse mapToResponse(User user) {

        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getCreatedAt()
        );
    }

    public void changePassword(
            ChangePasswordRequest request,
            String email
    ) {

        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        )) {
            throw new BadCredentialsException(
                    "Current password is incorrect"
            );
        }

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword()
        )) {
            throw new IllegalArgumentException(
                    "New password must be different from current password"
            );
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);
    }
}