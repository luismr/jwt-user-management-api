package com.example.login.util;

import com.example.login.entity.User.PasswordType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Utility class for password hashing and validation operations.
 * Supports multiple hashing algorithms: BCRYPT, SHA256, MD5, and SHA512.
 */
@Component
public class PasswordHashUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Generate a random salt as a hexadecimal string.
     * 
     * @param length the length of the salt in bytes
     * @return a hexadecimal string representation of the salt
     */
    public static String generateRandomSaltHex(int length) {
        byte[] salt = new byte[length];
        SECURE_RANDOM.nextBytes(salt);
        return HexFormat.of().formatHex(salt);
    }

    /**
     * Generate a hash for the given password using the specified algorithm and salt.
     * 
     * @param type the password hashing algorithm type
     * @param salt the salt to use for hashing
     * @param password the plain text password
     * @return the hashed password
     * @throws IllegalArgumentException if the password type is not supported
     */
    public static String generateHash(PasswordType type, String salt, String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (salt == null || salt.isEmpty()) {
            throw new IllegalArgumentException("Salt cannot be null or empty");
        }

        return switch (type) {
            case BCRYPT -> BCrypt.hashpw(password, salt);
            case SHA256 -> generateSHAHash(password, salt, "SHA-256");
            case MD5 -> generateSHAHash(password, salt, "MD5");
            case SHA512 -> generateSHAHash(password, salt, "SHA-512");
        };
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
    public static boolean isValid(PasswordType type, String salt, String password, String hash) {
        if (password == null || password.isEmpty() || hash == null || hash.isEmpty()) {
            return false;
        }

        try {
            return switch (type) {
                case BCRYPT -> BCrypt.checkpw(password, hash);
                case SHA256, MD5, SHA512 -> {
                    String algorithm = switch (type) {
                        case SHA256 -> "SHA-256";
                        case MD5 -> "MD5";
                        case SHA512 -> "SHA-512";
                        default -> throw new IllegalArgumentException("Unsupported algorithm: " + type);
                    };
                    String computedHash = generateSHAHash(password, salt, algorithm);
                    yield computedHash.equals(hash);
                }
            };
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Generate a hash using SHA algorithms (SHA-256, MD5, SHA-512).
     * 
     * @param password the plain text password
     * @param salt the salt to use
     * @param algorithm the SHA algorithm name
     * @return the hashed password
     */
    private static String generateSHAHash(String password, String salt, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(HexFormat.of().parseHex(salt));
            byte[] hash = digest.digest(password.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Unsupported algorithm: " + algorithm, e);
        }
    }

    /**
     * Generate a salt appropriate for the given password type.
     * 
     * @param type the password hashing algorithm type
     * @return a salt string appropriate for the algorithm
     */
    public static String generateSalt(PasswordType type) {
        return switch (type) {
            case BCRYPT -> BCrypt.gensalt();
            case SHA256, MD5, SHA512 -> generateRandomSaltHex(16);
        };
    }

    /**
     * Generate a complete hash with salt for the given password type.
     * This is a convenience method that generates both salt and hash.
     * 
     * @param type the password hashing algorithm type
     * @param password the plain text password
     * @return a PasswordHashResult containing both salt and hash
     */
    public static PasswordHashResult generateHashWithSalt(PasswordType type, String password) {
        String salt = generateSalt(type);
        String hash = generateHash(type, salt, password);
        return new PasswordHashResult(salt, hash);
    }

    /**
     * Result class containing salt and hash for password operations.
     */
    public static class PasswordHashResult {
        private final String salt;
        private final String hash;

        public PasswordHashResult(String salt, String hash) {
            this.salt = salt;
            this.hash = hash;
        }

        public String getSalt() {
            return salt;
        }

        public String getHash() {
            return hash;
        }
    }
}
