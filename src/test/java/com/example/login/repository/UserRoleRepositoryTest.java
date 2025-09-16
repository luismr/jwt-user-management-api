package com.example.login.repository;

import com.example.login.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRoleRepositoryTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    private UserRole userRole1;
    private UserRole userRole2;
    private UserRole userRole3;

    @BeforeEach
    void setUp() {
        userRole1 = new UserRole();
        userRole1.setIdUser(1L);
        userRole1.setIdClient(1L);
        userRole1.setIdRole(1L);
        userRole1.setDateCreated(LocalDateTime.now());
        userRole1.setDateUpdated(LocalDateTime.now());

        userRole2 = new UserRole();
        userRole2.setIdUser(1L);
        userRole2.setIdClient(1L);
        userRole2.setIdRole(2L);
        userRole2.setDateCreated(LocalDateTime.now());
        userRole2.setDateUpdated(LocalDateTime.now());

        userRole3 = new UserRole();
        userRole3.setIdUser(2L);
        userRole3.setIdClient(2L);
        userRole3.setIdRole(1L);
        userRole3.setDateCreated(LocalDateTime.now());
        userRole3.setDateUpdated(LocalDateTime.now());
    }

    @Test
    void testFindByIdUser() {
        // Given
        List<UserRole> userRolesForUser1 = Arrays.asList(userRole1, userRole2);
        when(userRoleRepository.findByIdUser(1L)).thenReturn(userRolesForUser1);

        // When
        List<UserRole> result = userRoleRepository.findByIdUser(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(ur -> ur.getIdUser().equals(1L)));
        verify(userRoleRepository, times(1)).findByIdUser(1L);
    }

    @Test
    void testFindByIdUser_EmptyResult() {
        // Given
        when(userRoleRepository.findByIdUser(999L)).thenReturn(Arrays.asList());

        // When
        List<UserRole> result = userRoleRepository.findByIdUser(999L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRoleRepository, times(1)).findByIdUser(999L);
    }

    @Test
    void testFindByIdClient() {
        // Given
        List<UserRole> userRolesForClient1 = Arrays.asList(userRole1, userRole2);
        when(userRoleRepository.findByIdClient(1L)).thenReturn(userRolesForClient1);

        // When
        List<UserRole> result = userRoleRepository.findByIdClient(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(ur -> ur.getIdClient().equals(1L)));
        verify(userRoleRepository, times(1)).findByIdClient(1L);
    }

    @Test
    void testFindByIdRole() {
        // Given
        List<UserRole> userRolesForRole1 = Arrays.asList(userRole1, userRole3);
        when(userRoleRepository.findByIdRole(1L)).thenReturn(userRolesForRole1);

        // When
        List<UserRole> result = userRoleRepository.findByIdRole(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(ur -> ur.getIdRole().equals(1L)));
        verify(userRoleRepository, times(1)).findByIdRole(1L);
    }

    @Test
    void testFindByIdUserAndIdClient() {
        // Given
        List<UserRole> userRolesForUserAndClient = Arrays.asList(userRole1, userRole2);
        when(userRoleRepository.findByIdUserAndIdClient(1L, 1L)).thenReturn(userRolesForUserAndClient);

        // When
        List<UserRole> result = userRoleRepository.findByIdUserAndIdClient(1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(ur -> 
            ur.getIdUser().equals(1L) && ur.getIdClient().equals(1L)));
        verify(userRoleRepository, times(1)).findByIdUserAndIdClient(1L, 1L);
    }

    @Test
    void testFindByRoleAndClient() {
        // Given
        List<UserRole> userRolesForRoleAndClient = Arrays.asList(userRole1);
        when(userRoleRepository.findByRoleAndClient(1L, 1L)).thenReturn(userRolesForRoleAndClient);

        // When
        List<UserRole> result = userRoleRepository.findByRoleAndClient(1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdRole());
        assertEquals(1L, result.get(0).getIdClient());
        verify(userRoleRepository, times(1)).findByRoleAndClient(1L, 1L);
    }

    @Test
    void testCountRolesByUserAndClient() {
        // Given
        when(userRoleRepository.countRolesByUserAndClient(1L, 1L)).thenReturn(2L);

        // When
        long result = userRoleRepository.countRolesByUserAndClient(1L, 1L);

        // Then
        assertEquals(2L, result);
        verify(userRoleRepository, times(1)).countRolesByUserAndClient(1L, 1L);
    }

    @Test
    void testCountRolesByUserAndClient_ZeroResult() {
        // Given
        when(userRoleRepository.countRolesByUserAndClient(999L, 999L)).thenReturn(0L);

        // When
        long result = userRoleRepository.countRolesByUserAndClient(999L, 999L);

        // Then
        assertEquals(0L, result);
        verify(userRoleRepository, times(1)).countRolesByUserAndClient(999L, 999L);
    }

    @Test
    void testExistsByIdUserAndIdClientAndIdRole_True() {
        // Given
        when(userRoleRepository.existsByIdUserAndIdClientAndIdRole(1L, 1L, 1L)).thenReturn(true);

        // When
        boolean result = userRoleRepository.existsByIdUserAndIdClientAndIdRole(1L, 1L, 1L);

        // Then
        assertTrue(result);
        verify(userRoleRepository, times(1)).existsByIdUserAndIdClientAndIdRole(1L, 1L, 1L);
    }

    @Test
    void testExistsByIdUserAndIdClientAndIdRole_False() {
        // Given
        when(userRoleRepository.existsByIdUserAndIdClientAndIdRole(999L, 999L, 999L)).thenReturn(false);

        // When
        boolean result = userRoleRepository.existsByIdUserAndIdClientAndIdRole(999L, 999L, 999L);

        // Then
        assertFalse(result);
        verify(userRoleRepository, times(1)).existsByIdUserAndIdClientAndIdRole(999L, 999L, 999L);
    }

    @Test
    void testSave() {
        // Given
        UserRole newUserRole = new UserRole();
        newUserRole.setIdUser(3L);
        newUserRole.setIdClient(2L);
        newUserRole.setIdRole(2L);

        UserRole savedUserRole = new UserRole();
        savedUserRole.setIdUser(3L);
        savedUserRole.setIdClient(2L);
        savedUserRole.setIdRole(2L);
        savedUserRole.setDateCreated(LocalDateTime.now());
        savedUserRole.setDateUpdated(LocalDateTime.now());

        when(userRoleRepository.save(any(UserRole.class))).thenReturn(savedUserRole);

        // When
        UserRole result = userRoleRepository.save(newUserRole);

        // Then
        assertNotNull(result);
        assertEquals(3L, result.getIdUser());
        assertEquals(2L, result.getIdClient());
        assertEquals(2L, result.getIdRole());
        assertNotNull(result.getDateCreated());
        assertNotNull(result.getDateUpdated());
        verify(userRoleRepository, times(1)).save(any(UserRole.class));
    }

    @Test
    void testFindById() {
        // Given
        UserRole.UserRoleId id = new UserRole.UserRoleId(1L, 1L, 1L);
        when(userRoleRepository.findById(id)).thenReturn(Optional.of(userRole1));

        // When
        Optional<UserRole> result = userRoleRepository.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getIdUser());
        assertEquals(1L, result.get().getIdClient());
        assertEquals(1L, result.get().getIdRole());
        verify(userRoleRepository, times(1)).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        UserRole.UserRoleId id = new UserRole.UserRoleId(999L, 999L, 999L);
        when(userRoleRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<UserRole> result = userRoleRepository.findById(id);

        // Then
        assertFalse(result.isPresent());
        verify(userRoleRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById() {
        // Given
        UserRole.UserRoleId id = new UserRole.UserRoleId(1L, 1L, 1L);
        doNothing().when(userRoleRepository).deleteById(id);

        // When
        userRoleRepository.deleteById(id);

        // Then
        verify(userRoleRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindAll() {
        // Given
        List<UserRole> allUserRoles = Arrays.asList(userRole1, userRole2, userRole3);
        when(userRoleRepository.findAll()).thenReturn(allUserRoles);

        // When
        List<UserRole> result = userRoleRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(userRoleRepository, times(1)).findAll();
    }

    @Test
    void testDelete() {
        // Given
        doNothing().when(userRoleRepository).delete(userRole1);

        // When
        userRoleRepository.delete(userRole1);

        // Then
        verify(userRoleRepository, times(1)).delete(userRole1);
    }

    @Test
    void testUserRoleIdEquals() {
        // Given
        UserRole.UserRoleId id1 = new UserRole.UserRoleId(1L, 1L, 1L);
        UserRole.UserRoleId id2 = new UserRole.UserRoleId(1L, 1L, 1L);
        UserRole.UserRoleId id3 = new UserRole.UserRoleId(2L, 1L, 1L);

        // When & Then
        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void testUserRoleIdGettersAndSetters() {
        // Given
        UserRole.UserRoleId id = new UserRole.UserRoleId();

        // When
        id.setIdUser(1L);
        id.setIdClient(2L);
        id.setIdRole(3L);

        // Then
        assertEquals(1L, id.getIdUser());
        assertEquals(2L, id.getIdClient());
        assertEquals(3L, id.getIdRole());
    }
}
