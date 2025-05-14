package com.task_management_system.auth_service.service;

import com.task_management_system.auth_service.dto.AuthResponse;
import com.task_management_system.auth_service.dto.LoginRequest;
import com.task_management_system.auth_service.exception.InvalidCredentialsException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final SecretKey jwtSecretKey;
    private final long jwtExpirationMinutes;

    public AuthService(
            PasswordEncoder passwordEncoder,
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expiration-minutes}") long jwtExpirationMinutes) {
        this.passwordEncoder = passwordEncoder;
        this.jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtExpirationMinutes = jwtExpirationMinutes;
    }

    public AuthResponse authenticate(LoginRequest request) {
        // 1. Validación básica
        if (request.getUsername() == null || request.getUsername().isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank()) {
            throw new InvalidCredentialsException("Username and password are required");
        }

        // 2. Lógica de autenticación simulada (deberías reemplazar con tu lógica real)
        if (!isValidUser(request.getUsername(), request.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // 3. Generación de JWT
        String token = generateJwtToken(request.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(request.getUsername())
                .build();
    }

    private boolean isValidUser(String username, String password) {
        // Implementación temporal - reemplazar con consulta a BD
        String storedPassword = passwordEncoder.encode("password123"); // Contraseña de ejemplo
        return "admin".equals(username) &&
                passwordEncoder.matches(password, storedPassword);
    }

    private String generateJwtToken(String username) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(jwtExpirationMinutes, ChronoUnit.MINUTES)))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}