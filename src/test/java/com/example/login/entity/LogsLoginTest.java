package com.example.login.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LogsLoginTest {

    private LogsLogin logsLogin;

    @BeforeEach
    void setUp() {
        logsLogin = new LogsLogin();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        LogsLogin newLogsLogin = new LogsLogin();

        // Then
        assertNull(newLogsLogin.getId());
        assertNull(newLogsLogin.getUserId());
        assertNull(newLogsLogin.getUser());
        assertNull(newLogsLogin.getType());
        assertNull(newLogsLogin.getDateCreated());
        assertNull(newLogsLogin.getDateUpdated());
    }

    @Test
    void testParameterizedConstructor() {
        // Given & When
        LogsLogin newLogsLogin = new LogsLogin(1L, "LOGIN");

        // Then
        assertEquals(1L, newLogsLogin.getUserId());
        assertEquals("LOGIN", newLogsLogin.getType());
        assertNull(newLogsLogin.getId());
    }

    @Test
    void testGettersAndSetters() {
        // Given
        Long id = 1L;
        Long userId = 100L;
        String type = "LOGIN";
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        // When
        logsLogin.setId(id);
        logsLogin.setUserId(userId);
        logsLogin.setType(type);
        logsLogin.setDateCreated(created);
        logsLogin.setDateUpdated(updated);

        // Then
        assertEquals(id, logsLogin.getId());
        assertEquals(userId, logsLogin.getUserId());
        assertEquals(type, logsLogin.getType());
        assertEquals(created, logsLogin.getDateCreated());
        assertEquals(updated, logsLogin.getDateUpdated());
    }

    @Test
    void testBuilderPattern() {
        // Given & When
        LogsLogin builtLogsLogin = LogsLogin.builder()
                .id(1L)
                .userId(100L)
                .type("LOGOUT")
                .dateCreated(LocalDateTime.now().minusDays(1))
                .dateUpdated(LocalDateTime.now())
                .build();

        // Then
        assertEquals(1L, builtLogsLogin.getId());
        assertEquals(100L, builtLogsLogin.getUserId());
        assertEquals("LOGOUT", builtLogsLogin.getType());
        assertNotNull(builtLogsLogin.getDateCreated());
        assertNotNull(builtLogsLogin.getDateUpdated());
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        Long id = 1L;
        Long userId = 100L;
        String type = "LOGIN";
        LocalDateTime dateCreated = LocalDateTime.now().minusDays(1);
        LocalDateTime dateUpdated = LocalDateTime.now();

        // When
        LogsLogin logsLogin = new LogsLogin(id, userId, null, type, dateCreated, dateUpdated);

        // Then
        assertEquals(id, logsLogin.getId());
        assertEquals(userId, logsLogin.getUserId());
        assertEquals(type, logsLogin.getType());
        assertEquals(dateCreated, logsLogin.getDateCreated());
        assertEquals(dateUpdated, logsLogin.getDateUpdated());
    }

    @Test
    void testToString() {
        // Given
        logsLogin.setId(1L);
        logsLogin.setUserId(100L);
        logsLogin.setType("LOGIN");
        LocalDateTime now = LocalDateTime.now();
        logsLogin.setDateCreated(now);
        logsLogin.setDateUpdated(now);

        // When
        String result = logsLogin.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("LogsLogin("));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("userId=100"));
        assertTrue(result.contains("type=LOGIN"));
        assertTrue(result.contains("dateCreated="));
        assertTrue(result.contains("dateUpdated="));
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        LogsLogin logsLogin1 = new LogsLogin();
        logsLogin1.setId(1L);
        logsLogin1.setUserId(100L);

        LogsLogin logsLogin2 = new LogsLogin();
        logsLogin2.setId(1L);
        logsLogin2.setUserId(100L);

        LogsLogin logsLogin3 = new LogsLogin();
        logsLogin3.setId(2L);
        logsLogin3.setUserId(200L);

        // Note: With Lombok @EqualsAndHashCode(onlyExplicitlyIncluded = true),
        // only the ID field is used for equals/hashCode
        assertEquals(logsLogin1, logsLogin2); // Same ID (1L), so they are equal
        assertEquals(logsLogin1.hashCode(), logsLogin2.hashCode()); // Same ID, same hashCode
        assertNotEquals(logsLogin1, logsLogin3); // Different ID (2L), so not equal
    }

    @Test
    void testEqualsWithNullId() {
        // Given
        LogsLogin logsLogin1 = new LogsLogin();
        LogsLogin logsLogin2 = new LogsLogin();

        // When & Then
        assertEquals(logsLogin1, logsLogin2); // Both have null ID, so they are equal
        assertEquals(logsLogin1.hashCode(), logsLogin2.hashCode());
    }

    @Test
    void testEqualsWithDifferentNullId() {
        // Given
        LogsLogin logsLogin1 = new LogsLogin();
        logsLogin1.setId(1L);

        LogsLogin logsLogin2 = new LogsLogin();
        // logsLogin2 has null ID

        // When & Then
        assertNotEquals(logsLogin1, logsLogin2); // Different ID (1L vs null), so not equal
    }

    @Test
    void testEqualsWithSameObject() {
        // When & Then
        assertEquals(logsLogin, logsLogin);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Given
        String differentObject = "not a LogsLogin";

        // When & Then
        assertNotEquals(logsLogin, differentObject);
    }

    @Test
    void testEqualsWithNull() {
        // When & Then
        assertNotEquals(logsLogin, null);
    }

    @Test
    void testMaxLengthType() {
        // Given
        String maxLengthType = "a".repeat(64); // Max length according to schema

        // When
        logsLogin.setType(maxLengthType);

        // Then
        assertEquals(maxLengthType, logsLogin.getType());
        assertEquals(64, logsLogin.getType().length());
    }

    @Test
    void testSpecialCharactersInType() {
        // Given
        String specialType = "LOGIN_SPECIAL@#$%^&*()_+-=[]{}|;':\",./<>?`~";

        // When
        logsLogin.setType(specialType);

        // Then
        assertEquals(specialType, logsLogin.getType());
    }

    @Test
    void testUnicodeCharactersInType() {
        // Given
        String unicodeType = "登录日志中文";

        // When
        logsLogin.setType(unicodeType);

        // Then
        assertEquals(unicodeType, logsLogin.getType());
    }

    @Test
    void testWhitespaceInType() {
        // Given
        String typeWithSpaces = "  LOGIN  ";

        // When
        logsLogin.setType(typeWithSpaces);

        // Then
        assertEquals(typeWithSpaces, logsLogin.getType());
    }

    @Test
    void testEmptyType() {
        // Given
        String emptyType = "";

        // When
        logsLogin.setType(emptyType);

        // Then
        assertEquals(emptyType, logsLogin.getType());
    }

    @Test
    void testNullType() {
        // When
        logsLogin.setType(null);

        // Then
        assertNull(logsLogin.getType());
    }

    @Test
    void testCommonLoginTypes() {
        // Test common login types
        String[] commonTypes = {"LOGIN", "LOGOUT", "LOGIN_FAILED", "PASSWORD_RESET", "ACCOUNT_LOCKED", "ACCOUNT_UNLOCKED"};

        for (String type : commonTypes) {
            // When
            logsLogin.setType(type);

            // Then
            assertEquals(type, logsLogin.getType());
        }
    }

    @Test
    void testNumericTypes() {
        // Given
        String numericType = "LOGIN_123";

        // When
        logsLogin.setType(numericType);

        // Then
        assertEquals(numericType, logsLogin.getType());
    }

    @Test
    void testMixedCaseTypes() {
        // Given
        String mixedCaseType = "LoGiN_AtTeMpT";

        // When
        logsLogin.setType(mixedCaseType);

        // Then
        assertEquals(mixedCaseType, logsLogin.getType());
    }

    @Test
    void testTypeWithUnderscores() {
        // Given
        String underscoreType = "LOGIN_ATTEMPT_FAILED";

        // When
        logsLogin.setType(underscoreType);

        // Then
        assertEquals(underscoreType, logsLogin.getType());
    }

    @Test
    void testTypeWithDashes() {
        // Given
        String dashType = "LOGIN-ATTEMPT-FAILED";

        // When
        logsLogin.setType(dashType);

        // Then
        assertEquals(dashType, logsLogin.getType());
    }

    @Test
    void testTypeWithDots() {
        // Given
        String dotType = "LOGIN.ATTEMPT.FAILED";

        // When
        logsLogin.setType(dotType);

        // Then
        assertEquals(dotType, logsLogin.getType());
    }

    @Test
    void testTypeWithNumbers() {
        // Given
        String numberType = "LOGIN_2023_12_25";

        // When
        logsLogin.setType(numberType);

        // Then
        assertEquals(numberType, logsLogin.getType());
    }

    @Test
    void testTypeWithSpecialFormatting() {
        // Given
        String formattedType = "LOGIN\nATTEMPT\nFAILED";

        // When
        logsLogin.setType(formattedType);

        // Then
        assertEquals(formattedType, logsLogin.getType());
    }

    @Test
    void testTypeWithJsonLikeFormat() {
        // Given
        String jsonType = "{\"action\": \"LOGIN\", \"status\": \"FAILED\"}";

        // When
        logsLogin.setType(jsonType);

        // Then
        assertEquals(jsonType, logsLogin.getType());
    }

    @Test
    void testNullSafety() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            logsLogin.setId(null);
            logsLogin.setUserId(null);
            logsLogin.setType(null);
            logsLogin.setDateCreated(null);
            logsLogin.setDateUpdated(null);
        });

        assertNull(logsLogin.getId());
        assertNull(logsLogin.getUserId());
        assertNull(logsLogin.getType());
        assertNull(logsLogin.getDateCreated());
        assertNull(logsLogin.getDateUpdated());
    }

    @Test
    void testDateModification() {
        // Given
        LocalDateTime originalCreated = LocalDateTime.now().minusDays(1);
        LocalDateTime originalUpdated = LocalDateTime.now().minusHours(1);
        logsLogin.setDateCreated(originalCreated);
        logsLogin.setDateUpdated(originalUpdated);

        // When
        LocalDateTime newCreated = LocalDateTime.now();
        LocalDateTime newUpdated = LocalDateTime.now();
        logsLogin.setDateCreated(newCreated);
        logsLogin.setDateUpdated(newUpdated);

        // Then
        assertEquals(newCreated, logsLogin.getDateCreated());
        assertEquals(newUpdated, logsLogin.getDateUpdated());
        assertNotEquals(originalCreated, logsLogin.getDateCreated());
        assertNotEquals(originalUpdated, logsLogin.getDateUpdated());
    }

    @Test
    void testLongType() {
        // Given
        String longType = "This is a very long log type description that contains multiple words and should be able to handle longer text content without any issues. It should test the maximum length handling capabilities of the log type field.";

        // When
        logsLogin.setType(longType);

        // Then
        assertEquals(longType, logsLogin.getType());
        assertTrue(logsLogin.getType().length() > 100);
    }

    @Test
    void testTypeWithHtmlTags() {
        // Given
        String htmlType = "LOGIN <b>ATTEMPT</b> - <i>FAILED</i>";

        // When
        logsLogin.setType(htmlType);

        // Then
        assertEquals(htmlType, logsLogin.getType());
    }

    @Test
    void testTypeWithSqlLikeFormat() {
        // Given
        String sqlType = "SELECT * FROM logs WHERE type = 'LOGIN'";

        // When
        logsLogin.setType(sqlType);

        // Then
        assertEquals(sqlType, logsLogin.getType());
    }

    @Test
    void testTypeWithXmlLikeFormat() {
        // Given
        String xmlType = "<log type=\"LOGIN\" status=\"FAILED\"/>";

        // When
        logsLogin.setType(xmlType);

        // Then
        assertEquals(xmlType, logsLogin.getType());
    }

    @Test
    void testTypeWithUrlLikeFormat() {
        // Given
        String urlType = "https://api.example.com/login/attempt";

        // When
        logsLogin.setType(urlType);

        // Then
        assertEquals(urlType, logsLogin.getType());
    }

    @Test
    void testTypeWithEmailLikeFormat() {
        // Given
        String emailType = "user@example.com_LOGIN_ATTEMPT";

        // When
        logsLogin.setType(emailType);

        // Then
        assertEquals(emailType, logsLogin.getType());
    }

    @Test
    void testTypeWithBase64LikeFormat() {
        // Given
        String base64Type = "TE9HSU5fQVRURU1QVF9GQUlMRUQ=";

        // When
        logsLogin.setType(base64Type);

        // Then
        assertEquals(base64Type, logsLogin.getType());
    }

    @Test
    void testTypeWithUuidLikeFormat() {
        // Given
        String uuidType = "LOGIN_550e8400-e29b-41d4-a716-446655440000";

        // When
        logsLogin.setType(uuidType);

        // Then
        assertEquals(uuidType, logsLogin.getType());
    }
}
