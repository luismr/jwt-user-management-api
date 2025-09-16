package com.example.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
@Tag(name = "Authentication", description = "Authentication and logout endpoints")
public class AuthController {

    @GetMapping("/login")
    @Operation(summary = "Get login information", description = "Returns information about how to authenticate with the API")
    public ResponseEntity<Map<String, String>> login() {
        return ResponseEntity.ok(Map.of(
            "message", "Please provide credentials for authentication",
            "endpoints", "Use /api/** with Basic Auth (username: admin, password: admin123)"
        ));
    }

    @PostMapping("/login")
    @Operation(summary = "Perform login", description = "Authenticates a user and returns login status")
    public ResponseEntity<Map<String, String>> loginPost() {
        return ResponseEntity.ok(Map.of(
            "message", "Login successful",
            "status", "authenticated"
        ));
    }

    @PostMapping("/logout")
    @Operation(summary = "Perform logout", description = "Logs out the current user")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of(
            "message", "Logout successful",
            "status", "logged_out"
        ));
    }

    @GetMapping("/logout")
    @Operation(summary = "Get logout information", description = "Returns logout status information")
    public ResponseEntity<Map<String, String>> logoutGet() {
        return ResponseEntity.ok(Map.of(
            "message", "Logout successful",
            "status", "logged_out"
        ));
    }
}
