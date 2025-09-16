package com.example.login.util;

import com.example.login.entity.User.PasswordType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PasswordHashUtil Tests")
class PasswordHashUtilTest {

    private static final String TEST_PASSWORD = "P@ssw0rd!";
    private static final String TEST_SALT = "abcdef1234567890abcdef1234567890";

    @Test
    @DisplayName("Should generate random salt hex of correct length")
    void shouldGenerateRandomSaltHex() {
        // Given
        int length = 16;

        // When
        String salt = PasswordHashUtil.generateRandomSaltHex(length);

        // Then
        assertNotNull(salt);
        assertEquals(length * 2, salt.length()); // Hex string is 2x the byte length
        assertTrue(salt.matches("[0-9a-fA-F]+"));
    }

    @Test
    @DisplayName("Should generate different salts on multiple calls")
    void shouldGenerateDifferentSalts() {
        // When
        String salt1 = PasswordHashUtil.generateRandomSaltHex(16);
        String salt2 = PasswordHashUtil.generateRandomSaltHex(16);

        // Then
        assertNotEquals(salt1, salt2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"MD5", "SHA256", "SHA512"})
    @DisplayName("Should generate hash for SHA algorithms")
    void shouldGenerateHashForSHAAlgorithms(String algorithm) {
        // Given
        PasswordType type = PasswordType.valueOf(algorithm);

        // When
        String hash = PasswordHashUtil.generateHash(type, TEST_SALT, TEST_PASSWORD);

        // Then
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
        assertTrue(hash.matches("[0-9a-fA-F]+"));
    }

    @Test
    @DisplayName("Should generate hash for BCRYPT")
    void shouldGenerateHashForBCrypt() {
        // Given
        PasswordType type = PasswordType.BCRYPT;
        String bcryptSalt = "$2a$10$abcdefghijklmnopqrstuv";

        // When
        String hash = PasswordHashUtil.generateHash(type, bcryptSalt, TEST_PASSWORD);

        // Then
        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$"));
    }

    @Test
    @DisplayName("Should validate correct password for SHA algorithms")
    void shouldValidateCorrectPasswordForSHAAlgorithms() {
        // Given
        PasswordType type = PasswordType.SHA256;
        String hash = PasswordHashUtil.generateHash(type, TEST_SALT, TEST_PASSWORD);

        // When
        boolean isValid = PasswordHashUtil.isValid(type, TEST_SALT, TEST_PASSWORD, hash);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should validate correct password for BCRYPT")
    void shouldValidateCorrectPasswordForBCrypt() {
        // Given
        PasswordType type = PasswordType.BCRYPT;
        String bcryptSalt = "$2a$10$abcdefghijklmnopqrstuv";
        String hash = PasswordHashUtil.generateHash(type, bcryptSalt, TEST_PASSWORD);

        // When
        boolean isValid = PasswordHashUtil.isValid(type, bcryptSalt, TEST_PASSWORD, hash);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should reject incorrect password")
    void shouldRejectIncorrectPassword() {
        // Given
        PasswordType type = PasswordType.SHA256;
        String hash = PasswordHashUtil.generateHash(type, TEST_SALT, TEST_PASSWORD);

        // When
        boolean isValid = PasswordHashUtil.isValid(type, TEST_SALT, "wrongpassword", hash);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should reject null password")
    void shouldRejectNullPassword() {
        // Given
        PasswordType type = PasswordType.SHA256;
        String hash = PasswordHashUtil.generateHash(type, TEST_SALT, TEST_PASSWORD);

        // When
        boolean isValid = PasswordHashUtil.isValid(type, TEST_SALT, null, hash);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should reject empty password")
    void shouldRejectEmptyPassword() {
        // Given
        PasswordType type = PasswordType.SHA256;
        String hash = PasswordHashUtil.generateHash(type, TEST_SALT, TEST_PASSWORD);

        // When
        boolean isValid = PasswordHashUtil.isValid(type, TEST_SALT, "", hash);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should generate appropriate salt for each algorithm")
    void shouldGenerateAppropriateSaltForEachAlgorithm() {
        // When & Then
        String bcryptSalt = PasswordHashUtil.generateSalt(PasswordType.BCRYPT);
        assertTrue(bcryptSalt.startsWith("$2a$"));

        String shaSalt = PasswordHashUtil.generateSalt(PasswordType.SHA256);
        assertEquals(32, shaSalt.length()); // 16 bytes = 32 hex chars
        assertTrue(shaSalt.matches("[0-9a-fA-F]+"));
    }

    @Test
    @DisplayName("Should generate hash with salt")
    void shouldGenerateHashWithSalt() {
        // When
        PasswordHashUtil.PasswordHashResult result = PasswordHashUtil.generateHashWithSalt(PasswordType.SHA256, TEST_PASSWORD);

        // Then
        assertNotNull(result);
        assertNotNull(result.getSalt());
        assertNotNull(result.getHash());
        assertFalse(result.getSalt().isEmpty());
        assertFalse(result.getHash().isEmpty());
    }

    @Test
    @DisplayName("Should throw exception for null password in generateHash")
    void shouldThrowExceptionForNullPasswordInGenerateHash() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            PasswordHashUtil.generateHash(PasswordType.SHA256, TEST_SALT, null));
    }

    @Test
    @DisplayName("Should throw exception for empty password in generateHash")
    void shouldThrowExceptionForEmptyPasswordInGenerateHash() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            PasswordHashUtil.generateHash(PasswordType.SHA256, TEST_SALT, ""));
    }

    @Test
    @DisplayName("Should throw exception for null salt in generateHash")
    void shouldThrowExceptionForNullSaltInGenerateHash() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            PasswordHashUtil.generateHash(PasswordType.SHA256, null, TEST_PASSWORD));
    }

    @Test
    @DisplayName("Should throw exception for empty salt in generateHash")
    void shouldThrowExceptionForEmptySaltInGenerateHash() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            PasswordHashUtil.generateHash(PasswordType.SHA256, "", TEST_PASSWORD));
    }
}
