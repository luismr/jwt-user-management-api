package com.example.login.repository;

import com.example.login.entity.Role;
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
class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    private Role adminRole;
    private Role userRole;
    private Role externalRole;

    @BeforeEach
    void setUp() {
        adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setDescription("ROLE_ADMIN");
        adminRole.setInternal(true);
        adminRole.setDateCreated(LocalDateTime.now());
        adminRole.setDateUpdated(LocalDateTime.now());

        userRole = new Role();
        userRole.setId(2L);
        userRole.setDescription("ROLE_USER");
        userRole.setInternal(true);
        userRole.setDateCreated(LocalDateTime.now());
        userRole.setDateUpdated(LocalDateTime.now());

        externalRole = new Role();
        externalRole.setId(3L);
        externalRole.setDescription("EXTERNAL_ROLE");
        externalRole.setInternal(false);
        externalRole.setDateCreated(LocalDateTime.now());
        externalRole.setDateUpdated(LocalDateTime.now());
    }

    @Test
    void testFindByDescription_Success() {
        // Given
        when(roleRepository.findByDescription("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));

        // When
        Optional<Role> result = roleRepository.findByDescription("ROLE_ADMIN");

        // Then
        assertTrue(result.isPresent());
        assertEquals("ROLE_ADMIN", result.get().getDescription());
        assertTrue(result.get().getInternal());
        verify(roleRepository, times(1)).findByDescription("ROLE_ADMIN");
    }

    @Test
    void testFindByDescription_NotFound() {
        // Given
        when(roleRepository.findByDescription("NON_EXISTENT_ROLE")).thenReturn(Optional.empty());

        // When
        Optional<Role> result = roleRepository.findByDescription("NON_EXISTENT_ROLE");

        // Then
        assertFalse(result.isPresent());
        verify(roleRepository, times(1)).findByDescription("NON_EXISTENT_ROLE");
    }

    @Test
    void testFindByInternal_True() {
        // Given
        List<Role> internalRoles = Arrays.asList(adminRole, userRole);
        when(roleRepository.findByInternal(true)).thenReturn(internalRoles);

        // When
        List<Role> result = roleRepository.findByInternal(true);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Role::getInternal));
        verify(roleRepository, times(1)).findByInternal(true);
    }

    @Test
    void testFindByInternal_False() {
        // Given
        List<Role> externalRoles = Arrays.asList(externalRole);
        when(roleRepository.findByInternal(false)).thenReturn(externalRoles);

        // When
        List<Role> result = roleRepository.findByInternal(false);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).getInternal());
        verify(roleRepository, times(1)).findByInternal(false);
    }

    @Test
    void testFindByDescriptionContainingIgnoreCase() {
        // Given
        List<Role> matchingRoles = Arrays.asList(adminRole, userRole);
        when(roleRepository.findByDescriptionContainingIgnoreCase("role")).thenReturn(matchingRoles);

        // When
        List<Role> result = roleRepository.findByDescriptionContainingIgnoreCase("role");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(role -> 
            role.getDescription().toLowerCase().contains("role")));
        verify(roleRepository, times(1)).findByDescriptionContainingIgnoreCase("role");
    }

    @Test
    void testFindByDescriptionContainingIgnoreCase_EmptyResult() {
        // Given
        when(roleRepository.findByDescriptionContainingIgnoreCase("xyz")).thenReturn(Arrays.asList());

        // When
        List<Role> result = roleRepository.findByDescriptionContainingIgnoreCase("xyz");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roleRepository, times(1)).findByDescriptionContainingIgnoreCase("xyz");
    }

    @Test
    void testFindByInternalFalseOrderByDescriptionAsc() {
        // Given
        List<Role> externalRoles = Arrays.asList(externalRole);
        when(roleRepository.findByInternalFalseOrderByDescriptionAsc()).thenReturn(externalRoles);

        // When
        List<Role> result = roleRepository.findByInternalFalseOrderByDescriptionAsc();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).getInternal());
        verify(roleRepository, times(1)).findByInternalFalseOrderByDescriptionAsc();
    }

    @Test
    void testFindByInternalTrueOrderByDescriptionAsc() {
        // Given
        List<Role> internalRoles = Arrays.asList(adminRole, userRole);
        when(roleRepository.findByInternalTrueOrderByDescriptionAsc()).thenReturn(internalRoles);

        // When
        List<Role> result = roleRepository.findByInternalTrueOrderByDescriptionAsc();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Role::getInternal));
        verify(roleRepository, times(1)).findByInternalTrueOrderByDescriptionAsc();
    }

    @Test
    void testExistsByDescription_True() {
        // Given
        when(roleRepository.existsByDescription("ROLE_ADMIN")).thenReturn(true);

        // When
        boolean result = roleRepository.existsByDescription("ROLE_ADMIN");

        // Then
        assertTrue(result);
        verify(roleRepository, times(1)).existsByDescription("ROLE_ADMIN");
    }

    @Test
    void testExistsByDescription_False() {
        // Given
        when(roleRepository.existsByDescription("NON_EXISTENT_ROLE")).thenReturn(false);

        // When
        boolean result = roleRepository.existsByDescription("NON_EXISTENT_ROLE");

        // Then
        assertFalse(result);
        verify(roleRepository, times(1)).existsByDescription("NON_EXISTENT_ROLE");
    }

    @Test
    void testSave() {
        // Given
        Role newRole = new Role();
        newRole.setDescription("NEW_ROLE");
        newRole.setInternal(false);

        Role savedRole = new Role();
        savedRole.setId(4L);
        savedRole.setDescription("NEW_ROLE");
        savedRole.setInternal(false);
        savedRole.setDateCreated(LocalDateTime.now());
        savedRole.setDateUpdated(LocalDateTime.now());

        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        // When
        Role result = roleRepository.save(newRole);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("NEW_ROLE", result.getDescription());
        assertFalse(result.getInternal());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void testFindById() {
        // Given
        when(roleRepository.findById(1L)).thenReturn(Optional.of(adminRole));

        // When
        Optional<Role> result = roleRepository.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("ROLE_ADMIN", result.get().getDescription());
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Role> result = roleRepository.findById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(roleRepository, times(1)).findById(999L);
    }

    @Test
    void testDeleteById() {
        // Given
        doNothing().when(roleRepository).deleteById(1L);

        // When
        roleRepository.deleteById(1L);

        // Then
        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAll() {
        // Given
        List<Role> allRoles = Arrays.asList(adminRole, userRole, externalRole);
        when(roleRepository.findAll()).thenReturn(allRoles);

        // When
        List<Role> result = roleRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        // Given
        adminRole.setDescription("UPDATED_ROLE_ADMIN");
        when(roleRepository.save(adminRole)).thenReturn(adminRole);

        // When
        Role result = roleRepository.save(adminRole);

        // Then
        assertNotNull(result);
        assertEquals("UPDATED_ROLE_ADMIN", result.getDescription());
        assertTrue(result.getInternal());
        verify(roleRepository, times(1)).save(adminRole);
    }
}
