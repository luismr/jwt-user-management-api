package com.example.login.controller;

import com.example.login.dto.CreateUserRequest;
import com.example.login.dto.LoginRequest;
import com.example.login.dto.LoginResponse;
import com.example.login.entity.User;
import com.example.login.repository.UserRepository;
import com.example.login.service.JwtService;
import com.example.login.service.PasswordService;
import com.example.login.service.UserLookupService;
import com.example.login.util.PasswordHashUtil.PasswordHashResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordService passwordService;

    @Mock
    private UserLookupService userLookupService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;
    private User testUser;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        // Setup test user
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .passwordHash("hashedPassword")
                .passwordSalt("salt")
                .passwordType(User.PasswordType.BCRYPT)
                .name("Test User")
                .email("test@example.com")
                .idClient(1L)
                .status(User.UserStatus.ACTIVE)
                .build();
    }

    @Test
    void login_ReturnsLoginInformation() {
        // Act
        ResponseEntity<Map<String, String>> response = authController.login();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Please provide credentials for authentication", response.getBody().get("message"));
        assertEquals("Use /api/** with Basic Auth (username: admin, password: admin123)", response.getBody().get("endpoints"));
    }

    @Test
    void loginPost_SuccessWithUsername() {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
                .usernameOrEmail("testuser")
                .password("password123")
                .build();
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        List<Long> clientIds = Arrays.asList(1L, 2L);
        String jwtToken = "test.jwt.token";

        when(userLookupService.authenticateByUsername("testuser", "password123"))
            .thenReturn(Optional.of(testUser));
        when(userLookupService.getUserRoles(testUser)).thenReturn(roles);
        when(userLookupService.getUserClientIds(testUser)).thenReturn(clientIds);
        when(jwtService.generateToken(testUser, roles, clientIds)).thenReturn(jwtToken);
        doNothing().when(userLookupService).updateLastLogin(testUser);

        // Act
        ResponseEntity<LoginResponse> response = authController.loginPost(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals(jwtToken, response.getBody().getToken());
        assertEquals(testUser.getUsername(), response.getBody().getUser().getUsername());

        verify(userLookupService).authenticateByUsername("testuser", "password123");
        verify(userLookupService).getUserRoles(testUser);
        verify(userLookupService).getUserClientIds(testUser);
        verify(jwtService).generateToken(testUser, roles, clientIds);
        verify(userLookupService).updateLastLogin(testUser);
    }

    @Test
    void loginPost_SuccessWithEmail() {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
                .usernameOrEmail("test@example.com")
                .password("password123")
                .build();
        List<String> roles = Arrays.asList("ROLE_USER");
        List<Long> clientIds = Arrays.asList(1L);
        String jwtToken = "test.jwt.token";

        when(userLookupService.authenticateByUsername("test@example.com", "password123"))
            .thenReturn(Optional.empty());
        when(userLookupService.authenticateByEmail("test@example.com", "password123"))
            .thenReturn(Optional.of(testUser));
        when(userLookupService.getUserRoles(testUser)).thenReturn(roles);
        when(userLookupService.getUserClientIds(testUser)).thenReturn(clientIds);
        when(jwtService.generateToken(testUser, roles, clientIds)).thenReturn(jwtToken);
        doNothing().when(userLookupService).updateLastLogin(testUser);

        // Act
        ResponseEntity<LoginResponse> response = authController.loginPost(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals(jwtToken, response.getBody().getToken());
        assertEquals(testUser.getUsername(), response.getBody().getUser().getUsername());

        verify(userLookupService).authenticateByUsername("test@example.com", "password123");
        verify(userLookupService).authenticateByEmail("test@example.com", "password123");
        verify(userLookupService).getUserRoles(testUser);
        verify(userLookupService).getUserClientIds(testUser);
        verify(jwtService).generateToken(testUser, roles, clientIds);
        verify(userLookupService).updateLastLogin(testUser);
    }

    @Test
    void loginPost_InvalidCredentials() {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
                .usernameOrEmail("testuser")
                .password("wrongpassword")
                .build();

        when(userLookupService.authenticateByUsername("testuser", "wrongpassword"))
            .thenReturn(Optional.empty());
        when(userLookupService.authenticateByEmail("testuser", "wrongpassword"))
            .thenReturn(Optional.empty());

        // Act
        ResponseEntity<LoginResponse> response = authController.loginPost(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Invalid username/email or password", response.getBody().getMessage());

        verify(userLookupService).authenticateByUsername("testuser", "wrongpassword");
        verify(userLookupService).authenticateByEmail("testuser", "wrongpassword");
        verify(userLookupService, never()).getUserRoles(any());
        verify(userLookupService, never()).getUserClientIds(any());
        verify(jwtService, never()).generateToken(any(), any(), any());
        verify(userLookupService, never()).updateLastLogin(any());
    }

    @Test
    void loginPost_InternalError() {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
                .usernameOrEmail("testuser")
                .password("password123")
                .build();

        when(userLookupService.authenticateByUsername("testuser", "password123"))
            .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<LoginResponse> response = authController.loginPost(loginRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("An error occurred during authentication", response.getBody().getMessage());

        verify(userLookupService).authenticateByUsername("testuser", "password123");
        verify(userLookupService, never()).getUserRoles(any());
        verify(userLookupService, never()).getUserClientIds(any());
        verify(jwtService, never()).generateToken(any(), any(), any());
        verify(userLookupService, never()).updateLastLogin(any());
    }

    @Test
    void logout_Success() {
        // Arrange
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer test.jwt.token");

        // Act
        ResponseEntity<Map<String, String>> response = authController.logout(mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Logout successful", response.getBody().get("message"));
        assertEquals("logged_out", response.getBody().get("status"));

        verify(jwtService).blacklistToken("test.jwt.token");
        verify(mockRequest).getHeader("Authorization");
    }

    @Test
    void logout_NoToken() {
        // Arrange
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn(null);

        // Act
        ResponseEntity<Map<String, String>> response = authController.logout(mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Logout successful", response.getBody().get("message"));
        assertEquals("logged_out", response.getBody().get("status"));

        verify(jwtService, never()).blacklistToken(anyString());
        verify(mockRequest).getHeader("Authorization");
    }

    @Test
    void logoutGet_ReturnsLogoutInformation() {
        // Act
        ResponseEntity<Map<String, String>> response = authController.logoutGet();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Logout successful", response.getBody().get("message"));
        assertEquals("logged_out", response.getBody().get("status"));
    }

    @Test
    void getUserByUsername_Success() {
        // Arrange
        when(userLookupService.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        ResponseEntity<User> response = authController.getUserByUsername("testuser");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUser.getId(), response.getBody().getId());
        assertEquals(testUser.getUsername(), response.getBody().getUsername());
        assertEquals(testUser.getEmail(), response.getBody().getEmail());

        verify(userLookupService).findByUsername("testuser");
    }

    @Test
    void getUserByUsername_NotFound() {
        // Arrange
        when(userLookupService.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = authController.getUserByUsername("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(userLookupService).findByUsername("nonexistent");
    }

    @Test
    void getUserByEmail_Success() {
        // Arrange
        when(userLookupService.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        ResponseEntity<User> response = authController.getUserByEmail("test@example.com");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUser.getId(), response.getBody().getId());
        assertEquals(testUser.getUsername(), response.getBody().getUsername());
        assertEquals(testUser.getEmail(), response.getBody().getEmail());

        verify(userLookupService).findByEmail("test@example.com");
    }

    @Test
    void getUserByEmail_NotFound() {
        // Arrange
        when(userLookupService.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = authController.getUserByEmail("nonexistent@example.com");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(userLookupService).findByEmail("nonexistent@example.com");
    }

    @Test
    void getUserById_Success() {
        // Arrange
        when(userLookupService.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        ResponseEntity<User> response = authController.getUserById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUser.getId(), response.getBody().getId());
        assertEquals(testUser.getUsername(), response.getBody().getUsername());
        assertEquals(testUser.getEmail(), response.getBody().getEmail());

        verify(userLookupService).findById(1L);
    }

    @Test
    void getUserById_NotFound() {
        // Arrange
        when(userLookupService.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = authController.getUserById(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(userLookupService).findById(999L);
    }

    @Test
    void validatePassword_Success() {
        // Arrange
        when(userLookupService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userLookupService.validatePassword(testUser, "password123")).thenReturn(true);

        // Act
        ResponseEntity<Map<String, Object>> response = authController.validatePassword("testuser", "password123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("valid"));
        assertEquals("testuser", response.getBody().get("username"));
        assertEquals("Password is valid", response.getBody().get("message"));

        verify(userLookupService).findByUsername("testuser");
        verify(userLookupService).validatePassword(testUser, "password123");
    }

    @Test
    void validatePassword_InvalidPassword() {
        // Arrange
        when(userLookupService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userLookupService.validatePassword(testUser, "wrongpassword")).thenReturn(false);

        // Act
        ResponseEntity<Map<String, Object>> response = authController.validatePassword("testuser", "wrongpassword");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("valid"));
        assertEquals("testuser", response.getBody().get("username"));
        assertEquals("Password is invalid", response.getBody().get("message"));

        verify(userLookupService).findByUsername("testuser");
        verify(userLookupService).validatePassword(testUser, "wrongpassword");
    }

    @Test
    void validatePassword_UserNotFound() {
        // Arrange
        when(userLookupService.findByUsername("nonexistent")).thenReturn(Optional.empty());
        when(userLookupService.findByEmail("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Map<String, Object>> response = authController.validatePassword("nonexistent", "password123");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(userLookupService).findByUsername("nonexistent");
        verify(userLookupService).findByEmail("nonexistent");
        verify(userLookupService, never()).validatePassword(any(), anyString());
    }

    @Test
    void createUser_Success() {
        // Arrange
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username("newuser")
                .password("Password123!")
                .name("New User")
                .email("newuser@example.com")
                .idClient(1L)
                .status("ACTIVE")
                .passwordType("BCRYPT")
                .build();

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(passwordService.isPasswordSecure("Password123!")).thenReturn(true);
        PasswordHashResult hashResult = new PasswordHashResult("newSalt", "newHash");
        when(passwordService.generateHash(eq(User.PasswordType.BCRYPT), eq("Password123!")))
                .thenReturn(hashResult);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(2L);
            return savedUser;
        });

        // Act
        ResponseEntity<?> response = authController.createUser(createUserRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof User);
        User createdUser = (User) response.getBody();
        assertEquals("newuser", createdUser.getUsername());
        assertEquals("New User", createdUser.getName());
        assertEquals("newuser@example.com", createdUser.getEmail());
        assertNull(createdUser.getPasswordHash());
        assertNull(createdUser.getPasswordSalt());

        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmail("newuser@example.com");
        verify(passwordService).isPasswordSecure("Password123!");
        verify(passwordService).generateHash(eq(User.PasswordType.BCRYPT), eq("Password123!"));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_UsernameExists() {
        // Arrange
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username("existinguser")
                .password("Password123!")
                .name("New User")
                .email("newuser@example.com")
                .idClient(1L)
                .status("ACTIVE")
                .passwordType("BCRYPT")
                .build();

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.createUser(createUserRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("Username already exists", responseBody.get("error"));
        assertEquals("username", responseBody.get("field"));

        verify(userRepository).existsByUsername("existinguser");
        verify(userRepository, never()).existsByEmail(anyString());
        verify(passwordService, never()).isPasswordSecure(anyString());
        verify(passwordService, never()).generateHash(any(User.PasswordType.class), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_EmailExists() {
        // Arrange
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username("newuser")
                .password("Password123!")
                .name("New User")
                .email("existing@example.com")
                .idClient(1L)
                .status("ACTIVE")
                .passwordType("BCRYPT")
                .build();

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.createUser(createUserRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("Email already exists", responseBody.get("error"));
        assertEquals("email", responseBody.get("field"));

        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmail("existing@example.com");
        verify(passwordService, never()).isPasswordSecure(anyString());
        verify(passwordService, never()).generateHash(any(User.PasswordType.class), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_WeakPassword() {
        // Arrange
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username("newuser")
                .password("weak")
                .name("New User")
                .email("newuser@example.com")
                .idClient(1L)
                .status("ACTIVE")
                .passwordType("BCRYPT")
                .build();

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(passwordService.isPasswordSecure("weak")).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.createUser(createUserRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("Password does not meet security requirements. Must be at least 8 characters with uppercase, lowercase, digit, and special character", responseBody.get("error"));
        assertEquals("password", responseBody.get("field"));

        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmail("newuser@example.com");
        verify(passwordService).isPasswordSecure("weak");
        verify(passwordService, never()).generateHash(any(User.PasswordType.class), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_InternalError() {
        // Arrange
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username("newuser")
                .password("Password123!")
                .name("New User")
                .email("newuser@example.com")
                .idClient(1L)
                .status("ACTIVE")
                .passwordType("BCRYPT")
                .build();

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(passwordService.isPasswordSecure("Password123!")).thenReturn(true);
        when(passwordService.generateHash(eq(User.PasswordType.BCRYPT), eq("Password123!")))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = authController.createUser(createUserRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("An error occurred while creating the user", responseBody.get("error"));

        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmail("newuser@example.com");
        verify(passwordService).isPasswordSecure("Password123!");
        verify(passwordService).generateHash(eq(User.PasswordType.BCRYPT), eq("Password123!"));
        verify(userRepository, never()).save(any(User.class));
    }
}