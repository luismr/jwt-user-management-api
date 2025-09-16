package com.example.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for login requests.
 * Supports both username and email-based authentication.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login request containing user credentials")
public class LoginRequest {

    @NotBlank(message = "Username or email is required")
    @Size(max = 128, message = "Username or email must not exceed 128 characters")
    @Schema(description = "Username or email for authentication", example = "admin", maxLength = 128)
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 1, max = 255, message = "Password must be between 1 and 255 characters")
    @Schema(description = "Password for authentication", example = "admin123", maxLength = 255)
    private String password;

    @Builder.Default
    @Schema(description = "Whether to remember the login session", example = "false")
    private Boolean rememberMe = false;
}
