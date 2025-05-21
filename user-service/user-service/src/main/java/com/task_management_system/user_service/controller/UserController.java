package com.task_management_system.user_service.controller;

import com.task_management_system.user_service.model.Role;
import com.task_management_system.user_service.model.User;
import com.task_management_system.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> userOpt = username != null
                ? userService.findByUsername(username)
                : (email != null ? userService.findByEmail(email) : Optional.empty());

        Map<String, Object> response = new java.util.HashMap<>();
        if (userOpt.isPresent() && password != null && userService.checkPassword(password, userOpt.get().getPassword())) {
            User user = userOpt.get();
            response.put("username", user.getUsername());
            response.put("role", user.getRole() != null ? user.getRole().name() : "USER");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User userRequest) {
        try {
            User user = userService.createUser(
                    userRequest.getUsername(),
                    userRequest.getEmail(),
                    userRequest.getPassword(),
                    userRequest.getRole() != null ? userRequest.getRole() : Role.USER
            );
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
