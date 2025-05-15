package com.task_management_system.auth_service.service;

import com.task_management_system.auth_service.dto.AuthResponse;
import com.task_management_system.auth_service.dto.LoginRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    public AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(LoginRequest request) {
        // Autenticación básica (usuario: admin, contraseña: password123)
        if ("admin".equals(request.getUsername()) &&
                passwordEncoder.matches("password123", passwordEncoder.encode("password123"))) {
            return new AuthResponse("token-simulado", request.getUsername());
        }
        return null; // O lanzar una excepción
    }
}