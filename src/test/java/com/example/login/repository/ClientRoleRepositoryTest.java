package com.example.login.repository;

import com.example.login.entity.ClientRole;
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
class ClientRoleRepositoryTest {

    @Mock
    private ClientRoleRepository clientRoleRepository;

    private ClientRole clientRole1;
    private ClientRole clientRole2;
    private ClientRole clientRole3;

    @BeforeEach
    void setUp() {
        clientRole1 = new ClientRole();
        clientRole1.setIdClient(1L);
        clientRole1.setIdRole(1L);
        clientRole1.setDateCreated(LocalDateTime.now());
        clientRole1.setDateUpdated(LocalDateTime.now());

        clientRole2 = new ClientRole();
        clientRole2.setIdClient(1L);
        clientRole2.setIdRole(2L);
        clientRole2.setDateCreated(LocalDateTime.now());
        clientRole2.setDateUpdated(LocalDateTime.now());

        clientRole3 = new ClientRole();
        clientRole3.setIdClient(2L);
        clientRole3.setIdRole(1L);
        clientRole3.setDateCreated(LocalDateTime.now());
        clientRole3.setDateUpdated(LocalDateTime.now());
    }

    @Test
    void testFindByIdClient() {
        // Given
        List<ClientRole> clientRolesForClient1 = Arrays.asList(clientRole1, clientRole2);
        when(clientRoleRepository.findByIdClient(1L)).thenReturn(clientRolesForClient1);

        // When
        List<ClientRole> result = clientRoleRepository.findByIdClient(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(cr -> cr.getIdClient().equals(1L)));
        verify(clientRoleRepository, times(1)).findByIdClient(1L);
    }

    @Test
    void testFindByIdClient_EmptyResult() {
        // Given
        when(clientRoleRepository.findByIdClient(999L)).thenReturn(Arrays.asList());

        // When
        List<ClientRole> result = clientRoleRepository.findByIdClient(999L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clientRoleRepository, times(1)).findByIdClient(999L);
    }

    @Test
    void testFindByIdRole() {
        // Given
        List<ClientRole> clientRolesForRole1 = Arrays.asList(clientRole1, clientRole3);
        when(clientRoleRepository.findByIdRole(1L)).thenReturn(clientRolesForRole1);

        // When
        List<ClientRole> result = clientRoleRepository.findByIdRole(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(cr -> cr.getIdRole().equals(1L)));
        verify(clientRoleRepository, times(1)).findByIdRole(1L);
    }

    @Test
    void testFindByIdRole_EmptyResult() {
        // Given
        when(clientRoleRepository.findByIdRole(999L)).thenReturn(Arrays.asList());

        // When
        List<ClientRole> result = clientRoleRepository.findByIdRole(999L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clientRoleRepository, times(1)).findByIdRole(999L);
    }

    @Test
    void testCountRolesByClient() {
        // Given
        when(clientRoleRepository.countRolesByClient(1L)).thenReturn(2L);

        // When
        long result = clientRoleRepository.countRolesByClient(1L);

        // Then
        assertEquals(2L, result);
        verify(clientRoleRepository, times(1)).countRolesByClient(1L);
    }

    @Test
    void testCountRolesByClient_ZeroResult() {
        // Given
        when(clientRoleRepository.countRolesByClient(999L)).thenReturn(0L);

        // When
        long result = clientRoleRepository.countRolesByClient(999L);

        // Then
        assertEquals(0L, result);
        verify(clientRoleRepository, times(1)).countRolesByClient(999L);
    }

    @Test
    void testCountClientsByRole() {
        // Given
        when(clientRoleRepository.countClientsByRole(1L)).thenReturn(2L);

        // When
        long result = clientRoleRepository.countClientsByRole(1L);

        // Then
        assertEquals(2L, result);
        verify(clientRoleRepository, times(1)).countClientsByRole(1L);
    }

    @Test
    void testCountClientsByRole_ZeroResult() {
        // Given
        when(clientRoleRepository.countClientsByRole(999L)).thenReturn(0L);

        // When
        long result = clientRoleRepository.countClientsByRole(999L);

        // Then
        assertEquals(0L, result);
        verify(clientRoleRepository, times(1)).countClientsByRole(999L);
    }

    @Test
    void testExistsByIdClientAndIdRole_True() {
        // Given
        when(clientRoleRepository.existsByIdClientAndIdRole(1L, 1L)).thenReturn(true);

        // When
        boolean result = clientRoleRepository.existsByIdClientAndIdRole(1L, 1L);

        // Then
        assertTrue(result);
        verify(clientRoleRepository, times(1)).existsByIdClientAndIdRole(1L, 1L);
    }

    @Test
    void testExistsByIdClientAndIdRole_False() {
        // Given
        when(clientRoleRepository.existsByIdClientAndIdRole(999L, 999L)).thenReturn(false);

        // When
        boolean result = clientRoleRepository.existsByIdClientAndIdRole(999L, 999L);

        // Then
        assertFalse(result);
        verify(clientRoleRepository, times(1)).existsByIdClientAndIdRole(999L, 999L);
    }

    @Test
    void testSave() {
        // Given
        ClientRole newClientRole = new ClientRole();
        newClientRole.setIdClient(3L);
        newClientRole.setIdRole(2L);

        ClientRole savedClientRole = new ClientRole();
        savedClientRole.setIdClient(3L);
        savedClientRole.setIdRole(2L);
        savedClientRole.setDateCreated(LocalDateTime.now());
        savedClientRole.setDateUpdated(LocalDateTime.now());

        when(clientRoleRepository.save(any(ClientRole.class))).thenReturn(savedClientRole);

        // When
        ClientRole result = clientRoleRepository.save(newClientRole);

        // Then
        assertNotNull(result);
        assertEquals(3L, result.getIdClient());
        assertEquals(2L, result.getIdRole());
        assertNotNull(result.getDateCreated());
        assertNotNull(result.getDateUpdated());
        verify(clientRoleRepository, times(1)).save(any(ClientRole.class));
    }

    @Test
    void testFindById() {
        // Given
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(1L, 1L);
        when(clientRoleRepository.findById(id)).thenReturn(Optional.of(clientRole1));

        // When
        Optional<ClientRole> result = clientRoleRepository.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getIdClient());
        assertEquals(1L, result.get().getIdRole());
        verify(clientRoleRepository, times(1)).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(999L, 999L);
        when(clientRoleRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<ClientRole> result = clientRoleRepository.findById(id);

        // Then
        assertFalse(result.isPresent());
        verify(clientRoleRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById() {
        // Given
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(1L, 1L);
        doNothing().when(clientRoleRepository).deleteById(id);

        // When
        clientRoleRepository.deleteById(id);

        // Then
        verify(clientRoleRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindAll() {
        // Given
        List<ClientRole> allClientRoles = Arrays.asList(clientRole1, clientRole2, clientRole3);
        when(clientRoleRepository.findAll()).thenReturn(allClientRoles);

        // When
        List<ClientRole> result = clientRoleRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(clientRoleRepository, times(1)).findAll();
    }

    @Test
    void testDelete() {
        // Given
        doNothing().when(clientRoleRepository).delete(clientRole1);

        // When
        clientRoleRepository.delete(clientRole1);

        // Then
        verify(clientRoleRepository, times(1)).delete(clientRole1);
    }

    @Test
    void testClientRoleIdEquals() {
        // Given
        ClientRole.ClientRoleId id1 = new ClientRole.ClientRoleId(1L, 1L);
        ClientRole.ClientRoleId id2 = new ClientRole.ClientRoleId(1L, 1L);
        ClientRole.ClientRoleId id3 = new ClientRole.ClientRoleId(2L, 1L);

        // When & Then
        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void testClientRoleIdGettersAndSetters() {
        // Given
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId();

        // When
        id.setIdClient(1L);
        id.setIdRole(2L);

        // Then
        assertEquals(1L, id.getIdClient());
        assertEquals(2L, id.getIdRole());
    }

    @Test
    void testClientRoleIdConstructor() {
        // Given & When
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(5L, 10L);

        // Then
        assertEquals(5L, id.getIdClient());
        assertEquals(10L, id.getIdRole());
    }

    @Test
    void testUpdate() {
        // Given
        clientRole1.setDateUpdated(LocalDateTime.now().plusDays(1));
        when(clientRoleRepository.save(clientRole1)).thenReturn(clientRole1);

        // When
        ClientRole result = clientRoleRepository.save(clientRole1);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getIdClient());
        assertEquals(1L, result.getIdRole());
        verify(clientRoleRepository, times(1)).save(clientRole1);
    }
}
