package com.example.login.controller;

import com.example.login.dto.ChangePasswordRequest;
import com.example.login.entity.User;
import com.example.login.repository.UserRepository;
import com.example.login.service.PasswordService;
import com.example.login.service.UserLookupService;
import com.example.login.util.PasswordHashUtil.PasswordHashResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerChangePasswordTest {

    @Mock
    private UserRepository userRepository;

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
    private ChangePasswordRequest validRequest;

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

        // Setup valid request
        validRequest = ChangePasswordRequest.builder()
                .currentPassword("OldPassword123!")
                .newPassword("NewPassword123!")
                .confirmPassword("NewPassword123!")
                .build();

        // Setup security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void changePassword_Success() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(userLookupService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userLookupService.validatePassword(testUser, "OldPassword123!")).thenReturn(true);
        when(passwordService.isPasswordSecure("NewPassword123!")).thenReturn(true);
        
        PasswordHashResult hashResult = new PasswordHashResult("newSalt", "newHash");
        when(passwordService.generateHash(eq(User.PasswordType.BCRYPT), eq("NewPassword123!"))).thenReturn(hashResult);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        ResponseEntity<?> response = authController.changePassword(validRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("Password changed successfully", responseBody.get("message"));
        assertEquals("testuser", responseBody.get("username"));

        verify(userLookupService).findByUsername("testuser");
        verify(userLookupService).validatePassword(testUser, "OldPassword123!");
        verify(passwordService).isPasswordSecure("NewPassword123!");
        verify(passwordService).generateHash(eq(User.PasswordType.BCRYPT), eq("NewPassword123!"));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_Unauthenticated() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        ResponseEntity<?> response = authController.changePassword(validRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("Authentication required", responseBody.get("error"));

        verify(userLookupService, never()).findByUsername(anyString());
    }

    @Test
    void changePassword_UserNotFound() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(userLookupService.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.changePassword(validRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("User not found", responseBody.get("error"));

        verify(userLookupService).findByUsername("testuser");
        verify(userLookupService, never()).validatePassword(any(), anyString());
    }

    @Test
    void changePassword_PasswordConfirmationMismatch() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .currentPassword("OldPassword123!")
                .newPassword("NewPassword123!")
                .confirmPassword("DifferentPassword123!")
                .build();

        when(userLookupService.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        ResponseEntity<?> response = authController.changePassword(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("New password and confirmation do not match", responseBody.get("error"));
        assertEquals("confirmPassword", responseBody.get("field"));

        verify(userLookupService).findByUsername("testuser");
        verify(userLookupService, never()).validatePassword(any(), anyString());
    }

    @Test
    void changePassword_SamePassword() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .currentPassword("SamePassword123!")
                .newPassword("SamePassword123!")
                .confirmPassword("SamePassword123!")
                .build();

        when(userLookupService.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        ResponseEntity<?> response = authController.changePassword(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("New password must be different from current password", responseBody.get("error"));
        assertEquals("newPassword", responseBody.get("field"));

        verify(userLookupService).findByUsername("testuser");
        verify(userLookupService, never()).validatePassword(any(), anyString());
    }

    @Test
    void changePassword_InvalidCurrentPassword() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(userLookupService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userLookupService.validatePassword(testUser, "OldPassword123!")).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.changePassword(validRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("Current password is incorrect", responseBody.get("error"));
        assertEquals("currentPassword", responseBody.get("field"));

        verify(userLookupService).findByUsername("testuser");
        verify(userLookupService).validatePassword(testUser, "OldPassword123!");
        verify(passwordService, never()).isPasswordSecure(anyString());
    }

    @Test
    void changePassword_WeakNewPassword() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(userLookupService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userLookupService.validatePassword(testUser, "OldPassword123!")).thenReturn(true);
        when(passwordService.isPasswordSecure("NewPassword123!")).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.changePassword(validRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertTrue(responseBody.get("error").toString().contains("New password does not meet security requirements"));
        assertEquals("newPassword", responseBody.get("field"));

        verify(userLookupService).findByUsername("testuser");
        verify(userLookupService).validatePassword(testUser, "OldPassword123!");
        verify(passwordService).isPasswordSecure("NewPassword123!");
        verify(passwordService, never()).generateHash(any(User.PasswordType.class), anyString());
    }

    @Test
    void changePassword_IllegalArgumentException() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(userLookupService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userLookupService.validatePassword(testUser, "OldPassword123!")).thenReturn(true);
        when(passwordService.isPasswordSecure("NewPassword123!")).thenReturn(true);
        when(passwordService.generateHash(eq(User.PasswordType.BCRYPT), eq("NewPassword123!")))
                .thenThrow(new IllegalArgumentException("Invalid password type"));

        // Act
        ResponseEntity<?> response = authController.changePassword(validRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("Invalid password type", responseBody.get("error"));

        verify(userLookupService).findByUsername("testuser");
        verify(userLookupService).validatePassword(testUser, "OldPassword123!");
        verify(passwordService).isPasswordSecure("NewPassword123!");
        verify(passwordService).generateHash(eq(User.PasswordType.BCRYPT), eq("NewPassword123!"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_GeneralException() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(userLookupService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userLookupService.validatePassword(testUser, "OldPassword123!")).thenReturn(true);
        when(passwordService.isPasswordSecure("NewPassword123!")).thenReturn(true);
        when(passwordService.generateHash(eq(User.PasswordType.BCRYPT), eq("NewPassword123!")))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = authController.changePassword(validRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("An error occurred while changing the password", responseBody.get("error"));

        verify(userLookupService).findByUsername("testuser");
        verify(userLookupService).validatePassword(testUser, "OldPassword123!");
        verify(passwordService).isPasswordSecure("NewPassword123!");
        verify(passwordService).generateHash(eq(User.PasswordType.BCRYPT), eq("NewPassword123!"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_NotAuthenticated() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.changePassword(validRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("Authentication required", responseBody.get("error"));

        verify(userLookupService, never()).findByUsername(anyString());
    }

    @Test
    void changePassword_UpdatesUserPasswordAndSalt() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(userLookupService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userLookupService.validatePassword(testUser, "OldPassword123!")).thenReturn(true);
        when(passwordService.isPasswordSecure("NewPassword123!")).thenReturn(true);
        
        PasswordHashResult hashResult = new PasswordHashResult("newSalt", "newHash");
        when(passwordService.generateHash(eq(User.PasswordType.BCRYPT), eq("NewPassword123!"))).thenReturn(hashResult);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertEquals("newHash", savedUser.getPasswordHash());
            assertEquals("newSalt", savedUser.getPasswordSalt());
            return savedUser;
        });

        // Act
        ResponseEntity<?> response = authController.changePassword(validRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_UsesSamePasswordType() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        testUser.setPasswordType(User.PasswordType.SHA256);
        when(userLookupService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userLookupService.validatePassword(testUser, "OldPassword123!")).thenReturn(true);
        when(passwordService.isPasswordSecure("NewPassword123!")).thenReturn(true);
        
        PasswordHashResult hashResult = new PasswordHashResult("newSalt", "newHash");
        when(passwordService.generateHash(eq(User.PasswordType.SHA256), eq("NewPassword123!"))).thenReturn(hashResult);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        ResponseEntity<?> response = authController.changePassword(validRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(passwordService).generateHash(eq(User.PasswordType.SHA256), eq("NewPassword123!"));
    }
}
