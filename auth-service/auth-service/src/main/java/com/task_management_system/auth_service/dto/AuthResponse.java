package com.task_management_system.auth_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String username;

    private AuthResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }
}