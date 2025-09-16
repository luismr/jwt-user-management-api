package com.example.login.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClientRoleTest {

    private ClientRole clientRole;

    @BeforeEach
    void setUp() {
        clientRole = new ClientRole();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        ClientRole newClientRole = new ClientRole();

        // Then
        assertNull(newClientRole.getIdClient());
        assertNull(newClientRole.getIdRole());
        assertNull(newClientRole.getClient());
        assertNull(newClientRole.getRole());
        assertNull(newClientRole.getDateCreated());
        assertNull(newClientRole.getDateUpdated());
    }

    @Test
    void testParameterizedConstructor() {
        // Given & When
        ClientRole newClientRole = new ClientRole(1L, 2L);

        // Then
        assertEquals(1L, newClientRole.getIdClient());
        assertEquals(2L, newClientRole.getIdRole());
        assertNull(newClientRole.getClient());
        assertNull(newClientRole.getRole());
    }

    @Test
    void testGettersAndSetters() {
        // Given
        Long idClient = 1L;
        Long idRole = 2L;
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        // When
        clientRole.setIdClient(idClient);
        clientRole.setIdRole(idRole);
        clientRole.setDateCreated(created);
        clientRole.setDateUpdated(updated);

        // Then
        assertEquals(idClient, clientRole.getIdClient());
        assertEquals(idRole, clientRole.getIdRole());
        assertEquals(created, clientRole.getDateCreated());
        assertEquals(updated, clientRole.getDateUpdated());
    }

    @Test
    void testBuilderPattern() {
        // Given & When
        ClientRole builtClientRole = ClientRole.builder()
                .idClient(1L)
                .idRole(2L)
                .dateCreated(LocalDateTime.now().minusDays(1))
                .dateUpdated(LocalDateTime.now())
                .build();

        // Then
        assertEquals(1L, builtClientRole.getIdClient());
        assertEquals(2L, builtClientRole.getIdRole());
        assertNotNull(builtClientRole.getDateCreated());
        assertNotNull(builtClientRole.getDateUpdated());
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        Long idClient = 1L;
        Long idRole = 2L;
        LocalDateTime dateCreated = LocalDateTime.now().minusDays(1);
        LocalDateTime dateUpdated = LocalDateTime.now();

        // When
        ClientRole clientRole = new ClientRole(idClient, idRole, dateCreated, dateUpdated, null, null, null);

        // Then
        assertEquals(idClient, clientRole.getIdClient());
        assertEquals(idRole, clientRole.getIdRole());
        assertEquals(dateCreated, clientRole.getDateCreated());
        assertEquals(dateUpdated, clientRole.getDateUpdated());
    }

    @Test
    void testToString() {
        // Given
        clientRole.setIdClient(1L);
        clientRole.setIdRole(2L);
        LocalDateTime now = LocalDateTime.now();
        clientRole.setDateCreated(now);
        clientRole.setDateUpdated(now);

        // When
        String result = clientRole.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("ClientRole("));
        assertTrue(result.contains("idClient=1"));
        assertTrue(result.contains("idRole=2"));
        assertTrue(result.contains("dateCreated="));
        assertTrue(result.contains("dateUpdated="));
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        ClientRole clientRole1 = new ClientRole();
        clientRole1.setIdClient(1L);
        clientRole1.setIdRole(2L);

        ClientRole clientRole2 = new ClientRole();
        clientRole2.setIdClient(1L);
        clientRole2.setIdRole(2L);

        ClientRole clientRole3 = new ClientRole();
        clientRole3.setIdClient(3L);
        clientRole3.setIdRole(4L);

        // Note: With Lombok @EqualsAndHashCode(onlyExplicitlyIncluded = true),
        // only the ID fields are used for equals/hashCode
        assertEquals(clientRole1, clientRole2); // Same IDs, so they are equal
        assertEquals(clientRole1.hashCode(), clientRole2.hashCode()); // Same IDs, same hashCode
        assertNotEquals(clientRole1, clientRole3); // Different IDs, so not equal
    }

    @Test
    void testEqualsWithNullIds() {
        // Given
        ClientRole clientRole1 = new ClientRole();
        ClientRole clientRole2 = new ClientRole();

        // When & Then
        assertEquals(clientRole1, clientRole2); // Both have null IDs, so they are equal
        assertEquals(clientRole1.hashCode(), clientRole2.hashCode());
    }

    @Test
    void testEqualsWithDifferentNullIds() {
        // Given
        ClientRole clientRole1 = new ClientRole();
        clientRole1.setIdClient(1L);
        clientRole1.setIdRole(2L);

        ClientRole clientRole2 = new ClientRole();
        // clientRole2 has null IDs

        // When & Then
        assertNotEquals(clientRole1, clientRole2); // Different IDs, so not equal
    }

    @Test
    void testEqualsWithSameObject() {
        // When & Then
        assertEquals(clientRole, clientRole);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Given
        String differentObject = "not a ClientRole";

        // When & Then
        assertNotEquals(clientRole, differentObject);
    }

    @Test
    void testEqualsWithNull() {
        // When & Then
        assertNotEquals(clientRole, null);
    }

    @Test
    void testNullSafety() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            clientRole.setIdClient(null);
            clientRole.setIdRole(null);
            clientRole.setDateCreated(null);
            clientRole.setDateUpdated(null);
        });

        assertNull(clientRole.getIdClient());
        assertNull(clientRole.getIdRole());
        assertNull(clientRole.getDateCreated());
        assertNull(clientRole.getDateUpdated());
    }

    @Test
    void testDateModification() {
        // Given
        LocalDateTime originalCreated = LocalDateTime.now().minusDays(1);
        LocalDateTime originalUpdated = LocalDateTime.now().minusHours(1);
        clientRole.setDateCreated(originalCreated);
        clientRole.setDateUpdated(originalUpdated);

        // When
        LocalDateTime newCreated = LocalDateTime.now();
        LocalDateTime newUpdated = LocalDateTime.now();
        clientRole.setDateCreated(newCreated);
        clientRole.setDateUpdated(newUpdated);

        // Then
        assertEquals(newCreated, clientRole.getDateCreated());
        assertEquals(newUpdated, clientRole.getDateUpdated());
        assertNotEquals(originalCreated, clientRole.getDateCreated());
        assertNotEquals(originalUpdated, clientRole.getDateUpdated());
    }

    @Test
    void testLongIdValues() {
        // Given
        Long maxLongClient = Long.MAX_VALUE;
        Long maxLongRole = Long.MAX_VALUE;

        // When
        clientRole.setIdClient(maxLongClient);
        clientRole.setIdRole(maxLongRole);

        // Then
        assertEquals(maxLongClient, clientRole.getIdClient());
        assertEquals(maxLongRole, clientRole.getIdRole());
    }

    @Test
    void testZeroIdValues() {
        // Given
        Long zeroClient = 0L;
        Long zeroRole = 0L;

        // When
        clientRole.setIdClient(zeroClient);
        clientRole.setIdRole(zeroRole);

        // Then
        assertEquals(zeroClient, clientRole.getIdClient());
        assertEquals(zeroRole, clientRole.getIdRole());
    }

    @Test
    void testNegativeIdValues() {
        // Given
        Long negativeClient = -1L;
        Long negativeRole = -1L;

        // When
        clientRole.setIdClient(negativeClient);
        clientRole.setIdRole(negativeRole);

        // Then
        assertEquals(negativeClient, clientRole.getIdClient());
        assertEquals(negativeRole, clientRole.getIdRole());
    }

    @Test
    void testClientRoleIdClass() {
        // Given
        ClientRole.ClientRoleId id1 = new ClientRole.ClientRoleId(1L, 2L);
        ClientRole.ClientRoleId id2 = new ClientRole.ClientRoleId(1L, 2L);
        ClientRole.ClientRoleId id3 = new ClientRole.ClientRoleId(3L, 4L);

        // When & Then
        assertEquals(id1, id2); // Same values, so equal
        assertEquals(id1.hashCode(), id2.hashCode()); // Same values, same hashCode
        assertNotEquals(id1, id3); // Different values, so not equal
    }

    @Test
    void testClientRoleIdClassWithDifferentNullValues() {
        // Given
        ClientRole.ClientRoleId id1 = new ClientRole.ClientRoleId(1L, 2L);
        ClientRole.ClientRoleId id2 = new ClientRole.ClientRoleId(null, 2L);

        // When & Then
        assertNotEquals(id1, id2); // Different values, so not equal
    }

    @Test
    void testClientRoleIdClassDefaultConstructor() {
        // Given & When
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId();

        // Then
        assertNull(id.getIdClient());
        assertNull(id.getIdRole());
    }

    @Test
    void testClientRoleIdClassAllArgsConstructor() {
        // Given & When
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(1L, 2L);

        // Then
        assertEquals(1L, id.getIdClient());
        assertEquals(2L, id.getIdRole());
    }

    @Test
    void testClientRoleIdClassSettersAndGetters() {
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
    void testClientRoleIdClassToString() {
        // Given
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(1L, 2L);

        // When
        String result = id.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("ClientRoleId("));
        assertTrue(result.contains("idClient=1"));
        assertTrue(result.contains("idRole=2"));
    }

    @Test
    void testClientRoleIdClassEqualsWithSameObject() {
        // Given
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(1L, 2L);

        // When & Then
        assertEquals(id, id);
    }

    @Test
    void testClientRoleIdClassEqualsWithDifferentClass() {
        // Given
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(1L, 2L);
        String differentObject = "not a ClientRoleId";

        // When & Then
        assertNotEquals(id, differentObject);
    }

    @Test
    void testClientRoleIdClassEqualsWithNull() {
        // Given
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(1L, 2L);

        // When & Then
        assertNotEquals(id, null);
    }

    @Test
    void testClientRoleIdClassImplementsSerializable() {
        // Given
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(1L, 2L);

        // When & Then
        assertTrue(id instanceof Serializable);
    }

    @Test
    void testClientRoleIdClassWithMaxLongValues() {
        // Given
        Long maxLong = Long.MAX_VALUE;
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(maxLong, maxLong);

        // When & Then
        assertEquals(maxLong, id.getIdClient());
        assertEquals(maxLong, id.getIdRole());
    }

    @Test
    void testClientRoleIdClassWithMinLongValues() {
        // Given
        Long minLong = Long.MIN_VALUE;
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(minLong, minLong);

        // When & Then
        assertEquals(minLong, id.getIdClient());
        assertEquals(minLong, id.getIdRole());
    }

    @Test
    void testClientRoleIdClassWithZeroValues() {
        // Given
        Long zero = 0L;
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(zero, zero);

        // When & Then
        assertEquals(zero, id.getIdClient());
        assertEquals(zero, id.getIdRole());
    }

    @Test
    void testClientRoleIdClassWithNegativeValues() {
        // Given
        Long negative = -1L;
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(negative, negative);

        // When & Then
        assertEquals(negative, id.getIdClient());
        assertEquals(negative, id.getIdRole());
    }

    @Test
    void testClientRoleIdClassWithMixedValues() {
        // Given
        Long clientId = 1L;
        Long roleId = 2L;
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(clientId, roleId);

        // When & Then
        assertEquals(clientId, id.getIdClient());
        assertEquals(roleId, id.getIdRole());
    }

    @Test
    void testClientRoleIdClassWithNullValues() {
        // Given
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(null, null);

        // When & Then
        assertNull(id.getIdClient());
        assertNull(id.getIdRole());
    }

    @Test
    void testClientRoleIdClassWithOneNullValue() {
        // Given
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(1L, null);

        // When & Then
        assertEquals(1L, id.getIdClient());
        assertNull(id.getIdRole());
    }

    @Test
    void testClientRoleIdClassWithOtherNullValue() {
        // Given
        ClientRole.ClientRoleId id = new ClientRole.ClientRoleId(null, 2L);

        // When & Then
        assertNull(id.getIdClient());
        assertEquals(2L, id.getIdRole());
    }

    @Test
    void testClientRoleIdClassHashCodeConsistency() {
        // Given
        ClientRole.ClientRoleId id1 = new ClientRole.ClientRoleId(1L, 2L);
        ClientRole.ClientRoleId id2 = new ClientRole.ClientRoleId(1L, 2L);

        // When & Then
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testClientRoleIdClassHashCodeDifferentValues() {
        // Given
        ClientRole.ClientRoleId id1 = new ClientRole.ClientRoleId(1L, 2L);
        ClientRole.ClientRoleId id2 = new ClientRole.ClientRoleId(3L, 4L);

        // When & Then
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testClientRoleIdClassHashCodeWithNullValues() {
        // Given
        ClientRole.ClientRoleId id1 = new ClientRole.ClientRoleId(null, null);
        ClientRole.ClientRoleId id2 = new ClientRole.ClientRoleId(null, null);

        // When & Then
        assertEquals(id1.hashCode(), id2.hashCode());
    }
}
