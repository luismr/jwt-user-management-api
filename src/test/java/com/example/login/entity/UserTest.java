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
    void testPasswordTypeEnum() {
        // Test all password types
        assertEquals(4, User.PasswordType.values().length);
        assertTrue(java.util.Arrays.asList(User.PasswordType.values()).contains(User.PasswordType.BCRYPT));
        assertTrue(java.util.Arrays.asList(User.PasswordType.values()).contains(User.PasswordType.SHA256));
        assertTrue(java.util.Arrays.asList(User.PasswordType.values()).contains(User.PasswordType.MD5));
        assertTrue(java.util.Arrays.asList(User.PasswordType.values()).contains(User.PasswordType.SHA512));
    }

    @Test
    void testUserStatusEnum() {
        // Test all user statuses
        assertEquals(3, User.UserStatus.values().length);
        assertTrue(java.util.Arrays.asList(User.UserStatus.values()).contains(User.UserStatus.ACTIVE));
        assertTrue(java.util.Arrays.asList(User.UserStatus.values()).contains(User.UserStatus.INACTIVE));
        assertTrue(java.util.Arrays.asList(User.UserStatus.values()).contains(User.UserStatus.SUSPENDED));
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
    void testPasswordTypeValues() {
        // Test individual password type values
        assertEquals("BCRYPT", User.PasswordType.BCRYPT.name());
        assertEquals("SHA256", User.PasswordType.SHA256.name());
        assertEquals("MD5", User.PasswordType.MD5.name());
        assertEquals("SHA512", User.PasswordType.SHA512.name());
    }

    @Test
    void testUserStatusValues() {
        // Test individual user status values
        assertEquals("ACTIVE", User.UserStatus.ACTIVE.name());
        assertEquals("INACTIVE", User.UserStatus.INACTIVE.name());
        assertEquals("SUSPENDED", User.UserStatus.SUSPENDED.name());
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
    void testBuilderPattern() {
        // Given
        User newUser = new User();

        // When - simulate builder pattern
        newUser.setUsername("builder");
        newUser.setEmail("builder@example.com");
        newUser.setPasswordHash("hash");
        newUser.setPasswordSalt("salt");
        newUser.setName("Builder User");
        newUser.setPasswordType(User.PasswordType.BCRYPT);
        newUser.setStatus(User.UserStatus.ACTIVE);

        // Then
        assertEquals("builder", newUser.getUsername());
        assertEquals("builder@example.com", newUser.getEmail());
        assertEquals("hash", newUser.getPasswordHash());
        assertEquals("salt", newUser.getPasswordSalt());
        assertEquals("Builder User", newUser.getName());
        assertEquals(User.PasswordType.BCRYPT, newUser.getPasswordType());
        assertEquals(User.UserStatus.ACTIVE, newUser.getStatus());
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
}
