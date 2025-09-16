package com.example.login.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        Role newRole = new Role();

        // Then
        assertNull(newRole.getId());
        assertNull(newRole.getDescription());
        assertFalse(newRole.getInternal()); // Default value should be false
        assertNull(newRole.getDateCreated());
        assertNull(newRole.getDateUpdated());
    }

    @Test
    void testParameterizedConstructorWithDescription() {
        // Given & When
        Role newRole = new Role("Admin Role");

        // Then
        assertEquals("Admin Role", newRole.getDescription());
        assertNull(newRole.getId());
        assertFalse(newRole.getInternal()); // Should use default value
    }

    @Test
    void testParameterizedConstructorWithDescriptionAndInternal() {
        // Given & When
        Role newRole = new Role("System Role", true);

        // Then
        assertEquals("System Role", newRole.getDescription());
        assertTrue(newRole.getInternal());
        assertNull(newRole.getId());
    }

    @Test
    void testGettersAndSetters() {
        // Given
        Long id = 1L;
        String description = "Test Role";
        Boolean internal = true;
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        // When
        role.setId(id);
        role.setDescription(description);
        role.setInternal(internal);
        role.setDateCreated(created);
        role.setDateUpdated(updated);

        // Then
        assertEquals(id, role.getId());
        assertEquals(description, role.getDescription());
        assertEquals(internal, role.getInternal());
        assertEquals(created, role.getDateCreated());
        assertEquals(updated, role.getDateUpdated());
    }

    @Test
    void testBuilderPattern() {
        // Given & When
        Role builtRole = Role.builder()
                .id(1L)
                .description("Builder Role")
                .internal(true)
                .dateCreated(LocalDateTime.now().minusDays(1))
                .dateUpdated(LocalDateTime.now())
                .build();

        // Then
        assertEquals(1L, builtRole.getId());
        assertEquals("Builder Role", builtRole.getDescription());
        assertTrue(builtRole.getInternal());
        assertNotNull(builtRole.getDateCreated());
        assertNotNull(builtRole.getDateUpdated());
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        Long id = 1L;
        String description = "Test Role";
        Boolean internal = true;
        LocalDateTime dateCreated = LocalDateTime.now().minusDays(1);
        LocalDateTime dateUpdated = LocalDateTime.now();

        // When
        Role role = new Role(id, description, internal, dateCreated, dateUpdated, null);

        // Then
        assertEquals(id, role.getId());
        assertEquals(description, role.getDescription());
        assertEquals(internal, role.getInternal());
        assertEquals(dateCreated, role.getDateCreated());
        assertEquals(dateUpdated, role.getDateUpdated());
    }

    @Test
    void testToString() {
        // Given
        role.setId(1L);
        role.setDescription("Test Role");
        role.setInternal(true);
        LocalDateTime now = LocalDateTime.now();
        role.setDateCreated(now);
        role.setDateUpdated(now);

        // When
        String result = role.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Role("));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("description=Test Role"));
        assertTrue(result.contains("internal=true"));
        assertTrue(result.contains("dateCreated="));
        assertTrue(result.contains("dateUpdated="));
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        Role role1 = new Role();
        role1.setId(1L);
        role1.setDescription("Test Role");

        Role role2 = new Role();
        role2.setId(1L);
        role2.setDescription("Test Role");

        Role role3 = new Role();
        role3.setId(2L);
        role3.setDescription("Different Role");

        // Note: With Lombok @EqualsAndHashCode(onlyExplicitlyIncluded = true),
        // only the ID field is used for equals/hashCode
        assertEquals(role1, role2); // Same ID (1L), so they are equal
        assertEquals(role1.hashCode(), role2.hashCode()); // Same ID, same hashCode
        assertNotEquals(role1, role3); // Different ID (2L), so not equal
    }

    @Test
    void testEqualsWithNullId() {
        // Given
        Role role1 = new Role();
        Role role2 = new Role();

        // When & Then
        assertEquals(role1, role2); // Both have null ID, so they are equal
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    void testEqualsWithDifferentNullId() {
        // Given
        Role role1 = new Role();
        role1.setId(1L);

        Role role2 = new Role();
        // role2 has null ID

        // When & Then
        assertNotEquals(role1, role2); // Different ID (1L vs null), so not equal
    }

    @Test
    void testEqualsWithSameObject() {
        // When & Then
        assertEquals(role, role);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Given
        String differentObject = "not a Role";

        // When & Then
        assertNotEquals(role, differentObject);
    }

    @Test
    void testEqualsWithNull() {
        // When & Then
        assertNotEquals(role, null);
    }

    @Test
    void testMaxLengthDescription() {
        // Given
        String maxLengthDescription = "a".repeat(191); // Max length according to schema

        // When
        role.setDescription(maxLengthDescription);

        // Then
        assertEquals(maxLengthDescription, role.getDescription());
        assertEquals(191, role.getDescription().length());
    }

    @Test
    void testSpecialCharactersInDescription() {
        // Given
        String specialDescription = "Role & Co. (Ltd.) - Special chars: @#$%^&*()_+-=[]{}|;':\",./<>?`~";

        // When
        role.setDescription(specialDescription);

        // Then
        assertEquals(specialDescription, role.getDescription());
    }

    @Test
    void testUnicodeCharactersInDescription() {
        // Given
        String unicodeDescription = "Rol Español 中文角色 الدور العربي";

        // When
        role.setDescription(unicodeDescription);

        // Then
        assertEquals(unicodeDescription, role.getDescription());
    }

    @Test
    void testWhitespaceInDescription() {
        // Given
        String descriptionWithSpaces = "  Role Description  ";

        // When
        role.setDescription(descriptionWithSpaces);

        // Then
        assertEquals(descriptionWithSpaces, role.getDescription());
    }

    @Test
    void testEmptyDescription() {
        // Given
        String emptyDescription = "";

        // When
        role.setDescription(emptyDescription);

        // Then
        assertEquals(emptyDescription, role.getDescription());
    }

    @Test
    void testNullDescription() {
        // When
        role.setDescription(null);

        // Then
        assertNull(role.getDescription());
    }

    @Test
    void testInternalBooleanValues() {
        // Test true
        role.setInternal(true);
        assertTrue(role.getInternal());

        // Test false
        role.setInternal(false);
        assertFalse(role.getInternal());

        // Test null
        role.setInternal(null);
        assertNull(role.getInternal());
    }

    @Test
    void testDefaultInternalValue() {
        // Given & When
        Role newRole = new Role();

        // Then
        assertFalse(newRole.getInternal()); // Should be false by default
    }

    @Test
    void testBuilderWithDefaultInternal() {
        // Given & When
        Role builtRole = Role.builder()
                .id(1L)
                .description("Test Role")
                .build();

        // Then
        assertFalse(builtRole.getInternal()); // Should use default value
    }

    @Test
    void testBuilderWithExplicitInternal() {
        // Given & When
        Role builtRole = Role.builder()
                .id(1L)
                .description("Test Role")
                .internal(true)
                .build();

        // Then
        assertTrue(builtRole.getInternal());
    }

    @Test
    void testNullSafety() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            role.setId(null);
            role.setDescription(null);
            role.setInternal(null);
            role.setDateCreated(null);
            role.setDateUpdated(null);
        });

        assertNull(role.getId());
        assertNull(role.getDescription());
        assertNull(role.getInternal());
        assertNull(role.getDateCreated());
        assertNull(role.getDateUpdated());
    }

    @Test
    void testDateModification() {
        // Given
        LocalDateTime originalCreated = LocalDateTime.now().minusDays(1);
        LocalDateTime originalUpdated = LocalDateTime.now().minusHours(1);
        role.setDateCreated(originalCreated);
        role.setDateUpdated(originalUpdated);

        // When
        LocalDateTime newCreated = LocalDateTime.now();
        LocalDateTime newUpdated = LocalDateTime.now();
        role.setDateCreated(newCreated);
        role.setDateUpdated(newUpdated);

        // Then
        assertEquals(newCreated, role.getDateCreated());
        assertEquals(newUpdated, role.getDateUpdated());
        assertNotEquals(originalCreated, role.getDateCreated());
        assertNotEquals(originalUpdated, role.getDateUpdated());
    }

    @Test
    void testLongDescription() {
        // Given
        String longDescription = "This is a very long role description that contains multiple words and should be able to handle longer text content without any issues. It should test the maximum length handling capabilities of the role description field.";

        // When
        role.setDescription(longDescription);

        // Then
        assertEquals(longDescription, role.getDescription());
        assertTrue(role.getDescription().length() > 100);
    }

    @Test
    void testRoleWithNumbersInDescription() {
        // Given
        String descriptionWithNumbers = "Role 123 - Version 2.0 - ID: 456789";

        // When
        role.setDescription(descriptionWithNumbers);

        // Then
        assertEquals(descriptionWithNumbers, role.getDescription());
    }

    @Test
    void testRoleWithMixedCaseDescription() {
        // Given
        String mixedCaseDescription = "AdMiN RoLe - sYsTeM aCcEsS";

        // When
        role.setDescription(mixedCaseDescription);

        // Then
        assertEquals(mixedCaseDescription, role.getDescription());
    }

    @Test
    void testRoleWithSpecialFormatting() {
        // Given
        String formattedDescription = "Role: Admin\nDescription: Full system access\nLevel: 1";

        // When
        role.setDescription(formattedDescription);

        // Then
        assertEquals(formattedDescription, role.getDescription());
    }

    @Test
    void testRoleWithHtmlTags() {
        // Given
        String htmlDescription = "Role <b>Admin</b> - <i>System Access</i>";

        // When
        role.setDescription(htmlDescription);

        // Then
        assertEquals(htmlDescription, role.getDescription());
    }

    @Test
    void testRoleWithJsonLikeDescription() {
        // Given
        String jsonDescription = "{\"role\": \"admin\", \"permissions\": [\"read\", \"write\", \"delete\"]}";

        // When
        role.setDescription(jsonDescription);

        // Then
        assertEquals(jsonDescription, role.getDescription());
    }

    @Test
    void testRoleWithSqlLikeDescription() {
        // Given
        String sqlDescription = "SELECT * FROM users WHERE role = 'admin'";

        // When
        role.setDescription(sqlDescription);

        // Then
        assertEquals(sqlDescription, role.getDescription());
    }
}
