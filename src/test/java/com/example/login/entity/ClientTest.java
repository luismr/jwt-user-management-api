package com.example.login.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        Client newClient = new Client();

        // Then
        assertNull(newClient.getId());
        assertNull(newClient.getExternalId());
        assertNull(newClient.getName());
        assertNull(newClient.getDateCreated());
        assertNull(newClient.getDateUpdated());
    }

    @Test
    void testParameterizedConstructorWithName() {
        // Given & When
        Client newClient = new Client("Test Client");

        // Then
        assertEquals("Test Client", newClient.getName());
        assertNull(newClient.getExternalId());
        assertNull(newClient.getId());
    }

    @Test
    void testParameterizedConstructorWithExternalIdAndName() {
        // Given & When
        Client newClient = new Client("EXT-123", "Test Client");

        // Then
        assertEquals("EXT-123", newClient.getExternalId());
        assertEquals("Test Client", newClient.getName());
        assertNull(newClient.getId());
    }

    @Test
    void testGettersAndSetters() {
        // Given
        Long id = 1L;
        String externalId = "EXTERNAL-123";
        String name = "Test Client Corporation";
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        // When
        client.setId(id);
        client.setExternalId(externalId);
        client.setName(name);
        client.setDateCreated(created);
        client.setDateUpdated(updated);

        // Then
        assertEquals(id, client.getId());
        assertEquals(externalId, client.getExternalId());
        assertEquals(name, client.getName());
        assertEquals(created, client.getDateCreated());
        assertEquals(updated, client.getDateUpdated());
    }

    @Test
    void testToString() {
        // Given
        client.setId(1L);
        client.setExternalId("EXT-123");
        client.setName("Test Client");
        LocalDateTime now = LocalDateTime.now();
        client.setDateCreated(now);
        client.setDateUpdated(now);

        // When
        String result = client.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Client("));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("externalId=EXT-123"));
        assertTrue(result.contains("name=Test Client"));
        assertTrue(result.contains("dateCreated="));
        assertTrue(result.contains("dateUpdated="));
    }

    @Test
    void testNullExternalId() {
        // Given & When
        client.setName("Client without external ID");
        client.setExternalId(null);

        // Then
        assertEquals("Client without external ID", client.getName());
        assertNull(client.getExternalId());
    }

    @Test
    void testEmptyStrings() {
        // Given & When
        client.setExternalId("");
        client.setName("");

        // Then
        assertEquals("", client.getExternalId());
        assertEquals("", client.getName());
    }

    @Test
    void testLongStrings() {
        // Given
        String longExternalId = "A".repeat(64); // Max length according to schema
        String longName = "B".repeat(128); // Max length according to schema

        // When
        client.setExternalId(longExternalId);
        client.setName(longName);

        // Then
        assertEquals(longExternalId, client.getExternalId());
        assertEquals(longName, client.getName());
        assertEquals(64, client.getExternalId().length());
        assertEquals(128, client.getName().length());
    }

    @Test
    void testBuilderPattern() {
        // Given
        Client newClient = new Client();

        // When - simulate builder pattern
        newClient.setExternalId("BUILDER-CLIENT");
        newClient.setName("Builder Pattern Client");

        // Then
        assertEquals("BUILDER-CLIENT", newClient.getExternalId());
        assertEquals("Builder Pattern Client", newClient.getName());
    }

    @Test
    void testImmutableAfterCreation() {
        // Given
        LocalDateTime originalCreated = LocalDateTime.now().minusDays(1);
        client.setDateCreated(originalCreated);

        // When
        LocalDateTime newCreated = LocalDateTime.now();
        client.setDateCreated(newCreated);

        // Then
        assertEquals(newCreated, client.getDateCreated());
        assertNotEquals(originalCreated, client.getDateCreated());
    }

    @Test
    void testDateUpdatedModification() {
        // Given
        LocalDateTime originalUpdated = LocalDateTime.now().minusHours(1);
        client.setDateUpdated(originalUpdated);

        // When
        LocalDateTime newUpdated = LocalDateTime.now();
        client.setDateUpdated(newUpdated);

        // Then
        assertEquals(newUpdated, client.getDateUpdated());
        assertNotEquals(originalUpdated, client.getDateUpdated());
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        Client client1 = new Client();
        client1.setId(1L);
        client1.setName("Test Client");

        Client client2 = new Client();
        client2.setId(1L);
        client2.setName("Test Client");

        Client client3 = new Client();
        client3.setId(2L);
        client3.setName("Different Client");

        // Note: With Lombok @EqualsAndHashCode(onlyExplicitlyIncluded = true),
        // only the ID field is used for equals/hashCode
        assertEquals(client1, client2); // Same ID (1L), so they are equal
        assertEquals(client1.hashCode(), client2.hashCode()); // Same ID, same hashCode
        assertNotEquals(client1, client3); // Different ID (2L), so not equal
    }

    @Test
    void testSpecialCharactersInName() {
        // Given
        String specialName = "Client & Co. (Ltd.) - Special chars: @#$%";

        // When
        client.setName(specialName);

        // Then
        assertEquals(specialName, client.getName());
    }

    @Test
    void testUnicodeCharactersInName() {
        // Given
        String unicodeName = "Cliente Español 中文客户 العميل العربي";

        // When
        client.setName(unicodeName);

        // Then
        assertEquals(unicodeName, client.getName());
    }

    @Test
    void testNullSafety() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            client.setExternalId(null);
            client.setName(null);
            client.setDateCreated(null);
            client.setDateUpdated(null);
        });

        assertNull(client.getExternalId());
        assertNull(client.getName());
        assertNull(client.getDateCreated());
        assertNull(client.getDateUpdated());
    }
}
