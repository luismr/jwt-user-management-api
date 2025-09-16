package com.example.login.exception;

/**
 * Exception thrown when authentication fails.
 * This can be due to invalid credentials, inactive user, or other authentication issues.
 */
public class AuthenticationException extends RuntimeException {

    private final AuthenticationErrorType errorType;

    public AuthenticationException(String message) {
        super(message);
        this.errorType = AuthenticationErrorType.INVALID_CREDENTIALS;
    }

    public AuthenticationException(String message, AuthenticationErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        this.errorType = AuthenticationErrorType.INVALID_CREDENTIALS;
    }

    public AuthenticationException(String message, AuthenticationErrorType errorType, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public AuthenticationErrorType getErrorType() {
        return errorType;
    }

    /**
     * Enum representing different types of authentication errors.
     */
    public enum AuthenticationErrorType {
        INVALID_CREDENTIALS("Invalid username/email or password"),
        USER_NOT_FOUND("User not found"),
        USER_INACTIVE("User account is inactive"),
        USER_SUSPENDED("User account is suspended"),
        INVALID_PASSWORD("Invalid password"),
        ACCOUNT_LOCKED("Account is locked"),
        CREDENTIALS_EXPIRED("Credentials have expired");

        private final String description;

        AuthenticationErrorType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
