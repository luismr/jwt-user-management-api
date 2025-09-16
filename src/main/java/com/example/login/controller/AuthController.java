package com.example.login.controller;

import com.example.login.dto.LoginRequest;
import com.example.login.dto.LoginResponse;
import com.example.login.entity.User;
import com.example.login.exception.AuthenticationException;
import com.example.login.service.JwtService;
import com.example.login.service.UserLookupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping
@Tag(name = "Authentication", description = "Authentication and logout endpoints")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserLookupService userLookupService;
    private final JwtService jwtService;

    @GetMapping("/login")
    @Operation(summary = "Get login information", description = "Returns information about how to authenticate with the API")
    public ResponseEntity<Map<String, String>> login() {
        return ResponseEntity.ok(Map.of(
            "message", "Please provide credentials for authentication",
            "endpoints", "Use /api/** with Basic Auth (username: admin, password: admin123)"
        ));
    }

    @PostMapping("/login")
    @Operation(summary = "Perform login", description = "Authenticates a user with username/email and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "401", description = "Authentication failed",
            content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    })
    public ResponseEntity<LoginResponse> loginPost(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            log.info("Login attempt for: {}", loginRequest.getUsernameOrEmail());
            
            // Try to authenticate by username first, then by email
            Optional<User> userOpt = userLookupService.authenticateByUsername(
                loginRequest.getUsernameOrEmail(), 
                loginRequest.getPassword()
            );
            
            if (userOpt.isEmpty()) {
                userOpt = userLookupService.authenticateByEmail(
                    loginRequest.getUsernameOrEmail(), 
                    loginRequest.getPassword()
                );
            }
            
            if (userOpt.isEmpty()) {
                log.warn("Authentication failed for: {}", loginRequest.getUsernameOrEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(LoginResponse.failure("Invalid username/email or password"));
            }
            
            User user = userOpt.get();
            
            // Get user roles and client IDs for JWT
            List<String> roles = userLookupService.getUserRoles(user);
            List<Long> clientIds = userLookupService.getUserClientIds(user);
            
            // Generate JWT token
            String jwtToken = jwtService.generateToken(user, roles, clientIds);
            
            // Update last login timestamp
            userLookupService.updateLastLogin(user);
            
            log.info("Login successful for user: {} with roles: {}", user.getUsername(), roles);
            return ResponseEntity.ok(LoginResponse.success(user, jwtToken));
            
        } catch (Exception e) {
            log.error("Error during login for: {}", loginRequest.getUsernameOrEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(LoginResponse.failure("An error occurred during authentication"));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Perform logout", description = "Logs out the current user and invalidates JWT token")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout successful",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            jwtService.blacklistToken(jwt);
            log.info("JWT token invalidated for logout");
        }
        
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

    @GetMapping("/user/{username}")
    @Operation(summary = "Get user by username", description = "Retrieves user information by username")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> userOpt = userLookupService.findByUsername(username);
        return userOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/email/{email}")
    @Operation(summary = "Get user by email", description = "Retrieves user information by email")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> userOpt = userLookupService.findByEmail(email);
        return userOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/id/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves user information by ID")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userLookupService.findById(id);
        return userOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/validate-password")
    @Operation(summary = "Validate password", description = "Validates a password against a user's stored credentials")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password validation result",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Map<String, Object>> validatePassword(
            @RequestParam String usernameOrEmail,
            @RequestParam String password) {
        
        // Find user by username or email
        Optional<User> userOpt = userLookupService.findByUsername(usernameOrEmail);
        if (userOpt.isEmpty()) {
            userOpt = userLookupService.findByEmail(usernameOrEmail);
        }
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        User user = userOpt.get();
        boolean isValid = userLookupService.validatePassword(user, password);
        
        return ResponseEntity.ok(Map.of(
            "valid", isValid,
            "username", user.getUsername(),
            "message", isValid ? "Password is valid" : "Password is invalid"
        ));
    }
}
