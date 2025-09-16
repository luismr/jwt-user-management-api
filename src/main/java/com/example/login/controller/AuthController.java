package com.example.login.controller;

import com.example.login.dto.ChangePasswordRequest;
import com.example.login.dto.CreateUserRequest;
import com.example.login.dto.LoginRequest;
import com.example.login.dto.LoginResponse;
import com.example.login.entity.User;
import com.example.login.exception.AuthenticationException;
import com.example.login.repository.UserRepository;
import com.example.login.service.JwtService;
import com.example.login.service.PasswordService;
import com.example.login.service.UserLookupService;
import com.example.login.util.PasswordHashUtil.PasswordHashResult;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final PasswordService passwordService;
    private final UserRepository userRepository;

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

    @PostMapping("/api/users/create")
    @Operation(summary = "Create a new user", description = "Creates a new user account with hashed password. Requires ADMIN role.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data or user already exists",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        try {
            log.info("Creating new user: {}", createUserRequest.getUsername());
            
            // Check if username already exists
            if (userRepository.existsByUsername(createUserRequest.getUsername())) {
                log.warn("Username already exists: {}", createUserRequest.getUsername());
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username already exists", "field", "username"));
            }
            
            // Check if email already exists
            if (userRepository.existsByEmail(createUserRequest.getEmail())) {
                log.warn("Email already exists: {}", createUserRequest.getEmail());
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email already exists", "field", "email"));
            }
            
            // Validate password security if needed
            if (!passwordService.isPasswordSecure(createUserRequest.getPassword())) {
                log.warn("Password does not meet security requirements for user: {}", createUserRequest.getUsername());
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Password does not meet security requirements. Must be at least 8 characters with uppercase, lowercase, digit, and special character", "field", "password"));
            }
            
            // Generate password hash using PasswordService
            User.PasswordType passwordType = createUserRequest.getPasswordTypeEnum();
            PasswordHashResult hashResult = passwordService.generateHash(passwordType, createUserRequest.getPassword());
            
            // Create new user entity
            User newUser = User.builder()
                .username(createUserRequest.getUsername())
                .passwordHash(hashResult.getHash())
                .passwordSalt(hashResult.getSalt())
                .passwordType(passwordType)
                .name(createUserRequest.getName())
                .email(createUserRequest.getEmail())
                .idClient(createUserRequest.getIdClient())
                .status(createUserRequest.getStatusEnum())
                .build();
            
            // Save user to database
            User savedUser = userRepository.save(newUser);
            
            // Clear sensitive data before returning
            savedUser.setPasswordHash(null);
            savedUser.setPasswordSalt(null);
            
            log.info("User created successfully: {} with ID: {}", savedUser.getUsername(), savedUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid request data for user creation: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating user: {}", createUserRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An error occurred while creating the user"));
        }
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change user password", description = "Changes the password for the authenticated user")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed successfully",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data or password validation failed",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            // Get the current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("Unauthenticated user attempted to change password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required"));
            }

            String username = authentication.getName();
            log.info("Password change attempt for user: {}", username);

            // Find the user
            Optional<User> userOpt = userLookupService.findByUsername(username);
            if (userOpt.isEmpty()) {
                log.warn("User not found for password change: {}", username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
            }

            User user = userOpt.get();

            // Validate password confirmation
            if (!changePasswordRequest.isPasswordConfirmationValid()) {
                log.warn("Password confirmation mismatch for user: {}", username);
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "New password and confirmation do not match", "field", "confirmPassword"));
            }

            // Check if new password is different from current
            if (!changePasswordRequest.isNewPasswordDifferent()) {
                log.warn("New password same as current password for user: {}", username);
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "New password must be different from current password", "field", "newPassword"));
            }

            // Validate current password
            boolean isCurrentPasswordValid = userLookupService.validatePassword(user, changePasswordRequest.getCurrentPassword());
            if (!isCurrentPasswordValid) {
                log.warn("Invalid current password for user: {}", username);
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Current password is incorrect", "field", "currentPassword"));
            }

            // Validate new password security
            if (!passwordService.isPasswordSecure(changePasswordRequest.getNewPassword())) {
                log.warn("New password does not meet security requirements for user: {}", username);
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "New password does not meet security requirements. Must be at least 8 characters with uppercase, lowercase, digit, and special character", "field", "newPassword"));
            }

            // Generate new password hash using the same algorithm as the current password
            User.PasswordType passwordType = user.getPasswordType();
            PasswordHashResult hashResult = passwordService.generateHash(passwordType, changePasswordRequest.getNewPassword());

            // Update user password
            user.setPasswordHash(hashResult.getHash());
            user.setPasswordSalt(hashResult.getSalt());
            
            // Save updated user
            userRepository.save(user);

            log.info("Password changed successfully for user: {}", username);
            return ResponseEntity.ok(Map.of(
                "message", "Password changed successfully",
                "username", username
            ));

        } catch (IllegalArgumentException e) {
            log.error("Invalid request data for password change: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error changing password for user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An error occurred while changing the password"));
        }
    }
}
