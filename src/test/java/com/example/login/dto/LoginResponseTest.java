package com.example.login.dto;

import com.example.login.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {

    private LoginResponse loginResponse;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setStatus(User.UserStatus.ACTIVE);
        testUser.setIdClient(1L);
        testUser.setDateLastLogin(LocalDateTime.now().minusDays(1));
        testUser.setDateCreated(LocalDateTime.now().minusMonths(1));

        loginResponse = LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .user(LoginResponse.UserInfo.fromUser(testUser))
                .token("jwt-token-here")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    void builder_CreatesValidLoginResponse() {
        // Act
        LoginResponse builtResponse = LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .user(LoginResponse.UserInfo.fromUser(testUser))
                .token("jwt-token-here")
                .timestamp(LocalDateTime.now())
                .build();

        // Assert
        assertNotNull(builtResponse);
        assertTrue(builtResponse.isSuccess());
        assertEquals("Login successful", builtResponse.getMessage());
        assertNotNull(builtResponse.getUser());
        assertEquals("jwt-token-here", builtResponse.getToken());
        assertNotNull(builtResponse.getTimestamp());
    }

    @Test
    void builder_WithNullValues_CreatesValidResponse() {
        // Act
        LoginResponse builtResponse = LoginResponse.builder()
                .success(false)
                .message("Login failed")
                .user(null)
                .token(null)
                .timestamp(LocalDateTime.now())
                .build();

        // Assert
        assertNotNull(builtResponse);
        assertFalse(builtResponse.isSuccess());
        assertEquals("Login failed", builtResponse.getMessage());
        assertNull(builtResponse.getUser());
        assertNull(builtResponse.getToken());
        assertNotNull(builtResponse.getTimestamp());
    }

    @Test
    void noArgsConstructor_CreatesEmptyResponse() {
        // Act
        LoginResponse emptyResponse = new LoginResponse();

        // Assert
        assertNotNull(emptyResponse);
        assertFalse(emptyResponse.isSuccess());
        assertNull(emptyResponse.getMessage());
        assertNull(emptyResponse.getUser());
        assertNull(emptyResponse.getToken());
        assertNull(emptyResponse.getTimestamp());
    }

    @Test
    void allArgsConstructor_CreatesResponseWithAllFields() {
        // Arrange
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.fromUser(testUser);
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        LoginResponse responseWithAllFields = new LoginResponse(
                true,
                "Login successful",
                userInfo,
                "jwt-token-here",
                timestamp
        );

        // Assert
        assertNotNull(responseWithAllFields);
        assertTrue(responseWithAllFields.isSuccess());
        assertEquals("Login successful", responseWithAllFields.getMessage());
        assertEquals(userInfo, responseWithAllFields.getUser());
        assertEquals("jwt-token-here", responseWithAllFields.getToken());
        assertEquals(timestamp, responseWithAllFields.getTimestamp());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        // Arrange
        LoginResponse response = new LoginResponse();
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.fromUser(testUser);
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        response.setSuccess(false);
        response.setMessage("Authentication failed");
        response.setUser(userInfo);
        response.setToken("new-token");
        response.setTimestamp(timestamp);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Authentication failed", response.getMessage());
        assertEquals(userInfo, response.getUser());
        assertEquals("new-token", response.getToken());
        assertEquals(timestamp, response.getTimestamp());
    }

    @Test
    void success_WithUserAndToken_CreatesSuccessfulResponse() {
        // Act
        LoginResponse response = LoginResponse.success(testUser, "jwt-token");

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getUser());
        assertEquals("jwt-token", response.getToken());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void success_WithUserOnly_CreatesSuccessfulResponseWithoutToken() {
        // Act
        LoginResponse response = LoginResponse.success(testUser);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getUser());
        assertNull(response.getToken());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void success_WithNullUser_CreatesResponseWithNullUser() {
        // Act
        LoginResponse response = LoginResponse.success(null, "jwt-token");

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertNull(response.getUser());
        assertEquals("jwt-token", response.getToken());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void failure_WithMessage_CreatesFailedResponse() {
        // Act
        LoginResponse response = LoginResponse.failure("Invalid credentials");

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Invalid credentials", response.getMessage());
        assertNull(response.getUser());
        assertNull(response.getToken());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void failure_WithNullMessage_CreatesFailedResponseWithNullMessage() {
        // Act
        LoginResponse response = LoginResponse.failure(null);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getMessage());
        assertNull(response.getUser());
        assertNull(response.getToken());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void toString_ContainsAllFields() {
        // Act
        String toString = loginResponse.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("true"));
        assertTrue(toString.contains("Login successful"));
        assertTrue(toString.contains("jwt-token-here"));
    }

    @Test
    void equals_WhenSameValues_ReturnsTrue() {
        // Arrange
        LoginResponse response1 = LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .user(LoginResponse.UserInfo.fromUser(testUser))
                .token("jwt-token")
                .timestamp(LocalDateTime.now())
                .build();

        LoginResponse response2 = LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .user(LoginResponse.UserInfo.fromUser(testUser))
                .token("jwt-token")
                .timestamp(response1.getTimestamp())
                .build();

        // Act & Assert
        assertEquals(response1, response2);
    }

    @Test
    void equals_WhenDifferentSuccess_ReturnsFalse() {
        // Arrange
        LoginResponse response1 = LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .build();

        LoginResponse response2 = LoginResponse.builder()
                .success(false)
                .message("Login successful")
                .build();

        // Act & Assert
        assertNotEquals(response1, response2);
    }

    @Test
    void equals_WhenSameObject_ReturnsTrue() {
        // Act & Assert
        assertEquals(loginResponse, loginResponse);
    }

    @Test
    void equals_WhenOneIsNull_ReturnsFalse() {
        // Arrange
        LoginResponse response1 = LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .build();

        LoginResponse response2 = null;

        // Act & Assert
        assertNotEquals(response1, response2);
    }

    @Test
    void equals_WhenDifferentClass_ReturnsFalse() {
        // Arrange
        LoginResponse response = LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .build();

        String differentObject = "not a LoginResponse";

        // Act & Assert
        assertNotEquals(response, differentObject);
    }

    @Test
    void hashCode_WhenSameValues_ReturnsSameHashCode() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        LoginResponse response1 = LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .user(LoginResponse.UserInfo.fromUser(testUser))
                .token("jwt-token")
                .timestamp(timestamp)
                .build();

        LoginResponse response2 = LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .user(LoginResponse.UserInfo.fromUser(testUser))
                .token("jwt-token")
                .timestamp(timestamp)
                .build();

        // Act & Assert
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void hashCode_WhenDifferentValues_ReturnsDifferentHashCode() {
        // Arrange
        LoginResponse response1 = LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .build();

        LoginResponse response2 = LoginResponse.builder()
                .success(false)
                .message("Login failed")
                .build();

        // Act & Assert
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    // UserInfo static class tests
    @Test
    void userInfo_FromUser_CreatesValidUserInfo() {
        // Act
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.fromUser(testUser);

        // Assert
        assertNotNull(userInfo);
        assertEquals(testUser.getId(), userInfo.getId());
        assertEquals(testUser.getUsername(), userInfo.getUsername());
        assertEquals(testUser.getName(), userInfo.getName());
        assertEquals(testUser.getEmail(), userInfo.getEmail());
        assertEquals(testUser.getStatus().name(), userInfo.getStatus());
        assertEquals(testUser.getIdClient(), userInfo.getClientId());
        assertEquals(testUser.getDateLastLogin(), userInfo.getLastLogin());
        assertEquals(testUser.getDateCreated(), userInfo.getDateCreated());
    }

    @Test
    void userInfo_FromNullUser_ReturnsNull() {
        // Act
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.fromUser(null);

        // Assert
        assertNull(userInfo);
    }

    @Test
    void userInfo_FromUserWithNullStatus_HandlesNullStatus() {
        // Arrange
        User userWithNullStatus = new User();
        userWithNullStatus.setId(1L);
        userWithNullStatus.setUsername("testuser");
        userWithNullStatus.setName("Test User");
        userWithNullStatus.setEmail("test@example.com");
        userWithNullStatus.setStatus(null);
        userWithNullStatus.setIdClient(1L);
        userWithNullStatus.setDateLastLogin(LocalDateTime.now().minusDays(1));
        userWithNullStatus.setDateCreated(LocalDateTime.now().minusMonths(1));

        // Act
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.fromUser(userWithNullStatus);

        // Assert
        assertNotNull(userInfo);
        assertEquals(userWithNullStatus.getId(), userInfo.getId());
        assertEquals(userWithNullStatus.getUsername(), userInfo.getUsername());
        assertEquals(userWithNullStatus.getName(), userInfo.getName());
        assertEquals(userWithNullStatus.getEmail(), userInfo.getEmail());
        assertNull(userInfo.getStatus());
        assertEquals(userWithNullStatus.getIdClient(), userInfo.getClientId());
        assertEquals(userWithNullStatus.getDateLastLogin(), userInfo.getLastLogin());
        assertEquals(userWithNullStatus.getDateCreated(), userInfo.getDateCreated());
    }

    @Test
    void userInfo_Builder_CreatesValidUserInfo() {
        // Act
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.builder()
                .id(1L)
                .username("testuser")
                .name("Test User")
                .email("test@example.com")
                .status("ACTIVE")
                .clientId(1L)
                .lastLogin(LocalDateTime.now().minusDays(1))
                .dateCreated(LocalDateTime.now().minusMonths(1))
                .build();

        // Assert
        assertNotNull(userInfo);
        assertEquals(1L, userInfo.getId());
        assertEquals("testuser", userInfo.getUsername());
        assertEquals("Test User", userInfo.getName());
        assertEquals("test@example.com", userInfo.getEmail());
        assertEquals("ACTIVE", userInfo.getStatus());
        assertEquals(1L, userInfo.getClientId());
        assertNotNull(userInfo.getLastLogin());
        assertNotNull(userInfo.getDateCreated());
    }

    @Test
    void userInfo_NoArgsConstructor_CreatesEmptyUserInfo() {
        // Act
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();

        // Assert
        assertNotNull(userInfo);
        assertNull(userInfo.getId());
        assertNull(userInfo.getUsername());
        assertNull(userInfo.getName());
        assertNull(userInfo.getEmail());
        assertNull(userInfo.getStatus());
        assertNull(userInfo.getClientId());
        assertNull(userInfo.getLastLogin());
        assertNull(userInfo.getDateCreated());
    }

    @Test
    void userInfo_AllArgsConstructor_CreatesUserInfoWithAllFields() {
        // Arrange
        LocalDateTime lastLogin = LocalDateTime.now().minusDays(1);
        LocalDateTime dateCreated = LocalDateTime.now().minusMonths(1);

        // Act
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                1L,
                "testuser",
                "Test User",
                "test@example.com",
                "ACTIVE",
                1L,
                lastLogin,
                dateCreated
        );

        // Assert
        assertNotNull(userInfo);
        assertEquals(1L, userInfo.getId());
        assertEquals("testuser", userInfo.getUsername());
        assertEquals("Test User", userInfo.getName());
        assertEquals("test@example.com", userInfo.getEmail());
        assertEquals("ACTIVE", userInfo.getStatus());
        assertEquals(1L, userInfo.getClientId());
        assertEquals(lastLogin, userInfo.getLastLogin());
        assertEquals(dateCreated, userInfo.getDateCreated());
    }

    @Test
    void userInfo_SettersAndGetters_WorkCorrectly() {
        // Arrange
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        LocalDateTime lastLogin = LocalDateTime.now().minusDays(1);
        LocalDateTime dateCreated = LocalDateTime.now().minusMonths(1);

        // Act
        userInfo.setId(2L);
        userInfo.setUsername("newuser");
        userInfo.setName("New User");
        userInfo.setEmail("new@example.com");
        userInfo.setStatus("INACTIVE");
        userInfo.setClientId(2L);
        userInfo.setLastLogin(lastLogin);
        userInfo.setDateCreated(dateCreated);

        // Assert
        assertEquals(2L, userInfo.getId());
        assertEquals("newuser", userInfo.getUsername());
        assertEquals("New User", userInfo.getName());
        assertEquals("new@example.com", userInfo.getEmail());
        assertEquals("INACTIVE", userInfo.getStatus());
        assertEquals(2L, userInfo.getClientId());
        assertEquals(lastLogin, userInfo.getLastLogin());
        assertEquals(dateCreated, userInfo.getDateCreated());
    }

    @Test
    void userInfo_ToString_ContainsAllFields() {
        // Arrange
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.fromUser(testUser);

        // Act
        String toString = userInfo.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("testuser"));
        assertTrue(toString.contains("Test User"));
        assertTrue(toString.contains("test@example.com"));
    }

    @Test
    void userInfo_Equals_WhenSameValues_ReturnsTrue() {
        // Arrange
        LoginResponse.UserInfo userInfo1 = LoginResponse.UserInfo.builder()
                .id(1L)
                .username("testuser")
                .name("Test User")
                .email("test@example.com")
                .status("ACTIVE")
                .clientId(1L)
                .build();

        LoginResponse.UserInfo userInfo2 = LoginResponse.UserInfo.builder()
                .id(1L)
                .username("testuser")
                .name("Test User")
                .email("test@example.com")
                .status("ACTIVE")
                .clientId(1L)
                .build();

        // Act & Assert
        assertEquals(userInfo1, userInfo2);
    }

    @Test
    void userInfo_Equals_WhenDifferentValues_ReturnsFalse() {
        // Arrange
        LoginResponse.UserInfo userInfo1 = LoginResponse.UserInfo.builder()
                .id(1L)
                .username("testuser")
                .name("Test User")
                .email("test@example.com")
                .status("ACTIVE")
                .clientId(1L)
                .build();

        LoginResponse.UserInfo userInfo2 = LoginResponse.UserInfo.builder()
                .id(2L)
                .username("testuser")
                .name("Test User")
                .email("test@example.com")
                .status("ACTIVE")
                .clientId(1L)
                .build();

        // Act & Assert
        assertNotEquals(userInfo1, userInfo2);
    }

    @Test
    void userInfo_HashCode_WhenSameValues_ReturnsSameHashCode() {
        // Arrange
        LoginResponse.UserInfo userInfo1 = LoginResponse.UserInfo.builder()
                .id(1L)
                .username("testuser")
                .name("Test User")
                .email("test@example.com")
                .status("ACTIVE")
                .clientId(1L)
                .build();

        LoginResponse.UserInfo userInfo2 = LoginResponse.UserInfo.builder()
                .id(1L)
                .username("testuser")
                .name("Test User")
                .email("test@example.com")
                .status("ACTIVE")
                .clientId(1L)
                .build();

        // Act & Assert
        assertEquals(userInfo1.hashCode(), userInfo2.hashCode());
    }
}
