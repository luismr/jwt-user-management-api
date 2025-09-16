package com.example.login.service;

import com.example.login.entity.User.PasswordType;
import com.example.login.util.PasswordHashUtil.PasswordHashResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PasswordService Tests")
class PasswordServiceTest {

    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        passwordService = new PasswordService();
    }

    @Test
    @DisplayName("Should generate hash with string type")
    void shouldGenerateHashWithStringType() {
        // When
        PasswordHashResult result = passwordService.generateHash("MD5", "testpassword");

        // Then
        assertNotNull(result);
        assertNotNull(result.getSalt());
        assertNotNull(result.getHash());
        assertFalse(result.getSalt().isEmpty());
        assertFalse(result.getHash().isEmpty());
    }

    @Test
    @DisplayName("Should generate hash with enum type")
    void shouldGenerateHashWithEnumType() {
        // When
        PasswordHashResult result = passwordService.generateHash(PasswordType.SHA256, "testpassword");

        // Then
        assertNotNull(result);
        assertNotNull(result.getSalt());
        assertNotNull(result.getHash());
        assertFalse(result.getSalt().isEmpty());
        assertFalse(result.getHash().isEmpty());
    }

    @Test
    @DisplayName("Should generate hash with salt using string type")
    void shouldGenerateHashWithSaltUsingStringType() {
        // Given
        String salt = "abcdef1234567890abcdef1234567890";

        // When
        String hash = passwordService.generateHash("SHA256", salt, "testpassword");

        // Then
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    @DisplayName("Should generate hash with salt using enum type")
    void shouldGenerateHashWithSaltUsingEnumType() {
        // Given
        String salt = "abcdef1234567890abcdef1234567890";

        // When
        String hash = passwordService.generateHash(PasswordType.SHA256, salt, "testpassword");

        // Then
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    @DisplayName("Should validate password with string type")
    void shouldValidatePasswordWithStringType() {
        // Given
        String password = "testpassword";
        PasswordHashResult result = passwordService.generateHash("SHA256", password);

        // When
        boolean isValid = passwordService.isValid("SHA256", result.getSalt(), password, result.getHash());

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should validate password with enum type")
    void shouldValidatePasswordWithEnumType() {
        // Given
        String password = "testpassword";
        PasswordHashResult result = passwordService.generateHash(PasswordType.SHA256, password);

        // When
        boolean isValid = passwordService.isValid(PasswordType.SHA256, result.getSalt(), password, result.getHash());

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should reject incorrect password")
    void shouldRejectIncorrectPassword() {
        // Given
        String password = "testpassword";
        PasswordHashResult result = passwordService.generateHash(PasswordType.SHA256, password);

        // When
        boolean isValid = passwordService.isValid(PasswordType.SHA256, result.getSalt(), "wrongpassword", result.getHash());

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should generate salt with string type")
    void shouldGenerateSaltWithStringType() {
        // When
        String salt = passwordService.generateSalt("SHA256");

        // Then
        assertNotNull(salt);
        assertFalse(salt.isEmpty());
    }

    @Test
    @DisplayName("Should generate salt with enum type")
    void shouldGenerateSaltWithEnumType() {
        // When
        String salt = passwordService.generateSalt(PasswordType.SHA256);

        // Then
        assertNotNull(salt);
        assertFalse(salt.isEmpty());
    }

    @Test
    @DisplayName("Should get default password type")
    void shouldGetDefaultPasswordType() {
        // When
        PasswordType defaultType = passwordService.getDefaultPasswordType();

        // Then
        assertEquals(PasswordType.MD5, defaultType);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Password123!", "MySecure@Pass1", "Test#Password2"})
    @DisplayName("Should identify secure passwords")
    void shouldIdentifySecurePasswords(String password) {
        // When
        boolean isSecure = passwordService.isPasswordSecure(password);

        // Then
        assertTrue(isSecure);
    }

    @ParameterizedTest
    @ValueSource(strings = {"short", "nouppercase123!", "NOLOWERCASE123!", "NoNumbers!", "NoSpecialChars123"})
    @DisplayName("Should identify insecure passwords")
    void shouldIdentifyInsecurePasswords(String password) {
        // When
        boolean isSecure = passwordService.isPasswordSecure(password);

        // Then
        assertFalse(isSecure);
    }

    @Test
    @DisplayName("Should reject null password in generateHash")
    void shouldRejectNullPasswordInGenerateHash() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            passwordService.generateHash(PasswordType.SHA256, null));
    }

    @Test
    @DisplayName("Should reject empty password in generateHash")
    void shouldRejectEmptyPasswordInGenerateHash() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            passwordService.generateHash(PasswordType.SHA256, ""));
    }

    @Test
    @DisplayName("Should reject whitespace-only password in generateHash")
    void shouldRejectWhitespaceOnlyPasswordInGenerateHash() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            passwordService.generateHash(PasswordType.SHA256, "   "));
    }

    @Test
    @DisplayName("Should reject null salt in generateHash with salt")
    void shouldRejectNullSaltInGenerateHashWithSalt() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            passwordService.generateHash(PasswordType.SHA256, null, "testpassword"));
    }

    @Test
    @DisplayName("Should reject empty salt in generateHash with salt")
    void shouldRejectEmptySaltInGenerateHashWithSalt() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            passwordService.generateHash(PasswordType.SHA256, "", "testpassword"));
    }

    @Test
    @DisplayName("Should reject null password in generateHash with salt")
    void shouldRejectNullPasswordInGenerateHashWithSalt() {
        // Given
        String salt = "abcdef1234567890abcdef1234567890";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            passwordService.generateHash(PasswordType.SHA256, salt, null));
    }

    @Test
    @DisplayName("Should reject empty password in generateHash with salt")
    void shouldRejectEmptyPasswordInGenerateHashWithSalt() {
        // Given
        String salt = "abcdef1234567890abcdef1234567890";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            passwordService.generateHash(PasswordType.SHA256, salt, ""));
    }

    @Test
    @DisplayName("Should return false for null password in isValid")
    void shouldReturnFalseForNullPasswordInIsValid() {
        // Given
        String salt = "abcdef1234567890abcdef1234567890";
        String hash = "somehash";

        // When
        boolean isValid = passwordService.isValid(PasswordType.SHA256, salt, null, hash);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false for empty password in isValid")
    void shouldReturnFalseForEmptyPasswordInIsValid() {
        // Given
        String salt = "abcdef1234567890abcdef1234567890";
        String hash = "somehash";

        // When
        boolean isValid = passwordService.isValid(PasswordType.SHA256, salt, "", hash);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false for null salt in isValid")
    void shouldReturnFalseForNullSaltInIsValid() {
        // Given
        String hash = "somehash";

        // When
        boolean isValid = passwordService.isValid(PasswordType.SHA256, null, "testpassword", hash);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false for null hash in isValid")
    void shouldReturnFalseForNullHashInIsValid() {
        // Given
        String salt = "abcdef1234567890abcdef1234567890";

        // When
        boolean isValid = passwordService.isValid(PasswordType.SHA256, salt, "testpassword", null);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should handle case-insensitive password type strings")
    void shouldHandleCaseInsensitivePasswordTypeStrings() {
        // When
        PasswordHashResult result1 = passwordService.generateHash("md5", "testpassword");
        PasswordHashResult result2 = passwordService.generateHash("MD5", "testpassword");
        PasswordHashResult result3 = passwordService.generateHash("Md5", "testpassword");

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
    }

    @Test
    @DisplayName("Should throw exception for invalid password type string")
    void shouldThrowExceptionForInvalidPasswordTypeString() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            passwordService.generateHash("INVALID", "testpassword"));
    }
}
