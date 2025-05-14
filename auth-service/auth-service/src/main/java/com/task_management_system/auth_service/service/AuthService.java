package com.task_management_system.auth_service.service;

import com.task_management_system.auth_service.dto.AuthResponse;
import com.task_management_system.auth_service.dto.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public AuthResponse authenticate(LoginRequest request) {
        // Implementación temporal - reemplazar con lógica real
        return new AuthResponse("jwt-token-dummy", request.getUsername());
    }
}