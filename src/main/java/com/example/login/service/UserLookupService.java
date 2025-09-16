package com.example.login.service;

import com.example.login.entity.User;
import com.example.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service for user lookup and authentication operations.
 * Provides methods to find users and validate their credentials.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserLookupService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    /**
     * Find a user by username.
     * 
     * @param username the username to search for
     * @return Optional containing the user if found, empty otherwise
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            log.warn("Attempted to find user with null or empty username");
            return Optional.empty();
        }
        
        log.debug("Looking up user by username: {}", username);
        return userRepository.findByUsername(username.trim());
    }

    /**
     * Find a user by email.
     * 
     * @param email the email to search for
     * @return Optional containing the user if found, empty otherwise
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            log.warn("Attempted to find user with null or empty email");
            return Optional.empty();
        }
        
        log.debug("Looking up user by email: {}", email);
        return userRepository.findByEmail(email.trim());
    }

    /**
     * Find a user by ID.
     * 
     * @param id the user ID to search for
     * @return Optional containing the user if found, empty otherwise
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        if (id == null || id <= 0) {
            log.warn("Attempted to find user with invalid ID: {}", id);
            return Optional.empty();
        }
        
        log.debug("Looking up user by ID: {}", id);
        return userRepository.findById(id);
    }

    /**
     * Authenticate a user by username and password.
     * 
     * @param username the username
     * @param password the plain text password
     * @return Optional containing the user if authentication succeeds, empty otherwise
     */
    @Transactional(readOnly = true)
    public Optional<User> authenticateByUsername(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            log.warn("Authentication attempted with null or empty username");
            return Optional.empty();
        }
        
        if (password == null || password.isEmpty()) {
            log.warn("Authentication attempted with null or empty password for username: {}", username);
            return Optional.empty();
        }

        log.debug("Authenticating user: {}", username);
        
        Optional<User> userOpt = findByUsername(username);
        if (userOpt.isEmpty()) {
            log.warn("Authentication failed: user not found for username: {}", username);
            return Optional.empty();
        }

        User user = userOpt.get();
        
        // Check if user is active
        if (user.getStatus() != User.UserStatus.ACTIVE) {
            log.warn("Authentication failed: user {} is not active (status: {})", username, user.getStatus());
            return Optional.empty();
        }

        // Validate password
        boolean isValidPassword = passwordService.isValid(
            user.getPasswordType(),
            user.getPasswordSalt(),
            password,
            user.getPasswordHash()
        );

        if (!isValidPassword) {
            log.warn("Authentication failed: invalid password for user: {}", username);
            return Optional.empty();
        }

        log.info("Authentication successful for user: {}", username);
        return Optional.of(user);
    }

    /**
     * Authenticate a user by email and password.
     * 
     * @param email the email
     * @param password the plain text password
     * @return Optional containing the user if authentication succeeds, empty otherwise
     */
    @Transactional(readOnly = true)
    public Optional<User> authenticateByEmail(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            log.warn("Authentication attempted with null or empty email");
            return Optional.empty();
        }
        
        if (password == null || password.isEmpty()) {
            log.warn("Authentication attempted with null or empty password for email: {}", email);
            return Optional.empty();
        }

        log.debug("Authenticating user by email: {}", email);
        
        Optional<User> userOpt = findByEmail(email);
        if (userOpt.isEmpty()) {
            log.warn("Authentication failed: user not found for email: {}", email);
            return Optional.empty();
        }

        User user = userOpt.get();
        
        // Check if user is active
        if (user.getStatus() != User.UserStatus.ACTIVE) {
            log.warn("Authentication failed: user {} is not active (status: {})", email, user.getStatus());
            return Optional.empty();
        }

        // Validate password
        boolean isValidPassword = passwordService.isValid(
            user.getPasswordType(),
            user.getPasswordSalt(),
            password,
            user.getPasswordHash()
        );

        if (!isValidPassword) {
            log.warn("Authentication failed: invalid password for user: {}", email);
            return Optional.empty();
        }

        log.info("Authentication successful for user: {}", email);
        return Optional.of(user);
    }

    /**
     * Update the last login timestamp for a user.
     * 
     * @param user the user to update
     */
    @Transactional
    public void updateLastLogin(User user) {
        if (user == null) {
            log.warn("Attempted to update last login for null user");
            return;
        }
        
        log.debug("Updating last login for user: {}", user.getUsername());
        user.setDateLastLogin(LocalDateTime.now());
        userRepository.save(user);
        log.info("Last login updated for user: {}", user.getUsername());
    }

    /**
     * Check if a username exists in the system.
     * 
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        return userRepository.existsByUsername(username.trim());
    }

    /**
     * Check if an email exists in the system.
     * 
     * @param email the email to check
     * @return true if the email exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return userRepository.existsByEmail(email.trim());
    }

    /**
     * Validate a password against a user's stored credentials.
     * 
     * @param user the user to validate against
     * @param password the plain text password to validate
     * @return true if the password is valid, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean validatePassword(User user, String password) {
        if (user == null) {
            log.warn("Password validation attempted for null user");
            return false;
        }
        
        if (password == null || password.isEmpty()) {
            log.warn("Password validation attempted with null or empty password for user: {}", user.getUsername());
            return false;
        }

        return passwordService.isValid(
            user.getPasswordType(),
            user.getPasswordSalt(),
            password,
            user.getPasswordHash()
        );
    }
}
