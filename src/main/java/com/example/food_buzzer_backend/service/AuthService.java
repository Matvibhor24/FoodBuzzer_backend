package com.example.food_buzzer_backend.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.food_buzzer_backend.config.AppConstants;
import com.example.food_buzzer_backend.dto.auth.LoginRequest;
import com.example.food_buzzer_backend.dto.auth.LoginResponse;
import com.example.food_buzzer_backend.dto.auth.RegisterOwnerRequest;
import com.example.food_buzzer_backend.dto.auth.RegisterOwnerResponse;
import com.example.food_buzzer_backend.dto.auth.UserProfileResponseDTO;
import com.example.food_buzzer_backend.model.User;
import com.example.food_buzzer_backend.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return new LoginResponse(null, null, null,
                    AppConstants.MSG_INVALID_CREDENTIALS);
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new LoginResponse(null, null, null,
                    AppConstants.MSG_INVALID_CREDENTIALS);
        }

        if (!user.getIsActive()) {
            return new LoginResponse(null, null, null,
                    AppConstants.MSG_USER_INACTIVE);
        }

        if (!AppConstants.ROLE_ADMIN.equals(user.getRole())
                && user.getRestaurant() == null) {
            return new LoginResponse(
                    user.getId(),
                    user.getRole(),
                    user.getAccessLevel(),
                    AppConstants.MSG_USER_NOT_ASSIGNED_TO_RESTAURANT
            );
        }

        return new LoginResponse(
                user.getId(),
                user.getRole(),
                user.getAccessLevel(),
                AppConstants.MSG_LOGIN_SUCCESSFUL
        );
    }

    public RegisterOwnerResponse registerOwner(RegisterOwnerRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new RegisterOwnerResponse(
                    null, null, null,
                    AppConstants.MSG_EMAIL_ALREADY_REGISTERED
            );
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(AppConstants.ROLE_OWNER);
        user.setAccessLevel(AppConstants.ACCESS_LEVEL_OWNER);
        user.setIsActive(AppConstants.DEFAULT_USER_ACTIVE);

        userRepository.save(user);

        return new RegisterOwnerResponse(
                user.getId(),
                user.getRole(),
                user.getAccessLevel(),
                AppConstants.MSG_OWNER_REGISTERED_SUCCESSFUL
        );
    }

    public UserProfileResponseDTO getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserProfileResponseDTO(user);
    }
}