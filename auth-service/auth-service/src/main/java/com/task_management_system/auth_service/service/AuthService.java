package com.task_management_system.auth_service.service;

import com.task_management_system.auth_service.dto.AuthResponse;
import com.task_management_system.auth_service.dto.LoginRequest;
import com.task_management_system.auth_service.dto.RegisterRequest;
import com.task_management_system.auth_service.exception.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.Map;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final WebClient webClient;

    @Value("${user.service.url:http://user-service:8083}")
    private String userServiceUrl;

    public AuthService(PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.webClient = WebClient.builder().baseUrl(userServiceUrl).build();
    }

    // Método para autenticar usuario
    public AuthResponse authenticate(LoginRequest request) {
        if ((request.getUsername() == null && request.getEmail() == null) || request.getPassword() == null) {
            throw new InvalidCredentialsException("Username/email and password are required");
        }

        Map<String, String> credentials = new java.util.HashMap<>();
        if (request.getUsername() != null) credentials.put("username", request.getUsername());
        if (request.getEmail() != null) credentials.put("email", request.getEmail());
        credentials.put("password", request.getPassword());

        Map user;
        try {
            user = webClient.post()
                    .uri("/api/users/validate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(credentials)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new InvalidCredentialsException("Invalid username/email or password");
        }

        if (user == null || user.get("username") == null) {
            throw new InvalidCredentialsException("Invalid username/email or password");
        }

        String username = (String) user.get("username");
        String role = user.get("role") != null ? user.get("role").toString() : "USER";
        UserDetails userDetails = User.withUsername(username)
                .password("")
                .authorities(role)
                .build();

        String token = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .username(username)
                .build();
    }

    // Método para registrar usuario (dummy, debes implementar lógica real)
    //public AuthResponse register(RegisterRequest request) {
        // Aquí deberías llamar a user-service para crear el usuario
        // Por ahora solo retorna un AuthResponse vacío
        //return AuthResponse.builder()
    // .token("")
    // .username(request.getUsername())
    // .build();
    //}

    // Método para refrescar el token JWT
    public AuthResponse refreshToken(String token) {
        String username = jwtService.extractUsername(token);
        UserDetails userDetails = User.withUsername(username)
                .password("")
                .authorities("USER")
                .build();
        String newToken = jwtService.generateToken(userDetails);
        return AuthResponse.builder()
                .token(newToken)
                .username(username)
                .build();
    }
}
