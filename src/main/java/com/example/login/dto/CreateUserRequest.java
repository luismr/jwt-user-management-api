package com.example.login.dto;

import com.example.login.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user creation requests.
 * Contains user information needed to create a new user account.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new user account")
public class CreateUserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 128, message = "Username must be between 3 and 128 characters")
    @Schema(description = "Unique username for the user", example = "johndoe", minLength = 3, maxLength = 128)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    @Schema(description = "Password for the user account", example = "SecurePass123!", minLength = 8, maxLength = 255)
    private String password;

    @NotBlank(message = "Name is required")
    @Size(max = 128, message = "Name must not exceed 128 characters")
    @Schema(description = "Full name of the user", example = "John Doe", maxLength = 128)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 128, message = "Email must not exceed 128 characters")
    @Schema(description = "Email address of the user", example = "john.doe@example.com", maxLength = 128, format = "email")
    private String email;

    @NotNull(message = "Client ID is required")
    @Schema(description = "ID of the client this user belongs to", example = "1")
    private Long idClient;

    @Schema(description = "Password hashing algorithm type", example = "BCRYPT", allowableValues = {"BCRYPT", "SHA256", "MD5", "SHA512"})
    private String passwordType;

    @Schema(description = "Initial status of the user account", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED"})
    private String status;

    /**
     * Get the password type as enum, defaulting to MD5 if not specified.
     * 
     * @return PasswordType enum value
     */
    public User.PasswordType getPasswordTypeEnum() {
        if (passwordType == null || passwordType.trim().isEmpty()) {
            return User.PasswordType.MD5;
        }
        return User.PasswordType.fromString(passwordType);
    }

    /**
     * Get the status as enum, defaulting to ACTIVE if not specified.
     * 
     * @return UserStatus enum value
     */
    public User.UserStatus getStatusEnum() {
        if (status == null || status.trim().isEmpty()) {
            return User.UserStatus.ACTIVE;
        }
        try {
            return User.UserStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return User.UserStatus.ACTIVE;
        }
    }
}
