package com.example.login.service;

import com.example.login.entity.User;
import com.example.login.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserLookupServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private UserLookupService userLookupService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .name("Test User")
                .passwordHash("hashedpassword")
                .passwordSalt("salt")
                .passwordType(User.PasswordType.MD5)
                .status(User.UserStatus.ACTIVE)
                .dateCreated(LocalDateTime.now())
                .build();
    }

    @Test
    void findByUsername_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userLookupService.findByUsername("testuser");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void findByUsername_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When
        Optional<User> result = userLookupService.findByUsername("nonexistent");

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void findByUsername_WhenUsernameIsNull_ShouldReturnEmpty() {
        // When
        Optional<User> result = userLookupService.findByUsername(null);

        // Then
        assertThat(result).isEmpty();
        verify(userRepository, never()).findByUsername(anyString());
    }

    @Test
    void authenticateByUsername_WhenValidCredentials_ShouldReturnUser() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordService.isValid(any(User.PasswordType.class), any(), any(), any())).thenReturn(true);

        // When
        Optional<User> result = userLookupService.authenticateByUsername("testuser", "password");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
        verify(passwordService).isValid(testUser.getPasswordType(), testUser.getPasswordSalt(), "password", testUser.getPasswordHash());
    }

    @Test
    void authenticateByUsername_WhenInvalidPassword_ShouldReturnEmpty() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordService.isValid(any(User.PasswordType.class), any(), any(), any())).thenReturn(false);

        // When
        Optional<User> result = userLookupService.authenticateByUsername("testuser", "wrongpassword");

        // Then
        assertThat(result).isEmpty();
        verify(passwordService).isValid(testUser.getPasswordType(), testUser.getPasswordSalt(), "wrongpassword", testUser.getPasswordHash());
    }

    @Test
    void authenticateByUsername_WhenUserInactive_ShouldReturnEmpty() {
        // Given
        testUser.setStatus(User.UserStatus.INACTIVE);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userLookupService.authenticateByUsername("testuser", "password");

        // Then
        assertThat(result).isEmpty();
        verify(passwordService, never()).isValid(any(User.PasswordType.class), any(), any(), any());
    }

    @Test
    void validatePassword_WhenValidPassword_ShouldReturnTrue() {
        // Given
        when(passwordService.isValid(any(User.PasswordType.class), any(), any(), any())).thenReturn(true);

        // When
        boolean result = userLookupService.validatePassword(testUser, "password");

        // Then
        assertThat(result).isTrue();
        verify(passwordService).isValid(testUser.getPasswordType(), testUser.getPasswordSalt(), "password", testUser.getPasswordHash());
    }

    @Test
    void validatePassword_WhenInvalidPassword_ShouldReturnFalse() {
        // Given
        when(passwordService.isValid(any(User.PasswordType.class), any(), any(), any())).thenReturn(false);

        // When
        boolean result = userLookupService.validatePassword(testUser, "wrongpassword");

        // Then
        assertThat(result).isFalse();
        verify(passwordService).isValid(testUser.getPasswordType(), testUser.getPasswordSalt(), "wrongpassword", testUser.getPasswordHash());
    }
}
