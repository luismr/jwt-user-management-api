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

    @Test
    void hashCode_WhenDifferentValues_ReturnsDifferentHashCode() {
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
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void equals_WhenSameObject_ReturnsTrue() {
        // Act & Assert
        assertEquals(request, request);
    }

    @Test
    void equals_WhenOneIsNull_ReturnsFalse() {
        // Arrange
        ChangePasswordRequest request1 = ChangePasswordRequest.builder()
                .currentPassword("OldPassword123!")
                .newPassword("NewPassword123!")
                .confirmPassword("NewPassword123!")
                .build();

        ChangePasswordRequest request2 = null;

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void equals_WhenDifferentClass_ReturnsFalse() {
        // Arrange
        ChangePasswordRequest request1 = ChangePasswordRequest.builder()
                .currentPassword("OldPassword123!")
                .newPassword("NewPassword123!")
                .confirmPassword("NewPassword123!")
                .build();

        String differentObject = "not a ChangePasswordRequest";

        // Act & Assert
        assertNotEquals(request1, differentObject);
    }

    @Test
    void isPasswordConfirmationValid_WhenEmptyStrings_ReturnsTrue() {
        // Arrange
        request.setNewPassword("");
        request.setConfirmPassword("");

        // Act
        boolean result = request.isPasswordConfirmationValid();

        // Assert
        assertTrue(result);
    }

    @Test
    void isPasswordConfirmationValid_WhenNewPasswordEmptyConfirmPasswordNotEmpty_ReturnsFalse() {
        // Arrange
        request.setNewPassword("");
        request.setConfirmPassword("SomePassword123!");

        // Act
        boolean result = request.isPasswordConfirmationValid();

        // Assert
        assertFalse(result);
    }

    @Test
    void isPasswordConfirmationValid_WhenNewPasswordNotEmptyConfirmPasswordEmpty_ReturnsFalse() {
        // Arrange
        request.setNewPassword("SomePassword123!");
        request.setConfirmPassword("");

        // Act
        boolean result = request.isPasswordConfirmationValid();

        // Assert
        assertFalse(result);
    }

    @Test
    void isNewPasswordDifferent_WhenEmptyStrings_ReturnsFalse() {
        // Arrange
        request.setCurrentPassword("");
        request.setNewPassword("");

        // Act
        boolean result = request.isNewPasswordDifferent();

        // Assert
        assertFalse(result);
    }

    @Test
    void isNewPasswordDifferent_WhenCurrentPasswordEmptyNewPasswordNotEmpty_ReturnsTrue() {
        // Arrange
        request.setCurrentPassword("");
        request.setNewPassword("SomePassword123!");

        // Act
        boolean result = request.isNewPasswordDifferent();

        // Assert
        assertTrue(result);
    }

    @Test
    void isNewPasswordDifferent_WhenCurrentPasswordNotEmptyNewPasswordEmpty_ReturnsTrue() {
        // Arrange
        request.setCurrentPassword("SomePassword123!");
        request.setNewPassword("");

        // Act
        boolean result = request.isNewPasswordDifferent();

        // Assert
        assertTrue(result);
    }

    @Test
    void builder_WithMinLengthPasswords_CreatesValidRequest() {
        // Act
        ChangePasswordRequest builtRequest = ChangePasswordRequest.builder()
                .currentPassword("a") // min length 1
                .newPassword("password") // min length 8
                .confirmPassword("password") // min length 8
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals("a", builtRequest.getCurrentPassword());
        assertEquals("password", builtRequest.getNewPassword());
        assertEquals("password", builtRequest.getConfirmPassword());
    }

    @Test
    void builder_WithMaxLengthPasswords_CreatesValidRequest() {
        // Arrange
        String maxLengthPassword = "a".repeat(255);

        // Act
        ChangePasswordRequest builtRequest = ChangePasswordRequest.builder()
                .currentPassword(maxLengthPassword)
                .newPassword(maxLengthPassword)
                .confirmPassword(maxLengthPassword)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals(maxLengthPassword, builtRequest.getCurrentPassword());
        assertEquals(maxLengthPassword, builtRequest.getNewPassword());
        assertEquals(maxLengthPassword, builtRequest.getConfirmPassword());
    }

    @Test
    void builder_WithEmptyStrings_CreatesRequestWithEmptyValues() {
        // Act
        ChangePasswordRequest builtRequest = ChangePasswordRequest.builder()
                .currentPassword("")
                .newPassword("")
                .confirmPassword("")
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals("", builtRequest.getCurrentPassword());
        assertEquals("", builtRequest.getNewPassword());
        assertEquals("", builtRequest.getConfirmPassword());
    }

    @Test
    void builder_WithSpecialCharacters_CreatesValidRequest() {
        // Arrange
        String specialPassword = "!@#$%^&*()_+-=[]{}|;':\",./<>?`~";

        // Act
        ChangePasswordRequest builtRequest = ChangePasswordRequest.builder()
                .currentPassword(specialPassword)
                .newPassword(specialPassword)
                .confirmPassword(specialPassword)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals(specialPassword, builtRequest.getCurrentPassword());
        assertEquals(specialPassword, builtRequest.getNewPassword());
        assertEquals(specialPassword, builtRequest.getConfirmPassword());
    }

    @Test
    void builder_WithUnicodeCharacters_CreatesValidRequest() {
        // Arrange
        String unicodePassword = "пароль123!@#";

        // Act
        ChangePasswordRequest builtRequest = ChangePasswordRequest.builder()
                .currentPassword(unicodePassword)
                .newPassword(unicodePassword)
                .confirmPassword(unicodePassword)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals(unicodePassword, builtRequest.getCurrentPassword());
        assertEquals(unicodePassword, builtRequest.getNewPassword());
        assertEquals(unicodePassword, builtRequest.getConfirmPassword());
    }

    @Test
    void builder_WithWhitespacePasswords_CreatesValidRequest() {
        // Arrange
        String whitespacePassword = "  password with spaces  ";

        // Act
        ChangePasswordRequest builtRequest = ChangePasswordRequest.builder()
                .currentPassword(whitespacePassword)
                .newPassword(whitespacePassword)
                .confirmPassword(whitespacePassword)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals(whitespacePassword, builtRequest.getCurrentPassword());
        assertEquals(whitespacePassword, builtRequest.getNewPassword());
        assertEquals(whitespacePassword, builtRequest.getConfirmPassword());
    }

    @Test
    void isPasswordConfirmationValid_WithWhitespaceDifferences_ReturnsFalse() {
        // Arrange
        request.setNewPassword("password123");
        request.setConfirmPassword(" password123 "); // different whitespace

        // Act
        boolean result = request.isPasswordConfirmationValid();

        // Assert
        assertFalse(result);
    }

    @Test
    void isNewPasswordDifferent_WithWhitespaceDifferences_ReturnsTrue() {
        // Arrange
        request.setCurrentPassword("password123");
        request.setNewPassword(" password123 "); // different whitespace

        // Act
        boolean result = request.isNewPasswordDifferent();

        // Assert
        assertTrue(result);
    }

    @Test
    void isPasswordConfirmationValid_WithCaseDifferences_ReturnsFalse() {
        // Arrange
        request.setNewPassword("Password123");
        request.setConfirmPassword("password123"); // different case

        // Act
        boolean result = request.isPasswordConfirmationValid();

        // Assert
        assertFalse(result);
    }

    @Test
    void isNewPasswordDifferent_WithCaseDifferences_ReturnsTrue() {
        // Arrange
        request.setCurrentPassword("Password123");
        request.setNewPassword("password123"); // different case

        // Act
        boolean result = request.isNewPasswordDifferent();

        // Assert
        assertTrue(result);
    }
}
