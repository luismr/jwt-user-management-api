package com.example.login.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        User newUser = new User();

        // Then
        assertNull(newUser.getId());
        assertNull(newUser.getUsername());
        assertNull(newUser.getEmail());
        assertEquals(User.PasswordType.MD5, newUser.getPasswordType());
        assertEquals(User.UserStatus.ACTIVE, newUser.getStatus());
    }

    @Test
    void testParameterizedConstructor() {
        // Given & When
        User newUser = new User("testuser", "hashedpass", "salt", "Test User", "test@example.com");

        // Then
        assertEquals("testuser", newUser.getUsername());
        assertEquals("hashedpass", newUser.getPasswordHash());
        assertEquals("salt", newUser.getPasswordSalt());
        assertEquals("Test User", newUser.getName());
        assertEquals("test@example.com", newUser.getEmail());
        assertEquals(User.PasswordType.MD5, newUser.getPasswordType());
        assertEquals(User.UserStatus.ACTIVE, newUser.getStatus());
    }

    @Test
    void testGettersAndSetters() {
        // Given
        Long id = 1L;
        Long idClient = 100L;
        String username = "admin";
        String passwordHash = "$2y$10$hash";
        String passwordSalt = "salt123";
        User.PasswordType passwordType = User.PasswordType.BCRYPT;
        String name = "Administrator";
        String email = "admin@example.com";
        User.UserStatus status = User.UserStatus.ACTIVE;
        LocalDateTime lastLogin = LocalDateTime.now();
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        // When
        user.setId(id);
        user.setIdClient(idClient);
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setPasswordSalt(passwordSalt);
        user.setPasswordType(passwordType);
        user.setName(name);
        user.setEmail(email);
        user.setStatus(status);
        user.setDateLastLogin(lastLogin);
        user.setDateCreated(created);
        user.setDateUpdated(updated);

        // Then
        assertEquals(id, user.getId());
        assertEquals(idClient, user.getIdClient());
        assertEquals(username, user.getUsername());
        assertEquals(passwordHash, user.getPasswordHash());
        assertEquals(passwordSalt, user.getPasswordSalt());
        assertEquals(passwordType, user.getPasswordType());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(status, user.getStatus());
        assertEquals(lastLogin, user.getDateLastLogin());
        assertEquals(created, user.getDateCreated());
        assertEquals(updated, user.getDateUpdated());
    }

    @Test
    void testToString() {
        // Given
        user.setId(1L);
        user.setIdClient(100L);
        user.setUsername("testuser");
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setStatus(User.UserStatus.ACTIVE);
        LocalDateTime now = LocalDateTime.now();
        user.setDateCreated(now);
        user.setDateUpdated(now);

        // When
        String result = user.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("User("));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("idClient=100"));
        assertTrue(result.contains("username=testuser"));
        assertTrue(result.contains("name=Test User"));
        assertTrue(result.contains("email=test@example.com"));
        assertTrue(result.contains("status=ACTIVE"));
    }

    @Test
    void testDefaultValues() {
        // Given & When
        User newUser = new User();

        // Then
        assertEquals(User.PasswordType.MD5, newUser.getPasswordType());
        assertEquals(User.UserStatus.ACTIVE, newUser.getStatus());
    }

    @Test
    void testNullValues() {
        // Given & When
        User newUser = new User();

        // Then - nullable fields should accept null
        assertDoesNotThrow(() -> {
            newUser.setIdClient(null);
            newUser.setDateLastLogin(null);
        });

        assertNull(newUser.getIdClient());
        assertNull(newUser.getDateLastLogin());
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("test");

        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("test");

        User user3 = new User();
        user3.setId(2L);
        user3.setUsername("different");

        // Note: With Lombok @EqualsAndHashCode(onlyExplicitlyIncluded = true),
        // only the ID field is used for equals/hashCode
        assertEquals(user1, user2); // Same ID (1L), so they are equal
        assertEquals(user1.hashCode(), user2.hashCode()); // Same ID, same hashCode
        assertNotEquals(user1, user3); // Different ID (2L), so not equal
    }

    @Test
    void testBuilderPattern() {
        // Given & When
        User builtUser = User.builder()
                .id(1L)
                .idClient(100L)
                .username("builderuser")
                .passwordHash("hashedpass")
                .passwordSalt("salt123")
                .passwordType(User.PasswordType.BCRYPT)
                .name("Builder User")
                .email("builder@example.com")
                .status(User.UserStatus.ACTIVE)
                .dateLastLogin(LocalDateTime.now())
                .dateCreated(LocalDateTime.now().minusDays(1))
                .dateUpdated(LocalDateTime.now())
                .build();

        // Then
        assertEquals(1L, builtUser.getId());
        assertEquals(100L, builtUser.getIdClient());
        assertEquals("builderuser", builtUser.getUsername());
        assertEquals("hashedpass", builtUser.getPasswordHash());
        assertEquals("salt123", builtUser.getPasswordSalt());
        assertEquals(User.PasswordType.BCRYPT, builtUser.getPasswordType());
        assertEquals("Builder User", builtUser.getName());
        assertEquals("builder@example.com", builtUser.getEmail());
        assertEquals(User.UserStatus.ACTIVE, builtUser.getStatus());
        assertNotNull(builtUser.getDateLastLogin());
        assertNotNull(builtUser.getDateCreated());
        assertNotNull(builtUser.getDateUpdated());
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        Long id = 1L;
        Long idClient = 100L;
        String username = "testuser";
        String passwordHash = "hashedpass";
        String passwordSalt = "salt123";
        User.PasswordType passwordType = User.PasswordType.SHA256;
        String name = "Test User";
        String email = "test@example.com";
        User.UserStatus status = User.UserStatus.INACTIVE;
        LocalDateTime dateLastLogin = LocalDateTime.now();
        LocalDateTime dateCreated = LocalDateTime.now().minusDays(1);
        LocalDateTime dateUpdated = LocalDateTime.now();

        // When
        User user = new User(id, idClient, null, username, passwordHash, passwordSalt, 
                passwordType, name, email, status, dateLastLogin, dateCreated, dateUpdated, null, null);

        // Then
        assertEquals(id, user.getId());
        assertEquals(idClient, user.getIdClient());
        assertEquals(username, user.getUsername());
        assertEquals(passwordHash, user.getPasswordHash());
        assertEquals(passwordSalt, user.getPasswordSalt());
        assertEquals(passwordType, user.getPasswordType());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(status, user.getStatus());
        assertEquals(dateLastLogin, user.getDateLastLogin());
        assertEquals(dateCreated, user.getDateCreated());
        assertEquals(dateUpdated, user.getDateUpdated());
    }

    @Test
    void testPasswordTypeFromString() {
        // Test valid cases
        assertEquals(User.PasswordType.BCRYPT, User.PasswordType.fromString("BCRYPT"));
        assertEquals(User.PasswordType.BCRYPT, User.PasswordType.fromString("bcrypt"));
        assertEquals(User.PasswordType.SHA256, User.PasswordType.fromString("SHA256"));
        assertEquals(User.PasswordType.SHA256, User.PasswordType.fromString("sha256"));
        assertEquals(User.PasswordType.MD5, User.PasswordType.fromString("MD5"));
        assertEquals(User.PasswordType.MD5, User.PasswordType.fromString("md5"));
        assertEquals(User.PasswordType.SHA512, User.PasswordType.fromString("SHA512"));
        assertEquals(User.PasswordType.SHA512, User.PasswordType.fromString("sha512"));
    }

    @Test
    void testPasswordTypeFromStringWithNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            User.PasswordType.fromString(null));
        
        assertEquals("Password type cannot be null", exception.getMessage());
    }

    @Test
    void testPasswordTypeFromStringWithInvalidValue() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            User.PasswordType.fromString("INVALID"));
        
        assertTrue(exception.getMessage().contains("Unknown password type: INVALID"));
        assertTrue(exception.getMessage().contains("Supported types:"));
    }

    @Test
    void testToStringExcludesSensitiveData() {
        // Given
        user.setId(1L);
        user.setUsername("testuser");
        user.setPasswordHash("sensitive_hash");
        user.setPasswordSalt("sensitive_salt");
        user.setName("Test User");
        user.setEmail("test@example.com");

        // When
        String result = user.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("username=testuser"));
        assertTrue(result.contains("name=Test User"));
        assertTrue(result.contains("email=test@example.com"));
        assertFalse(result.contains("sensitive_hash"));
        assertFalse(result.contains("sensitive_salt"));
    }

    @Test
    void testEqualsWithNullId() {
        // Given
        User user1 = new User();
        User user2 = new User();

        // When & Then
        assertEquals(user1, user2); // Both have null ID, so they are equal
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testEqualsWithDifferentNullId() {
        // Given
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        // user2 has null ID

        // When & Then
        assertNotEquals(user1, user2); // Different ID (1L vs null), so not equal
    }

    @Test
    void testEqualsWithSameObject() {
        // When & Then
        assertEquals(user, user);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Given
        String differentObject = "not a User";

        // When & Then
        assertNotEquals(user, differentObject);
    }

    @Test
    void testEqualsWithNull() {
        // When & Then
        assertNotEquals(user, null);
    }

    @Test
    void testMaxLengthFields() {
        // Given
        String maxLengthUsername = "a".repeat(128);
        String maxLengthName = "a".repeat(128);
        String maxLengthEmail = "a".repeat(120) + "@example.com"; // 128 chars total
        String maxLengthPasswordHash = "a".repeat(255);
        String maxLengthPasswordSalt = "a".repeat(255);

        // When
        user.setUsername(maxLengthUsername);
        user.setName(maxLengthName);
        user.setEmail(maxLengthEmail);
        user.setPasswordHash(maxLengthPasswordHash);
        user.setPasswordSalt(maxLengthPasswordSalt);

        // Then
        assertEquals(maxLengthUsername, user.getUsername());
        assertEquals(maxLengthName, user.getName());
        assertEquals(maxLengthEmail, user.getEmail());
        assertEquals(maxLengthPasswordHash, user.getPasswordHash());
        assertEquals(maxLengthPasswordSalt, user.getPasswordSalt());
    }

    @Test
    void testSpecialCharactersInFields() {
        // Given
        String specialUsername = "user@#$%^&*()_+-=[]{}|;':\",./<>?`~";
        String specialName = "User & Co. (Ltd.) - Special chars: @#$%";
        String specialEmail = "user+tag@example-domain.com";

        // When
        user.setUsername(specialUsername);
        user.setName(specialName);
        user.setEmail(specialEmail);

        // Then
        assertEquals(specialUsername, user.getUsername());
        assertEquals(specialName, user.getName());
        assertEquals(specialEmail, user.getEmail());
    }

    @Test
    void testUnicodeCharactersInFields() {
        // Given
        String unicodeUsername = "用户123";
        String unicodeName = "Usuario Español 中文用户 العميل العربي";
        String unicodeEmail = "usuario@例え.com";

        // When
        user.setUsername(unicodeUsername);
        user.setName(unicodeName);
        user.setEmail(unicodeEmail);

        // Then
        assertEquals(unicodeUsername, user.getUsername());
        assertEquals(unicodeName, user.getName());
        assertEquals(unicodeEmail, user.getEmail());
    }

    @Test
    void testWhitespaceInFields() {
        // Given
        String usernameWithSpaces = "  user  ";
        String nameWithSpaces = "  User Name  ";
        String emailWithSpaces = "  user@example.com  ";

        // When
        user.setUsername(usernameWithSpaces);
        user.setName(nameWithSpaces);
        user.setEmail(emailWithSpaces);

        // Then
        assertEquals(usernameWithSpaces, user.getUsername());
        assertEquals(nameWithSpaces, user.getName());
        assertEquals(emailWithSpaces, user.getEmail());
    }

    @Test
    void testEmptyStringsInFields() {
        // Given
        String emptyString = "";

        // When
        user.setUsername(emptyString);
        user.setName(emptyString);
        user.setEmail(emptyString);
        user.setPasswordHash(emptyString);
        user.setPasswordSalt(emptyString);

        // Then
        assertEquals(emptyString, user.getUsername());
        assertEquals(emptyString, user.getName());
        assertEquals(emptyString, user.getEmail());
        assertEquals(emptyString, user.getPasswordHash());
        assertEquals(emptyString, user.getPasswordSalt());
    }

    @Test
    void testAllPasswordTypes() {
        // Test all password types can be set
        user.setPasswordType(User.PasswordType.BCRYPT);
        assertEquals(User.PasswordType.BCRYPT, user.getPasswordType());

        user.setPasswordType(User.PasswordType.SHA256);
        assertEquals(User.PasswordType.SHA256, user.getPasswordType());

        user.setPasswordType(User.PasswordType.MD5);
        assertEquals(User.PasswordType.MD5, user.getPasswordType());

        user.setPasswordType(User.PasswordType.SHA512);
        assertEquals(User.PasswordType.SHA512, user.getPasswordType());
    }

    @Test
    void testAllUserStatuses() {
        // Test all user statuses can be set
        user.setStatus(User.UserStatus.ACTIVE);
        assertEquals(User.UserStatus.ACTIVE, user.getStatus());

        user.setStatus(User.UserStatus.INACTIVE);
        assertEquals(User.UserStatus.INACTIVE, user.getStatus());

        user.setStatus(User.UserStatus.SUSPENDED);
        assertEquals(User.UserStatus.SUSPENDED, user.getStatus());
    }

    @Test
    void testPasswordTypeValues() {
        // Test all password type values
        assertEquals("BCRYPT", User.PasswordType.BCRYPT.name());
        assertEquals("SHA256", User.PasswordType.SHA256.name());
        assertEquals("MD5", User.PasswordType.MD5.name());
        assertEquals("SHA512", User.PasswordType.SHA512.name());
    }

    @Test
    void testUserStatusValues() {
        // Test all user status values
        assertEquals("ACTIVE", User.UserStatus.ACTIVE.name());
        assertEquals("INACTIVE", User.UserStatus.INACTIVE.name());
        assertEquals("SUSPENDED", User.UserStatus.SUSPENDED.name());
    }

    @Test
    void testPasswordTypeValuesArray() {
        // Test password type values array
        User.PasswordType[] values = User.PasswordType.values();
        assertEquals(4, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(User.PasswordType.BCRYPT));
        assertTrue(java.util.Arrays.asList(values).contains(User.PasswordType.SHA256));
        assertTrue(java.util.Arrays.asList(values).contains(User.PasswordType.MD5));
        assertTrue(java.util.Arrays.asList(values).contains(User.PasswordType.SHA512));
    }

    @Test
    void testUserStatusValuesArray() {
        // Test user status values array
        User.UserStatus[] values = User.UserStatus.values();
        assertEquals(3, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(User.UserStatus.ACTIVE));
        assertTrue(java.util.Arrays.asList(values).contains(User.UserStatus.INACTIVE));
        assertTrue(java.util.Arrays.asList(values).contains(User.UserStatus.SUSPENDED));
    }

    @Test
    void testPasswordTypeValueOf() {
        // Test password type valueOf
        assertEquals(User.PasswordType.BCRYPT, User.PasswordType.valueOf("BCRYPT"));
        assertEquals(User.PasswordType.SHA256, User.PasswordType.valueOf("SHA256"));
        assertEquals(User.PasswordType.MD5, User.PasswordType.valueOf("MD5"));
        assertEquals(User.PasswordType.SHA512, User.PasswordType.valueOf("SHA512"));
    }

    @Test
    void testUserStatusValueOf() {
        // Test user status valueOf
        assertEquals(User.UserStatus.ACTIVE, User.UserStatus.valueOf("ACTIVE"));
        assertEquals(User.UserStatus.INACTIVE, User.UserStatus.valueOf("INACTIVE"));
        assertEquals(User.UserStatus.SUSPENDED, User.UserStatus.valueOf("SUSPENDED"));
    }

    @Test
    void testPasswordTypeValueOfWithInvalidValue() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            User.PasswordType.valueOf("INVALID"));
    }

    @Test
    void testUserStatusValueOfWithInvalidValue() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            User.UserStatus.valueOf("INVALID"));
    }
}
