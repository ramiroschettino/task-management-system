package com.task_management_system.auth_service.service;

import com.task_management_system.auth_service.dto.AuthResponse;
import com.task_management_system.auth_service.dto.LoginRequest;
import com.task_management_system.auth_service.dto.RegisterRequest;
import com.task_management_system.auth_service.exception.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    public AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(LoginRequest request) {

        if (request.getUsername() == null || request.getPassword() == null) {
            throw new InvalidCredentialsException("Username and password are required");
        }

        if ("admin".equals(request.getUsername()) &&
                "password123".equals(request.getPassword())) {

            return AuthResponse.builder()
                    .token("jwt-token-generado")
                    .username(request.getUsername())
                    .build();
        }

        throw new InvalidCredentialsException("Invalid username or password");
    }

    public AuthResponse register(RegisterRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new InvalidCredentialsException("Email and password are required");
        }

        return AuthResponse.builder()
                .token("jwt-token-de-registro")
                .username(request.getEmail())
                .build();
    }

}