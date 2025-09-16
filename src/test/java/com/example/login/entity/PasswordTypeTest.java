package com.example.login.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PasswordType Tests")
class PasswordTypeTest {

    @Test
    @DisplayName("Should parse valid password types case-insensitively")
    void shouldParseValidPasswordTypesCaseInsensitively() {
        // Test all valid cases
        assertEquals(User.PasswordType.BCRYPT, User.PasswordType.fromString("BCRYPT"));
        assertEquals(User.PasswordType.BCRYPT, User.PasswordType.fromString("bcrypt"));
        assertEquals(User.PasswordType.BCRYPT, User.PasswordType.fromString("Bcrypt"));
        assertEquals(User.PasswordType.BCRYPT, User.PasswordType.fromString("bCrYpT"));

        assertEquals(User.PasswordType.SHA256, User.PasswordType.fromString("SHA256"));
        assertEquals(User.PasswordType.SHA256, User.PasswordType.fromString("sha256"));
        assertEquals(User.PasswordType.SHA256, User.PasswordType.fromString("Sha256"));

        assertEquals(User.PasswordType.MD5, User.PasswordType.fromString("MD5"));
        assertEquals(User.PasswordType.MD5, User.PasswordType.fromString("md5"));
        assertEquals(User.PasswordType.MD5, User.PasswordType.fromString("Md5"));

        assertEquals(User.PasswordType.SHA512, User.PasswordType.fromString("SHA512"));
        assertEquals(User.PasswordType.SHA512, User.PasswordType.fromString("sha512"));
        assertEquals(User.PasswordType.SHA512, User.PasswordType.fromString("Sha512"));
    }

    @Test
    @DisplayName("Should throw exception for null input")
    void shouldThrowExceptionForNullInput() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            User.PasswordType.fromString(null));
        
        assertEquals("Password type cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "INVALID", "UNKNOWN", "SHA1", "SHA384", "bcrypt2", "md4"})
    @DisplayName("Should throw exception for invalid password types")
    void shouldThrowExceptionForInvalidPasswordTypes(String invalidType) {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            User.PasswordType.fromString(invalidType));
        
        assertTrue(exception.getMessage().contains("Unknown password type: " + invalidType));
        assertTrue(exception.getMessage().contains("Supported types:"));
    }

    @Test
    @DisplayName("Should include all supported types in error message")
    void shouldIncludeAllSupportedTypesInErrorMessage() {
        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            User.PasswordType.fromString("INVALID"));

        // Then
        String message = exception.getMessage();
        assertTrue(message.contains("BCRYPT"));
        assertTrue(message.contains("SHA256"));
        assertTrue(message.contains("MD5"));
        assertTrue(message.contains("SHA512"));
    }

    @Test
    @DisplayName("Should handle whitespace in input")
    void shouldHandleWhitespaceInInput() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            User.PasswordType.fromString(" BCRYPT "));
        assertThrows(IllegalArgumentException.class, () -> 
            User.PasswordType.fromString(" BCRYPT"));
        assertThrows(IllegalArgumentException.class, () -> 
            User.PasswordType.fromString("BCRYPT "));
    }
}
