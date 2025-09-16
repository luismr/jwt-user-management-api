package com.example.login.service;

import com.example.login.config.JwtConfig;
import com.example.login.entity.User;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtConfig jwtConfig;
    private JwtService jwtService;
    private User testUser;
    private List<String> testRoles;
    private List<Long> testClientIds;

    @BeforeEach
    void setUp() {
        // Create real JWT config for testing
        jwtConfig = new JwtConfig();
        jwtConfig.setSecret("testSecretKey123456789012345678901234567890123456789012345678901234567890");
        jwtConfig.setExpiration(86400000L); // 24 hours
        jwtConfig.setIssuer("test-app");
        
        // Create a real secret key for testing
        SecretKey realSecretKey = Jwts.SIG.HS512.key().build();
        
        jwtService = new JwtService(jwtConfig, realSecretKey);
        
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .name("Test User")
                .build();
        
        testRoles = List.of("ROLE_ADMIN", "ROLE_USER");
        testClientIds = List.of(1L, 2L);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        // When
        String token = jwtService.generateToken(testUser, testRoles, testClientIds);

        // Then
        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3); // JWT has 3 parts
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        // Given
        String token = jwtService.generateToken(testUser, testRoles, testClientIds);

        // When
        String username = jwtService.extractUsername(token);

        // Then
        assertThat(username).isEqualTo("testuser");
    }

    @Test
    void extractRoles_ShouldReturnCorrectRoles() {
        // Given
        String token = jwtService.generateToken(testUser, testRoles, testClientIds);

        // When
        List<String> roles = jwtService.extractRoles(token);

        // Then
        assertThat(roles).containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    void extractClients_ShouldReturnCorrectClientIds() {
        // Given
        String token = jwtService.generateToken(testUser, testRoles, testClientIds);

        // When
        List<Long> clients = jwtService.extractClients(token);

        // Then
        assertThat(clients).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void isTokenExpired_WithValidToken_ShouldReturnFalse() {
        // Given
        String token = jwtService.generateToken(testUser, testRoles, testClientIds);

        // When
        Boolean isExpired = jwtService.isTokenExpired(token);

        // Then
        assertThat(isExpired).isFalse();
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Given
        String token = jwtService.generateToken(testUser, testRoles, testClientIds);

        // When
        Boolean isValid = jwtService.validateToken(token, "testuser");

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    void validateToken_WithWrongUsername_ShouldReturnFalse() {
        // Given
        String token = jwtService.generateToken(testUser, testRoles, testClientIds);

        // When
        Boolean isValid = jwtService.validateToken(token, "wronguser");

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void validateToken_WithBlacklistedToken_ShouldReturnFalse() {
        // Given
        String token = jwtService.generateToken(testUser, testRoles, testClientIds);
        jwtService.blacklistToken(token);

        // When
        Boolean isValid = jwtService.validateToken(token, "testuser");

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void blacklistToken_ShouldAddTokenToBlacklist() {
        // Given
        String token = jwtService.generateToken(testUser, testRoles, testClientIds);

        // When
        jwtService.blacklistToken(token);

        // Then
        Boolean isBlacklisted = jwtService.isTokenBlacklisted(token);
        assertThat(isBlacklisted).isTrue();
    }

    @Test
    void extractExpiration_ShouldReturnValidDate() {
        // Given
        String token = jwtService.generateToken(testUser, testRoles, testClientIds);

        // When
        Date expiration = jwtService.extractExpiration(token);

        // Then
        assertThat(expiration).isNotNull();
        assertThat(expiration).isAfter(new Date());
    }
}
