package com.example.login.repository;

import com.example.login.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User testUser1;
    private User testUser2;
    private User testUser3;

    @BeforeEach
    void setUp() {
        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setIdClient(1L);
        testUser1.setUsername("admin");
        testUser1.setPasswordHash("$2y$10$wJalrXUtnFEMI/K7MDENGuEv6Q9r1tGzQ6Q0eQFRCGDpa2BkLomqG");
        testUser1.setPasswordSalt("adminSALT");
        testUser1.setPasswordType(User.PasswordType.BCRYPT);
        testUser1.setName("Administrator");
        testUser1.setEmail("admin@example.com");
        testUser1.setStatus(User.UserStatus.ACTIVE);
        testUser1.setDateCreated(LocalDateTime.now());
        testUser1.setDateUpdated(LocalDateTime.now());

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setIdClient(1L);
        testUser2.setUsername("user1");
        testUser2.setPasswordHash("hashedPassword");
        testUser2.setPasswordSalt("salt");
        testUser2.setPasswordType(User.PasswordType.SHA256);
        testUser2.setName("John Doe");
        testUser2.setEmail("john@example.com");
        testUser2.setStatus(User.UserStatus.ACTIVE);
        testUser2.setDateCreated(LocalDateTime.now());
        testUser2.setDateUpdated(LocalDateTime.now());

        testUser3 = new User();
        testUser3.setId(3L);
        testUser3.setIdClient(2L);
        testUser3.setUsername("inactive_user");
        testUser3.setPasswordHash("hashedPassword");
        testUser3.setPasswordSalt("salt");
        testUser3.setPasswordType(User.PasswordType.MD5);
        testUser3.setName("Inactive User");
        testUser3.setEmail("inactive@example.com");
        testUser3.setStatus(User.UserStatus.INACTIVE);
        testUser3.setDateCreated(LocalDateTime.now());
        testUser3.setDateUpdated(LocalDateTime.now());
    }

    @Test
    void testFindByUsername_Success() {
        // Given
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(testUser1));

        // When
        Optional<User> result = userRepository.findByUsername("admin");

        // Then
        assertTrue(result.isPresent());
        assertEquals("admin", result.get().getUsername());
        assertEquals("Administrator", result.get().getName());
        verify(userRepository, times(1)).findByUsername("admin");
    }

    @Test
    void testFindByUsername_NotFound() {
        // Given
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepository.findByUsername("nonexistent");

        // Then
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    void testFindByEmail_Success() {
        // Given
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser1));

        // When
        Optional<User> result = userRepository.findByEmail("admin@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("admin@example.com", result.get().getEmail());
        assertEquals("admin", result.get().getUsername());
        verify(userRepository, times(1)).findByEmail("admin@example.com");
    }

    @Test
    void testFindByIdClient() {
        // Given
        List<User> usersForClient1 = Arrays.asList(testUser1, testUser2);
        when(userRepository.findByIdClient(1L)).thenReturn(usersForClient1);

        // When
        List<User> result = userRepository.findByIdClient(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(user -> user.getIdClient().equals(1L)));
        verify(userRepository, times(1)).findByIdClient(1L);
    }

    @Test
    void testFindByStatus() {
        // Given
        List<User> activeUsers = Arrays.asList(testUser1, testUser2);
        when(userRepository.findByStatus(User.UserStatus.ACTIVE)).thenReturn(activeUsers);

        // When
        List<User> result = userRepository.findByStatus(User.UserStatus.ACTIVE);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(user -> user.getStatus() == User.UserStatus.ACTIVE));
        verify(userRepository, times(1)).findByStatus(User.UserStatus.ACTIVE);
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        // Given
        List<User> matchingUsers = Arrays.asList(testUser1);
        when(userRepository.findByNameContainingIgnoreCase("admin")).thenReturn(matchingUsers);

        // When
        List<User> result = userRepository.findByNameContainingIgnoreCase("admin");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getName().toLowerCase().contains("admin"));
        verify(userRepository, times(1)).findByNameContainingIgnoreCase("admin");
    }

    @Test
    void testSearchByPattern() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<User> matchingUsers = Arrays.asList(testUser1, testUser2);
        Page<User> userPage = new PageImpl<>(matchingUsers, pageable, matchingUsers.size());
        when(userRepository.searchByPattern("john", pageable)).thenReturn(userPage);

        // When
        Page<User> result = userRepository.searchByPattern("john", pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        verify(userRepository, times(1)).searchByPattern("john", pageable);
    }

    @Test
    void testFindByStatusOrderByNameAsc() {
        // Given
        List<User> activeUsers = Arrays.asList(testUser1, testUser2);
        when(userRepository.findByStatusOrderByNameAsc(User.UserStatus.ACTIVE)).thenReturn(activeUsers);

        // When
        List<User> result = userRepository.findByStatusOrderByNameAsc(User.UserStatus.ACTIVE);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(user -> user.getStatus() == User.UserStatus.ACTIVE));
        verify(userRepository, times(1)).findByStatusOrderByNameAsc(User.UserStatus.ACTIVE);
    }

    @Test
    void testExistsByUsername_True() {
        // Given
        when(userRepository.existsByUsername("admin")).thenReturn(true);

        // When
        boolean result = userRepository.existsByUsername("admin");

        // Then
        assertTrue(result);
        verify(userRepository, times(1)).existsByUsername("admin");
    }

    @Test
    void testExistsByUsername_False() {
        // Given
        when(userRepository.existsByUsername("nonexistent")).thenReturn(false);

        // When
        boolean result = userRepository.existsByUsername("nonexistent");

        // Then
        assertFalse(result);
        verify(userRepository, times(1)).existsByUsername("nonexistent");
    }

    @Test
    void testExistsByEmail_True() {
        // Given
        when(userRepository.existsByEmail("admin@example.com")).thenReturn(true);

        // When
        boolean result = userRepository.existsByEmail("admin@example.com");

        // Then
        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail("admin@example.com");
    }

    @Test
    void testExistsByEmail_False() {
        // Given
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        // When
        boolean result = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertFalse(result);
        verify(userRepository, times(1)).existsByEmail("nonexistent@example.com");
    }

    @Test
    void testCountByIdClient() {
        // Given
        when(userRepository.countByIdClient(1L)).thenReturn(2L);

        // When
        long result = userRepository.countByIdClient(1L);

        // Then
        assertEquals(2L, result);
        verify(userRepository, times(1)).countByIdClient(1L);
    }

    @Test
    void testSave() {
        // Given
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("newuser@example.com");
        newUser.setPasswordHash("hashedPassword");
        newUser.setPasswordSalt("salt");
        newUser.setName("New User");

        User savedUser = new User();
        savedUser.setId(4L);
        savedUser.setUsername("newuser");
        savedUser.setEmail("newuser@example.com");
        savedUser.setPasswordHash("hashedPassword");
        savedUser.setPasswordSalt("salt");
        savedUser.setName("New User");
        savedUser.setStatus(User.UserStatus.ACTIVE);
        savedUser.setDateCreated(LocalDateTime.now());
        savedUser.setDateUpdated(LocalDateTime.now());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userRepository.save(newUser);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("newuser", result.getUsername());
        assertEquals("newuser@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindById() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser1));

        // When
        Optional<User> result = userRepository.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("admin", result.get().getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        // Given
        doNothing().when(userRepository).deleteById(1L);

        // When
        userRepository.deleteById(1L);

        // Then
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAll() {
        // Given
        List<User> allUsers = Arrays.asList(testUser1, testUser2, testUser3);
        when(userRepository.findAll()).thenReturn(allUsers);

        // When
        List<User> result = userRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(userRepository, times(1)).findAll();
    }
}
