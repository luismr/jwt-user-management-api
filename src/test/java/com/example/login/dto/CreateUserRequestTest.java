package com.example.login.dto;

import com.example.login.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserRequestTest {

    private CreateUserRequest createUserRequest;

    @BeforeEach
    void setUp() {
        createUserRequest = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();
    }

    @Test
    void builder_CreatesValidCreateUserRequest() {
        // Act
        CreateUserRequest builtRequest = CreateUserRequest.builder()
                .username("newuser")
                .password("newpassword123")
                .name("New User")
                .email("new@example.com")
                .idClient(2L)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals("newuser", builtRequest.getUsername());
        assertEquals("newpassword123", builtRequest.getPassword());
        assertEquals("New User", builtRequest.getName());
        assertEquals("new@example.com", builtRequest.getEmail());
        assertEquals(2L, builtRequest.getIdClient());
    }

    @Test
    void noArgsConstructor_CreatesEmptyRequest() {
        // Act
        CreateUserRequest emptyRequest = new CreateUserRequest();

        // Assert
        assertNotNull(emptyRequest);
        assertNull(emptyRequest.getUsername());
        assertNull(emptyRequest.getPassword());
        assertNull(emptyRequest.getName());
        assertNull(emptyRequest.getEmail());
        assertNull(emptyRequest.getIdClient());
    }

    @Test
    void allArgsConstructor_CreatesRequestWithAllFields() {
        // Act
        CreateUserRequest requestWithAllFields = new CreateUserRequest(
                "testuser",
                "password123",
                "Test User",
                "test@example.com",
                1L,
                "BCRYPT",
                "ACTIVE"
        );

        // Assert
        assertNotNull(requestWithAllFields);
        assertEquals("testuser", requestWithAllFields.getUsername());
        assertEquals("password123", requestWithAllFields.getPassword());
        assertEquals("Test User", requestWithAllFields.getName());
        assertEquals("test@example.com", requestWithAllFields.getEmail());
        assertEquals(1L, requestWithAllFields.getIdClient());
        assertEquals("BCRYPT", requestWithAllFields.getPasswordType());
        assertEquals("ACTIVE", requestWithAllFields.getStatus());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();

        // Act
        request.setUsername("newuser");
        request.setPassword("newpassword123");
        request.setName("New User");
        request.setEmail("new@example.com");
        request.setIdClient(2L);
        request.setPasswordType("SHA256");
        request.setStatus("INACTIVE");

        // Assert
        assertEquals("newuser", request.getUsername());
        assertEquals("newpassword123", request.getPassword());
        assertEquals("New User", request.getName());
        assertEquals("new@example.com", request.getEmail());
        assertEquals(2L, request.getIdClient());
        assertEquals("SHA256", request.getPasswordType());
        assertEquals("INACTIVE", request.getStatus());
    }

    @Test
    void settersAndGetters_WithNullValues_WorkCorrectly() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();

        // Act
        request.setUsername(null);
        request.setPassword(null);
        request.setName(null);
        request.setEmail(null);
        request.setIdClient(null);
        request.setPasswordType(null);
        request.setStatus(null);

        // Assert
        assertNull(request.getUsername());
        assertNull(request.getPassword());
        assertNull(request.getName());
        assertNull(request.getEmail());
        assertNull(request.getIdClient());
        assertNull(request.getPasswordType());
        assertNull(request.getStatus());
    }

    @Test
    void toString_ContainsAllFields() {
        // Act
        String toString = createUserRequest.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("testuser"));
        assertTrue(toString.contains("password123"));
        assertTrue(toString.contains("Test User"));
        assertTrue(toString.contains("test@example.com"));
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("BCRYPT"));
        assertTrue(toString.contains("ACTIVE"));
    }

    @Test
    void equals_WhenSameValues_ReturnsTrue() {
        // Arrange
        CreateUserRequest request1 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        CreateUserRequest request2 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        // Act & Assert
        assertEquals(request1, request2);
    }

    @Test
    void equals_WhenDifferentUsername_ReturnsFalse() {
        // Arrange
        CreateUserRequest request1 = CreateUserRequest.builder()
                .username("user1")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        CreateUserRequest request2 = CreateUserRequest.builder()
                .username("user2")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void equals_WhenDifferentPassword_ReturnsFalse() {
        // Arrange
        CreateUserRequest request1 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        CreateUserRequest request2 = CreateUserRequest.builder()
                .username("testuser")
                .password("differentpassword")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void equals_WhenDifferentName_ReturnsFalse() {
        // Arrange
        CreateUserRequest request1 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        CreateUserRequest request2 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Different User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void equals_WhenDifferentEmail_ReturnsFalse() {
        // Arrange
        CreateUserRequest request1 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        CreateUserRequest request2 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("different@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void equals_WhenDifferentIdClient_ReturnsFalse() {
        // Arrange
        CreateUserRequest request1 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        CreateUserRequest request2 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(2L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void equals_WhenSameObject_ReturnsTrue() {
        // Act & Assert
        assertEquals(createUserRequest, createUserRequest);
    }

    @Test
    void equals_WhenOneIsNull_ReturnsFalse() {
        // Arrange
        CreateUserRequest request1 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        CreateUserRequest request2 = null;

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void equals_WhenDifferentClass_ReturnsFalse() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        String differentObject = "not a CreateUserRequest";

        // Act & Assert
        assertNotEquals(request, differentObject);
    }

    @Test
    void hashCode_WhenSameValues_ReturnsSameHashCode() {
        // Arrange
        CreateUserRequest request1 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        CreateUserRequest request2 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        // Act & Assert
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void hashCode_WhenDifferentValues_ReturnsDifferentHashCode() {
        // Arrange
        CreateUserRequest request1 = CreateUserRequest.builder()
                .username("user1")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        CreateUserRequest request2 = CreateUserRequest.builder()
                .username("user2")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        // Act & Assert
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void builder_WithMinLengthValues_CreatesValidRequest() {
        // Act
        CreateUserRequest builtRequest = CreateUserRequest.builder()
                .username("abc") // min length 3
                .password("password") // min length 8
                .name("A")
                .email("a@b.com")
                .idClient(1L)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals("abc", builtRequest.getUsername());
        assertEquals("password", builtRequest.getPassword());
        assertEquals("A", builtRequest.getName());
        assertEquals("a@b.com", builtRequest.getEmail());
        assertEquals(1L, builtRequest.getIdClient());
    }

    @Test
    void builder_WithMaxLengthValues_CreatesValidRequest() {
        // Arrange
        String maxLengthUsername = "a".repeat(128);
        String maxLengthPassword = "a".repeat(255);
        String maxLengthName = "a".repeat(128);
        String maxLengthEmail = "a".repeat(120) + "@example.com"; // 128 chars total

        // Act
        CreateUserRequest builtRequest = CreateUserRequest.builder()
                .username(maxLengthUsername)
                .password(maxLengthPassword)
                .name(maxLengthName)
                .email(maxLengthEmail)
                .idClient(1L)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals(maxLengthUsername, builtRequest.getUsername());
        assertEquals(maxLengthPassword, builtRequest.getPassword());
        assertEquals(maxLengthName, builtRequest.getName());
        assertEquals(maxLengthEmail, builtRequest.getEmail());
        assertEquals(1L, builtRequest.getIdClient());
    }

    @Test
    void builder_WithEmptyStrings_CreatesRequestWithEmptyValues() {
        // Act
        CreateUserRequest builtRequest = CreateUserRequest.builder()
                .username("")
                .password("")
                .name("")
                .email("")
                .idClient(1L)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals("", builtRequest.getUsername());
        assertEquals("", builtRequest.getPassword());
        assertEquals("", builtRequest.getName());
        assertEquals("", builtRequest.getEmail());
        assertEquals(1L, builtRequest.getIdClient());
    }

    @Test
    void builder_WithValidEmailFormats_CreatesValidRequest() {
        // Test various valid email formats
        String[] validEmails = {
                "user@example.com",
                "user.name@example.com",
                "user+tag@example.co.uk",
                "user123@example-domain.com",
                "a@b.co"
        };

        for (String email : validEmails) {
            // Act
            CreateUserRequest builtRequest = CreateUserRequest.builder()
                    .username("testuser")
                    .password("password123")
                    .name("Test User")
                    .email(email)
                    .idClient(1L)
                    .build();

            // Assert
            assertNotNull(builtRequest);
            assertEquals(email, builtRequest.getEmail());
        }
    }

    @Test
    void builder_WithZeroIdClient_CreatesValidRequest() {
        // Act
        CreateUserRequest builtRequest = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(0L)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals(0L, builtRequest.getIdClient());
    }

    @Test
    void builder_WithNegativeIdClient_CreatesValidRequest() {
        // Act
        CreateUserRequest builtRequest = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(-1L)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals(-1L, builtRequest.getIdClient());
    }

    @Test
    void builder_WithLongIdClient_CreatesValidRequest() {
        // Act
        CreateUserRequest builtRequest = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(Long.MAX_VALUE)
                .build();

        // Assert
        assertNotNull(builtRequest);
        assertEquals(Long.MAX_VALUE, builtRequest.getIdClient());
    }

    @Test
    void getPasswordTypeEnum_WithValidType_ReturnsCorrectEnum() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .passwordType("BCRYPT")
                .build();

        // Act
        User.PasswordType result = request.getPasswordTypeEnum();

        // Assert
        assertEquals(User.PasswordType.BCRYPT, result);
    }

    @Test
    void getPasswordTypeEnum_WithNullType_ReturnsDefaultMD5() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .passwordType(null)
                .build();

        // Act
        User.PasswordType result = request.getPasswordTypeEnum();

        // Assert
        assertEquals(User.PasswordType.MD5, result);
    }

    @Test
    void getPasswordTypeEnum_WithEmptyType_ReturnsDefaultMD5() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .passwordType("")
                .build();

        // Act
        User.PasswordType result = request.getPasswordTypeEnum();

        // Assert
        assertEquals(User.PasswordType.MD5, result);
    }

    @Test
    void getPasswordTypeEnum_WithWhitespaceType_ReturnsDefaultMD5() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .passwordType("   ")
                .build();

        // Act
        User.PasswordType result = request.getPasswordTypeEnum();

        // Assert
        assertEquals(User.PasswordType.MD5, result);
    }

    @Test
    void getPasswordTypeEnum_WithDifferentCases_ReturnsCorrectEnum() {
        // Test various cases
        String[] testCases = {"bcrypt", "BCRYPT", "Bcrypt", "bCrYpT"};

        for (String testCase : testCases) {
            // Arrange
            CreateUserRequest request = CreateUserRequest.builder()
                    .passwordType(testCase)
                    .build();

            // Act
            User.PasswordType result = request.getPasswordTypeEnum();

            // Assert
            assertEquals(User.PasswordType.BCRYPT, result);
        }
    }

    @Test
    void getStatusEnum_WithValidStatus_ReturnsCorrectEnum() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .status("ACTIVE")
                .build();

        // Act
        User.UserStatus result = request.getStatusEnum();

        // Assert
        assertEquals(User.UserStatus.ACTIVE, result);
    }

    @Test
    void getStatusEnum_WithNullStatus_ReturnsDefaultACTIVE() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .status(null)
                .build();

        // Act
        User.UserStatus result = request.getStatusEnum();

        // Assert
        assertEquals(User.UserStatus.ACTIVE, result);
    }

    @Test
    void getStatusEnum_WithEmptyStatus_ReturnsDefaultACTIVE() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .status("")
                .build();

        // Act
        User.UserStatus result = request.getStatusEnum();

        // Assert
        assertEquals(User.UserStatus.ACTIVE, result);
    }

    @Test
    void getStatusEnum_WithWhitespaceStatus_ReturnsDefaultACTIVE() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .status("   ")
                .build();

        // Act
        User.UserStatus result = request.getStatusEnum();

        // Assert
        assertEquals(User.UserStatus.ACTIVE, result);
    }

    @Test
    void getStatusEnum_WithDifferentCases_ReturnsCorrectEnum() {
        // Test various cases
        String[] testCases = {"active", "ACTIVE", "Active", "aCtIvE"};

        for (String testCase : testCases) {
            // Arrange
            CreateUserRequest request = CreateUserRequest.builder()
                    .status(testCase)
                    .build();

            // Act
            User.UserStatus result = request.getStatusEnum();

            // Assert
            assertEquals(User.UserStatus.ACTIVE, result);
        }
    }

    @Test
    void getStatusEnum_WithInvalidStatus_ReturnsDefaultACTIVE() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .status("INVALID_STATUS")
                .build();

        // Act
        User.UserStatus result = request.getStatusEnum();

        // Assert
        assertEquals(User.UserStatus.ACTIVE, result);
    }

    @Test
    void getStatusEnum_WithAllValidStatuses_ReturnsCorrectEnums() {
        // Test all valid status values
        String[] statuses = {"ACTIVE", "INACTIVE", "SUSPENDED"};

        for (String status : statuses) {
            // Arrange
            CreateUserRequest request = CreateUserRequest.builder()
                    .status(status)
                    .build();

            // Act
            User.UserStatus result = request.getStatusEnum();

            // Assert
            assertEquals(User.UserStatus.valueOf(status), result);
        }
    }

    @Test
    void equals_WhenDifferentPasswordType_ReturnsFalse() {
        // Arrange
        CreateUserRequest request1 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        CreateUserRequest request2 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("SHA256")
                .status("ACTIVE")
                .build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void equals_WhenDifferentStatus_ReturnsFalse() {
        // Arrange
        CreateUserRequest request1 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("ACTIVE")
                .build();

        CreateUserRequest request2 = CreateUserRequest.builder()
                .username("testuser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .passwordType("BCRYPT")
                .status("INACTIVE")
                .build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }
}
