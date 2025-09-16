package com.example.login.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {

    private UserRole userRole;

    @BeforeEach
    void setUp() {
        userRole = new UserRole();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        UserRole newUserRole = new UserRole();

        // Then
        assertNull(newUserRole.getIdUser());
        assertNull(newUserRole.getIdClient());
        assertNull(newUserRole.getIdRole());
        assertNull(newUserRole.getUser());
        assertNull(newUserRole.getClientRole());
        assertNull(newUserRole.getDateCreated());
        assertNull(newUserRole.getDateUpdated());
    }

    @Test
    void testParameterizedConstructor() {
        // Given & When
        UserRole newUserRole = new UserRole(1L, 2L, 3L);

        // Then
        assertEquals(1L, newUserRole.getIdUser());
        assertEquals(2L, newUserRole.getIdClient());
        assertEquals(3L, newUserRole.getIdRole());
        assertNull(newUserRole.getUser());
        assertNull(newUserRole.getClientRole());
    }

    @Test
    void testGettersAndSetters() {
        // Given
        Long idUser = 1L;
        Long idClient = 2L;
        Long idRole = 3L;
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        // When
        userRole.setIdUser(idUser);
        userRole.setIdClient(idClient);
        userRole.setIdRole(idRole);
        userRole.setDateCreated(created);
        userRole.setDateUpdated(updated);

        // Then
        assertEquals(idUser, userRole.getIdUser());
        assertEquals(idClient, userRole.getIdClient());
        assertEquals(idRole, userRole.getIdRole());
        assertEquals(created, userRole.getDateCreated());
        assertEquals(updated, userRole.getDateUpdated());
    }

    @Test
    void testBuilderPattern() {
        // Given & When
        UserRole builtUserRole = UserRole.builder()
                .idUser(1L)
                .idClient(2L)
                .idRole(3L)
                .dateCreated(LocalDateTime.now().minusDays(1))
                .dateUpdated(LocalDateTime.now())
                .build();

        // Then
        assertEquals(1L, builtUserRole.getIdUser());
        assertEquals(2L, builtUserRole.getIdClient());
        assertEquals(3L, builtUserRole.getIdRole());
        assertNotNull(builtUserRole.getDateCreated());
        assertNotNull(builtUserRole.getDateUpdated());
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        Long idUser = 1L;
        Long idClient = 2L;
        Long idRole = 3L;
        LocalDateTime dateCreated = LocalDateTime.now().minusDays(1);
        LocalDateTime dateUpdated = LocalDateTime.now();

        // When
        UserRole userRole = new UserRole(idUser, idClient, idRole, dateCreated, dateUpdated, null, null);

        // Then
        assertEquals(idUser, userRole.getIdUser());
        assertEquals(idClient, userRole.getIdClient());
        assertEquals(idRole, userRole.getIdRole());
        assertEquals(dateCreated, userRole.getDateCreated());
        assertEquals(dateUpdated, userRole.getDateUpdated());
    }

    @Test
    void testToString() {
        // Given
        userRole.setIdUser(1L);
        userRole.setIdClient(2L);
        userRole.setIdRole(3L);
        LocalDateTime now = LocalDateTime.now();
        userRole.setDateCreated(now);
        userRole.setDateUpdated(now);

        // When
        String result = userRole.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("UserRole("));
        assertTrue(result.contains("idUser=1"));
        assertTrue(result.contains("idClient=2"));
        assertTrue(result.contains("idRole=3"));
        assertTrue(result.contains("dateCreated="));
        assertTrue(result.contains("dateUpdated="));
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        UserRole userRole1 = new UserRole();
        userRole1.setIdUser(1L);
        userRole1.setIdClient(2L);
        userRole1.setIdRole(3L);

        UserRole userRole2 = new UserRole();
        userRole2.setIdUser(1L);
        userRole2.setIdClient(2L);
        userRole2.setIdRole(3L);

        UserRole userRole3 = new UserRole();
        userRole3.setIdUser(4L);
        userRole3.setIdClient(5L);
        userRole3.setIdRole(6L);

        // Note: With Lombok @EqualsAndHashCode(onlyExplicitlyIncluded = true),
        // only the ID fields are used for equals/hashCode
        assertEquals(userRole1, userRole2); // Same IDs, so they are equal
        assertEquals(userRole1.hashCode(), userRole2.hashCode()); // Same IDs, same hashCode
        assertNotEquals(userRole1, userRole3); // Different IDs, so not equal
    }

    @Test
    void testEqualsWithNullIds() {
        // Given
        UserRole userRole1 = new UserRole();
        UserRole userRole2 = new UserRole();

        // When & Then
        assertEquals(userRole1, userRole2); // Both have null IDs, so they are equal
        assertEquals(userRole1.hashCode(), userRole2.hashCode());
    }

    @Test
    void testEqualsWithDifferentNullIds() {
        // Given
        UserRole userRole1 = new UserRole();
        userRole1.setIdUser(1L);
        userRole1.setIdClient(2L);
        userRole1.setIdRole(3L);

        UserRole userRole2 = new UserRole();
        // userRole2 has null IDs

        // When & Then
        assertNotEquals(userRole1, userRole2); // Different IDs, so not equal
    }

    @Test
    void testEqualsWithSameObject() {
        // When & Then
        assertEquals(userRole, userRole);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Given
        String differentObject = "not a UserRole";

        // When & Then
        assertNotEquals(userRole, differentObject);
    }

    @Test
    void testEqualsWithNull() {
        // When & Then
        assertNotEquals(userRole, null);
    }

    @Test
    void testNullSafety() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            userRole.setIdUser(null);
            userRole.setIdClient(null);
            userRole.setIdRole(null);
            userRole.setDateCreated(null);
            userRole.setDateUpdated(null);
        });

        assertNull(userRole.getIdUser());
        assertNull(userRole.getIdClient());
        assertNull(userRole.getIdRole());
        assertNull(userRole.getDateCreated());
        assertNull(userRole.getDateUpdated());
    }

    @Test
    void testDateModification() {
        // Given
        LocalDateTime originalCreated = LocalDateTime.now().minusDays(1);
        LocalDateTime originalUpdated = LocalDateTime.now().minusHours(1);
        userRole.setDateCreated(originalCreated);
        userRole.setDateUpdated(originalUpdated);

        // When
        LocalDateTime newCreated = LocalDateTime.now();
        LocalDateTime newUpdated = LocalDateTime.now();
        userRole.setDateCreated(newCreated);
        userRole.setDateUpdated(newUpdated);

        // Then
        assertEquals(newCreated, userRole.getDateCreated());
        assertEquals(newUpdated, userRole.getDateUpdated());
        assertNotEquals(originalCreated, userRole.getDateCreated());
        assertNotEquals(originalUpdated, userRole.getDateUpdated());
    }

    @Test
    void testLongIdValues() {
        // Given
        Long maxLong = Long.MAX_VALUE;

        // When
        userRole.setIdUser(maxLong);
        userRole.setIdClient(maxLong);
        userRole.setIdRole(maxLong);

        // Then
        assertEquals(maxLong, userRole.getIdUser());
        assertEquals(maxLong, userRole.getIdClient());
        assertEquals(maxLong, userRole.getIdRole());
    }

    @Test
    void testZeroIdValues() {
        // Given
        Long zero = 0L;

        // When
        userRole.setIdUser(zero);
        userRole.setIdClient(zero);
        userRole.setIdRole(zero);

        // Then
        assertEquals(zero, userRole.getIdUser());
        assertEquals(zero, userRole.getIdClient());
        assertEquals(zero, userRole.getIdRole());
    }

    @Test
    void testNegativeIdValues() {
        // Given
        Long negative = -1L;

        // When
        userRole.setIdUser(negative);
        userRole.setIdClient(negative);
        userRole.setIdRole(negative);

        // Then
        assertEquals(negative, userRole.getIdUser());
        assertEquals(negative, userRole.getIdClient());
        assertEquals(negative, userRole.getIdRole());
    }

    @Test
    void testUserRoleIdClass() {
        // Given
        UserRole.UserRoleId id1 = new UserRole.UserRoleId(1L, 2L, 3L);
        UserRole.UserRoleId id2 = new UserRole.UserRoleId(1L, 2L, 3L);
        UserRole.UserRoleId id3 = new UserRole.UserRoleId(4L, 5L, 6L);

        // When & Then
        assertEquals(id1, id2); // Same values, so equal
        assertEquals(id1.hashCode(), id2.hashCode()); // Same values, same hashCode
        assertNotEquals(id1, id3); // Different values, so not equal
    }

    @Test
    void testUserRoleIdClassWithDifferentNullValues() {
        // Given
        UserRole.UserRoleId id1 = new UserRole.UserRoleId(1L, 2L, 3L);
        UserRole.UserRoleId id2 = new UserRole.UserRoleId(null, 2L, 3L);

        // When & Then
        assertNotEquals(id1, id2); // Different values, so not equal
    }

    @Test
    void testUserRoleIdClassDefaultConstructor() {
        // Given & When
        UserRole.UserRoleId id = new UserRole.UserRoleId();

        // Then
        assertNull(id.getIdUser());
        assertNull(id.getIdClient());
        assertNull(id.getIdRole());
    }

    @Test
    void testUserRoleIdClassAllArgsConstructor() {
        // Given & When
        UserRole.UserRoleId id = new UserRole.UserRoleId(1L, 2L, 3L);

        // Then
        assertEquals(1L, id.getIdUser());
        assertEquals(2L, id.getIdClient());
        assertEquals(3L, id.getIdRole());
    }

    @Test
    void testUserRoleIdClassSettersAndGetters() {
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

    @Test
    void testUserRoleIdClassToString() {
        // Given
        UserRole.UserRoleId id = new UserRole.UserRoleId(1L, 2L, 3L);

        // When
        String result = id.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("UserRoleId("));
        assertTrue(result.contains("idUser=1"));
        assertTrue(result.contains("idClient=2"));
        assertTrue(result.contains("idRole=3"));
    }

    @Test
    void testUserRoleIdClassEqualsWithSameObject() {
        // Given
        UserRole.UserRoleId id = new UserRole.UserRoleId(1L, 2L, 3L);

        // When & Then
        assertEquals(id, id);
    }

    @Test
    void testUserRoleIdClassEqualsWithDifferentClass() {
        // Given
        UserRole.UserRoleId id = new UserRole.UserRoleId(1L, 2L, 3L);
        String differentObject = "not a UserRoleId";

        // When & Then
        assertNotEquals(id, differentObject);
    }

    @Test
    void testUserRoleIdClassEqualsWithNull() {
        // Given
        UserRole.UserRoleId id = new UserRole.UserRoleId(1L, 2L, 3L);

        // When & Then
        assertNotEquals(id, null);
    }

    @Test
    void testUserRoleIdClassImplementsSerializable() {
        // Given
        UserRole.UserRoleId id = new UserRole.UserRoleId(1L, 2L, 3L);

        // When & Then
        assertTrue(id instanceof Serializable);
    }

    @Test
    void testUserRoleIdClassWithMaxLongValues() {
        // Given
        Long maxLong = Long.MAX_VALUE;
        UserRole.UserRoleId id = new UserRole.UserRoleId(maxLong, maxLong, maxLong);

        // When & Then
        assertEquals(maxLong, id.getIdUser());
        assertEquals(maxLong, id.getIdClient());
        assertEquals(maxLong, id.getIdRole());
    }

    @Test
    void testUserRoleIdClassWithMinLongValues() {
        // Given
        Long minLong = Long.MIN_VALUE;
        UserRole.UserRoleId id = new UserRole.UserRoleId(minLong, minLong, minLong);

        // When & Then
        assertEquals(minLong, id.getIdUser());
        assertEquals(minLong, id.getIdClient());
        assertEquals(minLong, id.getIdRole());
    }

    @Test
    void testUserRoleIdClassWithZeroValues() {
        // Given
        Long zero = 0L;
        UserRole.UserRoleId id = new UserRole.UserRoleId(zero, zero, zero);

        // When & Then
        assertEquals(zero, id.getIdUser());
        assertEquals(zero, id.getIdClient());
        assertEquals(zero, id.getIdRole());
    }

    @Test
    void testUserRoleIdClassWithNegativeValues() {
        // Given
        Long negative = -1L;
        UserRole.UserRoleId id = new UserRole.UserRoleId(negative, negative, negative);

        // When & Then
        assertEquals(negative, id.getIdUser());
        assertEquals(negative, id.getIdClient());
        assertEquals(negative, id.getIdRole());
    }

    @Test
    void testUserRoleIdClassWithMixedValues() {
        // Given
        Long userId = 1L;
        Long clientId = 2L;
        Long roleId = 3L;
        UserRole.UserRoleId id = new UserRole.UserRoleId(userId, clientId, roleId);

        // When & Then
        assertEquals(userId, id.getIdUser());
        assertEquals(clientId, id.getIdClient());
        assertEquals(roleId, id.getIdRole());
    }

    @Test
    void testUserRoleIdClassWithNullValues() {
        // Given
        UserRole.UserRoleId id = new UserRole.UserRoleId(null, null, null);

        // When & Then
        assertNull(id.getIdUser());
        assertNull(id.getIdClient());
        assertNull(id.getIdRole());
    }

    @Test
    void testUserRoleIdClassWithOneNullValue() {
        // Given
        UserRole.UserRoleId id = new UserRole.UserRoleId(1L, null, 3L);

        // When & Then
        assertEquals(1L, id.getIdUser());
        assertNull(id.getIdClient());
        assertEquals(3L, id.getIdRole());
    }

    @Test
    void testUserRoleIdClassWithTwoNullValues() {
        // Given
        UserRole.UserRoleId id = new UserRole.UserRoleId(null, null, 3L);

        // When & Then
        assertNull(id.getIdUser());
        assertNull(id.getIdClient());
        assertEquals(3L, id.getIdRole());
    }

    @Test
    void testUserRoleIdClassHashCodeConsistency() {
        // Given
        UserRole.UserRoleId id1 = new UserRole.UserRoleId(1L, 2L, 3L);
        UserRole.UserRoleId id2 = new UserRole.UserRoleId(1L, 2L, 3L);

        // When & Then
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testUserRoleIdClassHashCodeDifferentValues() {
        // Given
        UserRole.UserRoleId id1 = new UserRole.UserRoleId(1L, 2L, 3L);
        UserRole.UserRoleId id2 = new UserRole.UserRoleId(4L, 5L, 6L);

        // When & Then
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testUserRoleIdClassHashCodeWithNullValues() {
        // Given
        UserRole.UserRoleId id1 = new UserRole.UserRoleId(null, null, null);
        UserRole.UserRoleId id2 = new UserRole.UserRoleId(null, null, null);

        // When & Then
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testUserRoleIdClassHashCodeWithMixedNullValues() {
        // Given
        UserRole.UserRoleId id1 = new UserRole.UserRoleId(1L, null, 3L);
        UserRole.UserRoleId id2 = new UserRole.UserRoleId(1L, null, 3L);

        // When & Then
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testUserRoleIdClassHashCodeWithDifferentNullValues() {
        // Given
        UserRole.UserRoleId id1 = new UserRole.UserRoleId(1L, 2L, 3L);
        UserRole.UserRoleId id2 = new UserRole.UserRoleId(1L, null, 3L);

        // When & Then
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testUserRoleIdClassEqualsWithPartialNullValues() {
        // Given
        UserRole.UserRoleId id1 = new UserRole.UserRoleId(1L, 2L, 3L);
        UserRole.UserRoleId id2 = new UserRole.UserRoleId(1L, 2L, null);

        // When & Then
        assertNotEquals(id1, id2);
    }

    @Test
    void testUserRoleIdClassEqualsWithAllNullValues() {
        // Given
        UserRole.UserRoleId id1 = new UserRole.UserRoleId(null, null, null);
        UserRole.UserRoleId id2 = new UserRole.UserRoleId(null, null, null);

        // When & Then
        assertEquals(id1, id2);
    }

    @Test
    void testUserRoleIdClassEqualsWithDifferentNullCombinations() {
        // Given
        UserRole.UserRoleId id1 = new UserRole.UserRoleId(1L, null, 3L);
        UserRole.UserRoleId id2 = new UserRole.UserRoleId(null, 2L, 3L);

        // When & Then
        assertNotEquals(id1, id2);
    }
}
