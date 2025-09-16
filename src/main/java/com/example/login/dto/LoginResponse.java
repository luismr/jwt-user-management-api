package com.example.login.dto;

import com.example.login.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for login responses.
 * Contains user information and authentication status.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login response containing user information and authentication status")
public class LoginResponse {

    @Schema(description = "Whether the login was successful", example = "true")
    private boolean success;

    @Schema(description = "Response message", example = "Login successful")
    private String message;

    @Schema(description = "User information if login successful")
    private UserInfo user;

    @Schema(description = "Authentication token (if applicable)", example = "jwt-token-here")
    private String token;

    @Schema(description = "Timestamp of the login", example = "2023-10-15T10:30:00")
    private LocalDateTime timestamp;

    /**
     * User information DTO for login response.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "User information in login response")
    public static class UserInfo {
        
        @Schema(description = "User ID", example = "1")
        private Long id;

        @Schema(description = "Username", example = "admin")
        private String username;

        @Schema(description = "User's full name", example = "John Doe")
        private String name;

        @Schema(description = "User's email", example = "john.doe@example.com")
        private String email;

        @Schema(description = "User's status", example = "ACTIVE")
        private String status;

        @Schema(description = "Client ID", example = "1")
        private Long clientId;

        @Schema(description = "Last login timestamp", example = "2023-10-15T10:30:00")
        private LocalDateTime lastLogin;

        @Schema(description = "Account creation timestamp", example = "2023-10-15T10:30:00")
        private LocalDateTime dateCreated;

        /**
         * Create UserInfo from User entity.
         * 
         * @param user the User entity
         * @return UserInfo DTO
         */
        public static UserInfo fromUser(User user) {
            if (user == null) {
                return null;
            }
            
            return UserInfo.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .email(user.getEmail())
                    .status(user.getStatus() != null ? user.getStatus().name() : null)
                    .clientId(user.getIdClient())
                    .lastLogin(user.getDateLastLogin())
                    .dateCreated(user.getDateCreated())
                    .build();
        }
    }

    /**
     * Create a successful login response.
     * 
     * @param user the authenticated user
     * @param token optional authentication token
     * @return LoginResponse
     */
    public static LoginResponse success(User user, String token) {
        return LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .user(UserInfo.fromUser(user))
                .token(token)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create a successful login response without token.
     * 
     * @param user the authenticated user
     * @return LoginResponse
     */
    public static LoginResponse success(User user) {
        return success(user, null);
    }

    /**
     * Create a failed login response.
     * 
     * @param message the failure message
     * @return LoginResponse
     */
    public static LoginResponse failure(String message) {
        return LoginResponse.builder()
                .success(false)
                .message(message)
                .user(null)
                .token(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
