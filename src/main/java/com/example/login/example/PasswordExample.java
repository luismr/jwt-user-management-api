package com.example.login.example;

import com.example.login.entity.User.PasswordType;
import com.example.login.service.PasswordService;
import com.example.login.util.PasswordHashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Example class demonstrating how to use the password hashing functionality.
 * This class shows the exact usage pattern requested by the user.
 */
@Component
public class PasswordExample {

    @Autowired
    private PasswordService passwordService;

    /**
     * Example method showing how to use the password hashing functionality
     * as requested in the user query.
     */
    public void demonstratePasswordHashing() {
        // Get type from string (case-insensitive)
        PasswordType type = PasswordType.fromString("MD5");

        // Generate salt
        String salt = PasswordHashUtil.generateRandomSaltHex(16);
        
        // Create hash
        String hash = PasswordHashUtil.generateHash(type, salt, "P@ssw0rd!");

        // Validate later
        boolean ok = PasswordHashUtil.isValid(type, salt, "P@ssw0rd!", hash);

        System.out.println("Password type: " + type);
        System.out.println("Salt: " + salt);
        System.out.println("Hash: " + hash);
        System.out.println("Validation result: " + ok);
    }

    /**
     * Example method showing how to use the Spring service bean.
     */
    public void demonstratePasswordService() {
        // Using the Spring service bean
        String password = "P@ssw0rd!";
        
        // Generate hash with salt using string type
        var result = passwordService.generateHash("SHA256", password);
        System.out.println("Generated salt: " + result.getSalt());
        System.out.println("Generated hash: " + result.getHash());
        
        // Validate password
        boolean isValid = passwordService.isValid("SHA256", result.getSalt(), password, result.getHash());
        System.out.println("Password validation: " + isValid);
        
        // Check password security
        boolean isSecure = passwordService.isPasswordSecure(password);
        System.out.println("Password is secure: " + isSecure);
    }

    /**
     * Example method showing different password types.
     */
    public void demonstrateDifferentPasswordTypes() {
        String password = "MySecurePassword123!";
        
        for (PasswordType type : PasswordType.values()) {
            System.out.println("\n--- " + type + " ---");
            
            // Generate salt and hash
            var result = passwordService.generateHash(type, password);
            System.out.println("Salt: " + result.getSalt());
            System.out.println("Hash: " + result.getHash());
            
            // Validate
            boolean isValid = passwordService.isValid(type, result.getSalt(), password, result.getHash());
            System.out.println("Valid: " + isValid);
        }
    }
}
