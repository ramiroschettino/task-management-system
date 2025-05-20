package com.task_management_system.auth_service.controller;

import com.task_management_system.auth_service.dto.RegisterRequest;
import com.task_management_system.auth_service.service.AuthService;
import com.task_management_system.auth_service.dto.LoginRequest;
import com.task_management_system.auth_service.dto.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/test")
    public String test() {
        return "Auth service is working!";
    }
}