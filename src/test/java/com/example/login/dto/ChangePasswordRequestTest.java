package com.example.login.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChangePasswordRequestTest {

    private ChangePasswordRequest request;

    @BeforeEach
    void setUp() {
        request = ChangePasswordRequest.builder()
                .currentPassword("OldPassword123!")
                .newPassword("NewPassword123!")
                .confirmPassword("NewPassword123!")
                .build();
    }

    @Test
    void isPasswordConfirmationValid_WhenPasswordsMatch_ReturnsTrue() {
        // Arrange
        request.setNewPassword("NewPassword123!");
        request.setConfirmPassword("NewPassword123!");

        // Act
        boolean result = request.isPasswordConfirmationValid();

        // Assert
        assertTrue(result);
    }

    @Test
    void isPasswordConfirmationValid_WhenPasswordsDoNotMatch_ReturnsFalse() {
        // Arrange
        request.setNewPassword("NewPassword123!");
        request.setConfirmPassword("DifferentPassword123!");

        // Act
        boolean result = request.isPasswordConfirmationValid();

        // Assert
        assertFalse(result);
    }

    @Test
    void isPasswordConfirmationValid_WhenNewPasswordIsNull_ReturnsFalse() {
        // Arrange
        request.setNewPassword(null);
        request.setConfirmPassword("SomePassword123!");

        // Act
        boolean result = request.isPasswordConfirmationValid();

        // Assert
        assertFalse(result);
    }

    @Test
    void isPasswordConfirmationValid_WhenConfirmPasswordIsNull_ReturnsFalse() {
        // Arrange
        request.setNewPassword("NewPassword123!");
        request.setConfirmPassword(null);

        // Act
        boolean result = request.isPasswordConfirmationValid();

        // Assert
        assertFalse(result);
    }

    @Test
    void isPasswordConfirmationValid_WhenBothPasswordsAreNull_ReturnsFalse() {
        // Arrange
        request.setNewPassword(null);
        request.setConfirmPassword(null);

        // Act
        boolean result = request.isPasswordConfirmationValid();

        // Assert
        assertFalse(result);
    }

    @Test
    void isNewPasswordDifferent_WhenPasswordsAreDifferent_ReturnsTrue() {
        // Arrange
        request.setCurrentPassword("OldPassword123!");
        request.setNewPassword("NewPassword123!");

        // Act
        boolean result = request.isNewPasswordDifferent();

        // Assert
        assertTrue(result);
    }

    @Test
    void isNewPasswordDifferent_WhenPasswordsAreSame_ReturnsFalse() {
        // Arrange
        request.setCurrentPassword("SamePassword123!");
        request.setNewPassword("SamePassword123!");

        // Act
        boolean result = request.isNewPasswordDifferent();

        // Assert
        assertFalse(result);
    }

    @Test
    void isNewPasswordDifferent_WhenCurrentPasswordIsNull_ReturnsFalse() {
        // Arrange
        request.setCurrentPassword(null);
        request.setNewPassword("SomePassword123!");

        // Act
        boolean result = request.isNewPasswordDifferent();

        // Assert
        assertFalse(result);
    }

    @Test
    void isNewPasswordDifferent_WhenNewPasswordIsNull_ReturnsFalse() {
        // Arrange
        request.setCurrentPassword("SomePassword123!");
        request.setNewPassword(null);

        // Act
        boolean result = request.isNewPasswordDifferent();

        // Assert
        assertFalse(result);
    }

    @Test
    void isNewPasswordDifferent_WhenBothPasswordsAreNull_ReturnsFalse() {
        // Arrange
        request.setCurrentPassword(null);
        request.setNewPassword(null);

        // Act
        boolean result = request.isNewPasswordDifferent();

        // Assert
        assertFalse(result);
    }

    @Test
    void builder_CreatesValidRequest() {
        // Act
        ChangePasswordRequest builtRequest = ChangePasswordRequest.builder()
                .currentPassword("OldPassword123!")
                .newPassword("NewPassword123!")
                .confirmPassword("NewPassword123!")
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals("OldPassword123!", builtRequest.getCurrentPassword());
        assertEquals("NewPassword123!", builtRequest.getNewPassword());
        assertEquals("NewPassword123!", builtRequest.getConfirmPassword());
    }

    @Test
    void noArgsConstructor_CreatesEmptyRequest() {
        // Act
        ChangePasswordRequest emptyRequest = new ChangePasswordRequest();

        // Assert
        assertNotNull(emptyRequest);
        assertNull(emptyRequest.getCurrentPassword());
        assertNull(emptyRequest.getNewPassword());
        assertNull(emptyRequest.getConfirmPassword());
    }

    @Test
    void allArgsConstructor_CreatesRequestWithAllFields() {
        // Act
        ChangePasswordRequest requestWithAllFields = new ChangePasswordRequest(
                "OldPassword123!",
                "NewPassword123!",
                "NewPassword123!"
        );

        // Assert
        assertNotNull(requestWithAllFields);
        assertEquals("OldPassword123!", requestWithAllFields.getCurrentPassword());
        assertEquals("NewPassword123!", requestWithAllFields.getNewPassword());
        assertEquals("NewPassword123!", requestWithAllFields.getConfirmPassword());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        // Arrange
        ChangePasswordRequest request = new ChangePasswordRequest();

        // Act
        request.setCurrentPassword("TestCurrent123!");
        request.setNewPassword("TestNew123!");
        request.setConfirmPassword("TestConfirm123!");

        // Assert
        assertEquals("TestCurrent123!", request.getCurrentPassword());
        assertEquals("TestNew123!", request.getNewPassword());
        assertEquals("TestConfirm123!", request.getConfirmPassword());
    }

    @Test
    void toString_ContainsAllFields() {
        // Act
        String toString = request.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("OldPassword123!"));
        assertTrue(toString.contains("NewPassword123!"));
    }

    @Test
    void equals_WhenSameValues_ReturnsTrue() {
        // Arrange
        ChangePasswordRequest request1 = ChangePasswordRequest.builder()
                .currentPassword("OldPassword123!")
                .newPassword("NewPassword123!")
                .confirmPassword("NewPassword123!")
                .build();

        ChangePasswordRequest request2 = ChangePasswordRequest.builder()
                .currentPassword("OldPassword123!")
                .newPassword("NewPassword123!")
                .confirmPassword("NewPassword123!")
                .build();

        // Act & Assert
        assertEquals(request1, request2);
    }

    @Test
    void equals_WhenDifferentValues_ReturnsFalse() {
        // Arrange
        ChangePasswordRequest request1 = ChangePasswordRequest.builder()
                .currentPassword("OldPassword123!")
                .newPassword("NewPassword123!")
                .confirmPassword("NewPassword123!")
                .build();

        ChangePasswordRequest request2 = ChangePasswordRequest.builder()
                .currentPassword("DifferentPassword123!")
                .newPassword("NewPassword123!")
                .confirmPassword("NewPassword123!")
                .build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void hashCode_WhenSameValues_ReturnsSameHashCode() {
        // Arrange
        ChangePasswordRequest request1 = ChangePasswordRequest.builder()
                .currentPassword("OldPassword123!")
                .newPassword("NewPassword123!")
                .confirmPassword("NewPassword123!")
                .build();

        ChangePasswordRequest request2 = ChangePasswordRequest.builder()
                .currentPassword("OldPassword123!")
                .newPassword("NewPassword123!")
                .confirmPassword("NewPassword123!")
                .build();

        // Act & Assert
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}
