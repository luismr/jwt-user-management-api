package com.example.login.service;

import com.example.login.entity.User.PasswordType;
import com.example.login.util.PasswordHashUtil;
import com.example.login.util.PasswordHashUtil.PasswordHashResult;
import org.springframework.stereotype.Service;

/**
 * Spring service for password hashing and validation operations.
 * Provides a high-level interface for password management with different hashing algorithms.
 */
@Service
public class PasswordService {

    /**
     * Generate a hash for the given password using the specified algorithm.
     * 
     * @param typeString the password hashing algorithm type as string (case-insensitive)
     * @param password the plain text password
     * @return a PasswordHashResult containing both salt and hash
     * @throws IllegalArgumentException if the password type is not supported or password is invalid
     */
    public PasswordHashResult generateHash(String typeString, String password) {
        PasswordType type = PasswordType.fromString(typeString);
        return generateHash(type, password);
    }

    /**
     * Generate a hash for the given password using the specified algorithm.
     * 
     * @param type the password hashing algorithm type
     * @param password the plain text password
     * @return a PasswordHashResult containing both salt and hash
     * @throws IllegalArgumentException if the password is invalid
     */
    public PasswordHashResult generateHash(PasswordType type, String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return PasswordHashUtil.generateHashWithSalt(type, password);
    }

    /**
     * Generate a hash for the given password using the specified algorithm and salt.
     * 
     * @param typeString the password hashing algorithm type as string (case-insensitive)
     * @param salt the salt to use for hashing
     * @param password the plain text password
     * @return the hashed password
     * @throws IllegalArgumentException if the password type is not supported or parameters are invalid
     */
    public String generateHash(String typeString, String salt, String password) {
        PasswordType type = PasswordType.fromString(typeString);
        return generateHash(type, salt, password);
    }

    /**
     * Generate a hash for the given password using the specified algorithm and salt.
     * 
     * @param type the password hashing algorithm type
     * @param salt the salt to use for hashing
     * @param password the plain text password
     * @return the hashed password
     * @throws IllegalArgumentException if parameters are invalid
     */
    public String generateHash(PasswordType type, String salt, String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (salt == null || salt.trim().isEmpty()) {
            throw new IllegalArgumentException("Salt cannot be null or empty");
        }
        return PasswordHashUtil.generateHash(type, salt, password);
    }

    /**
     * Validate a password against its hash using the specified algorithm and salt.
     * 
     * @param typeString the password hashing algorithm type as string (case-insensitive)
     * @param salt the salt used for hashing
     * @param password the plain text password to validate
     * @param hash the stored hash to compare against
     * @return true if the password matches the hash, false otherwise
     */
    public boolean isValid(String typeString, String salt, String password, String hash) {
        PasswordType type = PasswordType.fromString(typeString);
        return isValid(type, salt, password, hash);
    }

    /**
     * Validate a password against its hash using the specified algorithm and salt.
     * 
     * @param type the password hashing algorithm type
     * @param salt the salt used for hashing
     * @param password the plain text password to validate
     * @param hash the stored hash to compare against
     * @return true if the password matches the hash, false otherwise
     */
    public boolean isValid(PasswordType type, String salt, String password, String hash) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        if (salt == null || salt.trim().isEmpty() || hash == null || hash.trim().isEmpty()) {
            return false;
        }
        return PasswordHashUtil.isValid(type, salt, password, hash);
    }

    /**
     * Generate a salt appropriate for the given password type.
     * 
     * @param typeString the password hashing algorithm type as string (case-insensitive)
     * @return a salt string appropriate for the algorithm
     * @throws IllegalArgumentException if the password type is not supported
     */
    public String generateSalt(String typeString) {
        PasswordType type = PasswordType.fromString(typeString);
        return generateSalt(type);
    }

    /**
     * Generate a salt appropriate for the given password type.
     * 
     * @param type the password hashing algorithm type
     * @return a salt string appropriate for the algorithm
     */
    public String generateSalt(PasswordType type) {
        return PasswordHashUtil.generateSalt(type);
    }

    /**
     * Get the default password type for new users.
     * 
     * @return the default password type
     */
    public PasswordType getDefaultPasswordType() {
        return PasswordType.MD5;
    }

    /**
     * Check if a password meets minimum security requirements.
     * 
     * @param password the password to validate
     * @return true if the password meets requirements, false otherwise
     */
    public boolean isPasswordSecure(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);
        
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
}
