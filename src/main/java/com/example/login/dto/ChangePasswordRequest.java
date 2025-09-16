package com.example.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for password change requests.
 * Contains old password, new password, and confirmation for secure password updates.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to change user password")
public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required")
    @Size(min = 1, max = 255, message = "Current password must be between 1 and 255 characters")
    @Schema(description = "Current password for verification", example = "OldPassword123!", maxLength = 255)
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 255, message = "New password must be between 8 and 255 characters")
    @Schema(description = "New password for the account", example = "NewSecurePass123!", minLength = 8, maxLength = 255)
    private String newPassword;

    @NotBlank(message = "Password confirmation is required")
    @Size(min = 8, max = 255, message = "Password confirmation must be between 8 and 255 characters")
    @Schema(description = "Confirmation of the new password", example = "NewSecurePass123!", minLength = 8, maxLength = 255)
    private String confirmPassword;

    /**
     * Validate that new password and confirmation match.
     * 
     * @return true if passwords match, false otherwise
     */
    public boolean isPasswordConfirmationValid() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }

    /**
     * Check if the new password is different from the current password.
     * 
     * @return true if passwords are different, false otherwise
     */
    public boolean isNewPasswordDifferent() {
        return currentPassword != null && newPassword != null && !currentPassword.equals(newPassword);
    }
}
