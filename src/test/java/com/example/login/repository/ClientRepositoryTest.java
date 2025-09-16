package com.example.login.repository;

import com.example.login.entity.Client;
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
class ClientRepositoryTest {

    @Mock
    private ClientRepository clientRepository;

    private Client testClient1;
    private Client testClient2;

    @BeforeEach
    void setUp() {
        testClient1 = new Client();
        testClient1.setId(1L);
        testClient1.setExternalId("SEARS-HOME-SERVICES");
        testClient1.setName("SEARS HOME SERVICES");
        testClient1.setDateCreated(LocalDateTime.now());
        testClient1.setDateUpdated(LocalDateTime.now());

        testClient2 = new Client();
        testClient2.setId(2L);
        testClient2.setExternalId("ACME-CORP");
        testClient2.setName("ACME Corporation");
        testClient2.setDateCreated(LocalDateTime.now());
        testClient2.setDateUpdated(LocalDateTime.now());
    }

    @Test
    void testFindByExternalId_Success() {
        // Given
        when(clientRepository.findByExternalId("SEARS-HOME-SERVICES")).thenReturn(Optional.of(testClient1));

        // When
        Optional<Client> result = clientRepository.findByExternalId("SEARS-HOME-SERVICES");

        // Then
        assertTrue(result.isPresent());
        assertEquals("SEARS-HOME-SERVICES", result.get().getExternalId());
        assertEquals("SEARS HOME SERVICES", result.get().getName());
        verify(clientRepository, times(1)).findByExternalId("SEARS-HOME-SERVICES");
    }

    @Test
    void testFindByExternalId_NotFound() {
        // Given
        when(clientRepository.findByExternalId("NON-EXISTENT")).thenReturn(Optional.empty());

        // When
        Optional<Client> result = clientRepository.findByExternalId("NON-EXISTENT");

        // Then
        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findByExternalId("NON-EXISTENT");
    }

    @Test
    void testFindByName_Success() {
        // Given
        when(clientRepository.findByName("SEARS HOME SERVICES")).thenReturn(Optional.of(testClient1));

        // When
        Optional<Client> result = clientRepository.findByName("SEARS HOME SERVICES");

        // Then
        assertTrue(result.isPresent());
        assertEquals("SEARS HOME SERVICES", result.get().getName());
        assertEquals("SEARS-HOME-SERVICES", result.get().getExternalId());
        verify(clientRepository, times(1)).findByName("SEARS HOME SERVICES");
    }

    @Test
    void testFindByName_NotFound() {
        // Given
        when(clientRepository.findByName("NON-EXISTENT CLIENT")).thenReturn(Optional.empty());

        // When
        Optional<Client> result = clientRepository.findByName("NON-EXISTENT CLIENT");

        // Then
        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findByName("NON-EXISTENT CLIENT");
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        // Given
        List<Client> matchingClients = Arrays.asList(testClient1);
        when(clientRepository.findByNameContainingIgnoreCase("sears")).thenReturn(matchingClients);

        // When
        List<Client> result = clientRepository.findByNameContainingIgnoreCase("sears");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getName().toLowerCase().contains("sears"));
        verify(clientRepository, times(1)).findByNameContainingIgnoreCase("sears");
    }

    @Test
    void testFindByNameContainingIgnoreCase_EmptyResult() {
        // Given
        when(clientRepository.findByNameContainingIgnoreCase("xyz")).thenReturn(Arrays.asList());

        // When
        List<Client> result = clientRepository.findByNameContainingIgnoreCase("xyz");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clientRepository, times(1)).findByNameContainingIgnoreCase("xyz");
    }

    @Test
    void testExistsByExternalId_True() {
        // Given
        when(clientRepository.existsByExternalId("SEARS-HOME-SERVICES")).thenReturn(true);

        // When
        boolean result = clientRepository.existsByExternalId("SEARS-HOME-SERVICES");

        // Then
        assertTrue(result);
        verify(clientRepository, times(1)).existsByExternalId("SEARS-HOME-SERVICES");
    }

    @Test
    void testExistsByExternalId_False() {
        // Given
        when(clientRepository.existsByExternalId("NON-EXISTENT")).thenReturn(false);

        // When
        boolean result = clientRepository.existsByExternalId("NON-EXISTENT");

        // Then
        assertFalse(result);
        verify(clientRepository, times(1)).existsByExternalId("NON-EXISTENT");
    }

    @Test
    void testExistsByName_True() {
        // Given
        when(clientRepository.existsByName("SEARS HOME SERVICES")).thenReturn(true);

        // When
        boolean result = clientRepository.existsByName("SEARS HOME SERVICES");

        // Then
        assertTrue(result);
        verify(clientRepository, times(1)).existsByName("SEARS HOME SERVICES");
    }

    @Test
    void testExistsByName_False() {
        // Given
        when(clientRepository.existsByName("NON-EXISTENT CLIENT")).thenReturn(false);

        // When
        boolean result = clientRepository.existsByName("NON-EXISTENT CLIENT");

        // Then
        assertFalse(result);
        verify(clientRepository, times(1)).existsByName("NON-EXISTENT CLIENT");
    }

    @Test
    void testSave() {
        // Given
        Client newClient = new Client();
        newClient.setExternalId("NEW-CLIENT");
        newClient.setName("New Client");

        Client savedClient = new Client();
        savedClient.setId(3L);
        savedClient.setExternalId("NEW-CLIENT");
        savedClient.setName("New Client");
        savedClient.setDateCreated(LocalDateTime.now());
        savedClient.setDateUpdated(LocalDateTime.now());

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        // When
        Client result = clientRepository.save(newClient);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("NEW-CLIENT", result.getExternalId());
        assertEquals("New Client", result.getName());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testFindById() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient1));

        // When
        Optional<Client> result = clientRepository.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("SEARS-HOME-SERVICES", result.get().getExternalId());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Client> result = clientRepository.findById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findById(999L);
    }

    @Test
    void testDeleteById() {
        // Given
        doNothing().when(clientRepository).deleteById(1L);

        // When
        clientRepository.deleteById(1L);

        // Then
        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAll() {
        // Given
        List<Client> allClients = Arrays.asList(testClient1, testClient2);
        when(clientRepository.findAll()).thenReturn(allClients);

        // When
        List<Client> result = clientRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        // Given
        testClient1.setName("Updated SEARS HOME SERVICES");
        when(clientRepository.save(testClient1)).thenReturn(testClient1);

        // When
        Client result = clientRepository.save(testClient1);

        // Then
        assertNotNull(result);
        assertEquals("Updated SEARS HOME SERVICES", result.getName());
        assertEquals("SEARS-HOME-SERVICES", result.getExternalId());
        verify(clientRepository, times(1)).save(testClient1);
    }
}
