package com.task_management_system.auth_service.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String username;
}