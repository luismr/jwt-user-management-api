package com.example.login.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .usernameOrEmail("testuser@example.com")
                .password("password123")
                .rememberMe(false)
                .build();
    }

    @Test
    void builder_CreatesValidLoginRequest() {
        // Act
        LoginRequest builtRequest = LoginRequest.builder()
                .usernameOrEmail("admin")
                .password("admin123")
                .rememberMe(true)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals("admin", builtRequest.getUsernameOrEmail());
        assertEquals("admin123", builtRequest.getPassword());
        assertTrue(builtRequest.getRememberMe());
    }

    @Test
    void builder_WithDefaultRememberMe_CreatesRequestWithFalse() {
        // Act
        LoginRequest builtRequest = LoginRequest.builder()
                .usernameOrEmail("user@example.com")
                .password("password123")
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals("user@example.com", builtRequest.getUsernameOrEmail());
        assertEquals("password123", builtRequest.getPassword());
        assertFalse(builtRequest.getRememberMe());
    }

    @Test
    void noArgsConstructor_CreatesEmptyRequest() {
        // Act
        LoginRequest emptyRequest = new LoginRequest();

        // Assert
        assertNotNull(emptyRequest);
        assertNull(emptyRequest.getUsernameOrEmail());
        assertNull(emptyRequest.getPassword());
        assertFalse(emptyRequest.getRememberMe()); // Default value is false, not null
    }

    @Test
    void allArgsConstructor_CreatesRequestWithAllFields() {
        // Act
        LoginRequest requestWithAllFields = new LoginRequest(
                "user@example.com",
                "password123",
                true
        );

        // Assert
        assertNotNull(requestWithAllFields);
        assertEquals("user@example.com", requestWithAllFields.getUsernameOrEmail());
        assertEquals("password123", requestWithAllFields.getPassword());
        assertTrue(requestWithAllFields.getRememberMe());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        // Arrange
        LoginRequest request = new LoginRequest();

        // Act
        request.setUsernameOrEmail("newuser@example.com");
        request.setPassword("newpassword123");
        request.setRememberMe(true);

        // Assert
        assertEquals("newuser@example.com", request.getUsernameOrEmail());
        assertEquals("newpassword123", request.getPassword());
        assertTrue(request.getRememberMe());
    }

    @Test
    void settersAndGetters_WithNullValues_WorkCorrectly() {
        // Arrange
        LoginRequest request = new LoginRequest();

        // Act
        request.setUsernameOrEmail(null);
        request.setPassword(null);
        request.setRememberMe(null);

        // Assert
        assertNull(request.getUsernameOrEmail());
        assertNull(request.getPassword());
        assertNull(request.getRememberMe()); // Can be set to null explicitly
    }

    @Test
    void toString_ContainsAllFields() {
        // Act
        String toString = loginRequest.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("testuser@example.com"));
        assertTrue(toString.contains("password123"));
        assertTrue(toString.contains("false"));
    }

    @Test
    void equals_WhenSameValues_ReturnsTrue() {
        // Arrange
        LoginRequest request1 = LoginRequest.builder()
                .usernameOrEmail("user@example.com")
                .password("password123")
                .rememberMe(true)
                .build();

        LoginRequest request2 = LoginRequest.builder()
                .usernameOrEmail("user@example.com")
                .password("password123")
                .rememberMe(true)
                .build();

        // Act & Assert
        assertEquals(request1, request2);
    }

    @Test
    void equals_WhenDifferentUsernameOrEmail_ReturnsFalse() {
        // Arrange
        LoginRequest request1 = LoginRequest.builder()
                .usernameOrEmail("user1@example.com")
                .password("password123")
                .rememberMe(true)
                .build();

        LoginRequest request2 = LoginRequest.builder()
                .usernameOrEmail("user2@example.com")
                .password("password123")
                .rememberMe(true)
                .build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void equals_WhenDifferentPassword_ReturnsFalse() {
        // Arrange
        LoginRequest request1 = LoginRequest.builder()
                .usernameOrEmail("user@example.com")
                .password("password123")
                .rememberMe(true)
                .build();

        LoginRequest request2 = LoginRequest.builder()
                .usernameOrEmail("user@example.com")
                .password("differentpassword")
                .rememberMe(true)
                .build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void equals_WhenDifferentRememberMe_ReturnsFalse() {
        // Arrange
        LoginRequest request1 = LoginRequest.builder()
                .usernameOrEmail("user@example.com")
                .password("password123")
                .rememberMe(true)
                .build();

        LoginRequest request2 = LoginRequest.builder()
                .usernameOrEmail("user@example.com")
                .password("password123")
                .rememberMe(false)
                .build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void equals_WhenOneIsNull_ReturnsFalse() {
        // Arrange
        LoginRequest request1 = LoginRequest.builder()
                .usernameOrEmail("user@example.com")
                .password("password123")
                .rememberMe(true)
                .build();

        LoginRequest request2 = null;

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void equals_WhenSameObject_ReturnsTrue() {
        // Act & Assert
        assertEquals(loginRequest, loginRequest);
    }

    @Test
    void equals_WhenDifferentClass_ReturnsFalse() {
        // Arrange
        LoginRequest request = LoginRequest.builder()
                .usernameOrEmail("user@example.com")
                .password("password123")
                .rememberMe(true)
                .build();

        String differentObject = "not a LoginRequest";

        // Act & Assert
        assertNotEquals(request, differentObject);
    }

    @Test
    void hashCode_WhenSameValues_ReturnsSameHashCode() {
        // Arrange
        LoginRequest request1 = LoginRequest.builder()
                .usernameOrEmail("user@example.com")
                .password("password123")
                .rememberMe(true)
                .build();

        LoginRequest request2 = LoginRequest.builder()
                .usernameOrEmail("user@example.com")
                .password("password123")
                .rememberMe(true)
                .build();

        // Act & Assert
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void hashCode_WhenDifferentValues_ReturnsDifferentHashCode() {
        // Arrange
        LoginRequest request1 = LoginRequest.builder()
                .usernameOrEmail("user1@example.com")
                .password("password123")
                .rememberMe(true)
                .build();

        LoginRequest request2 = LoginRequest.builder()
                .usernameOrEmail("user2@example.com")
                .password("password123")
                .rememberMe(true)
                .build();

        // Act & Assert
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void builder_WithUsername_CreatesValidRequest() {
        // Act
        LoginRequest builtRequest = LoginRequest.builder()
                .usernameOrEmail("admin")
                .password("admin123")
                .rememberMe(false)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals("admin", builtRequest.getUsernameOrEmail());
        assertEquals("admin123", builtRequest.getPassword());
        assertFalse(builtRequest.getRememberMe());
    }

    @Test
    void builder_WithEmail_CreatesValidRequest() {
        // Act
        LoginRequest builtRequest = LoginRequest.builder()
                .usernameOrEmail("user@example.com")
                .password("password123")
                .rememberMe(true)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals("user@example.com", builtRequest.getUsernameOrEmail());
        assertEquals("password123", builtRequest.getPassword());
        assertTrue(builtRequest.getRememberMe());
    }

    @Test
    void builder_WithEmptyStrings_CreatesRequestWithEmptyValues() {
        // Act
        LoginRequest builtRequest = LoginRequest.builder()
                .usernameOrEmail("")
                .password("")
                .rememberMe(false)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals("", builtRequest.getUsernameOrEmail());
        assertEquals("", builtRequest.getPassword());
        assertFalse(builtRequest.getRememberMe());
    }

    @Test
    void builder_WithMaxLengthValues_CreatesValidRequest() {
        // Arrange
        String maxLengthUsername = "a".repeat(128);
        String maxLengthPassword = "a".repeat(255);

        // Act
        LoginRequest builtRequest = LoginRequest.builder()
                .usernameOrEmail(maxLengthUsername)
                .password(maxLengthPassword)
                .rememberMe(true)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals(maxLengthUsername, builtRequest.getUsernameOrEmail());
        assertEquals(maxLengthPassword, builtRequest.getPassword());
        assertTrue(builtRequest.getRememberMe());
    }
}
