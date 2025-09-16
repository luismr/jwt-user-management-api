package com.example.login.repository;

import com.example.login.entity.LogsLogin;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogsLoginRepositoryTest {

    @Mock
    private LogsLoginRepository logsLoginRepository;

    private LogsLogin loginLog1;
    private LogsLogin loginLog2;
    private LogsLogin loginLog3;

    @BeforeEach
    void setUp() {
        loginLog1 = new LogsLogin();
        loginLog1.setId(1L);
        loginLog1.setUserId(1L);
        loginLog1.setType("LOGIN_SUCCESS");
        loginLog1.setDateCreated(LocalDateTime.now().minusHours(1));
        loginLog1.setDateUpdated(LocalDateTime.now().minusHours(1));

        loginLog2 = new LogsLogin();
        loginLog2.setId(2L);
        loginLog2.setUserId(1L);
        loginLog2.setType("LOGIN_FAILURE");
        loginLog2.setDateCreated(LocalDateTime.now().minusMinutes(30));
        loginLog2.setDateUpdated(LocalDateTime.now().minusMinutes(30));

        loginLog3 = new LogsLogin();
        loginLog3.setId(3L);
        loginLog3.setUserId(2L);
        loginLog3.setType("LOGIN_SUCCESS");
        loginLog3.setDateCreated(LocalDateTime.now().minusMinutes(15));
        loginLog3.setDateUpdated(LocalDateTime.now().minusMinutes(15));
    }

    @Test
    void testFindByUserIdOrderByDateCreatedDesc() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<LogsLogin> userLogs = Arrays.asList(loginLog2, loginLog1); // Ordered by date desc
        Page<LogsLogin> logsPage = new PageImpl<>(userLogs, pageable, userLogs.size());
        when(logsLoginRepository.findByUserIdOrderByDateCreatedDesc(1L, pageable)).thenReturn(logsPage);

        // When
        Page<LogsLogin> result = logsLoginRepository.findByUserIdOrderByDateCreatedDesc(1L, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(log -> log.getUserId().equals(1L)));
        verify(logsLoginRepository, times(1)).findByUserIdOrderByDateCreatedDesc(1L, pageable);
    }

    @Test
    void testFindByUserIdOrderByDateCreatedDesc_EmptyResult() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<LogsLogin> emptyPage = new PageImpl<>(Arrays.asList(), pageable, 0);
        when(logsLoginRepository.findByUserIdOrderByDateCreatedDesc(999L, pageable)).thenReturn(emptyPage);

        // When
        Page<LogsLogin> result = logsLoginRepository.findByUserIdOrderByDateCreatedDesc(999L, pageable);

        // Then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(logsLoginRepository, times(1)).findByUserIdOrderByDateCreatedDesc(999L, pageable);
    }

    @Test
    void testFindByTypeOrderByDateCreatedDesc() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<LogsLogin> successLogs = Arrays.asList(loginLog3, loginLog1); // Ordered by date desc
        Page<LogsLogin> logsPage = new PageImpl<>(successLogs, pageable, successLogs.size());
        when(logsLoginRepository.findByTypeOrderByDateCreatedDesc("LOGIN_SUCCESS", pageable)).thenReturn(logsPage);

        // When
        Page<LogsLogin> result = logsLoginRepository.findByTypeOrderByDateCreatedDesc("LOGIN_SUCCESS", pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(log -> log.getType().equals("LOGIN_SUCCESS")));
        verify(logsLoginRepository, times(1)).findByTypeOrderByDateCreatedDesc("LOGIN_SUCCESS", pageable);
    }

    @Test
    void testFindByUserIdAndTypeOrderByDateCreatedDesc() {
        // Given
        List<LogsLogin> userSuccessLogs = Arrays.asList(loginLog1);
        when(logsLoginRepository.findByUserIdAndTypeOrderByDateCreatedDesc(1L, "LOGIN_SUCCESS"))
                .thenReturn(userSuccessLogs);

        // When
        List<LogsLogin> result = logsLoginRepository.findByUserIdAndTypeOrderByDateCreatedDesc(1L, "LOGIN_SUCCESS");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals("LOGIN_SUCCESS", result.get(0).getType());
        verify(logsLoginRepository, times(1)).findByUserIdAndTypeOrderByDateCreatedDesc(1L, "LOGIN_SUCCESS");
    }

    @Test
    void testFindRecentLogs() {
        // Given
        LocalDateTime since = LocalDateTime.now().minusHours(2);
        Pageable pageable = PageRequest.of(0, 10);
        List<LogsLogin> recentLogs = Arrays.asList(loginLog3, loginLog2, loginLog1);
        Page<LogsLogin> logsPage = new PageImpl<>(recentLogs, pageable, recentLogs.size());
        when(logsLoginRepository.findRecentLogs(since, pageable)).thenReturn(logsPage);

        // When
        Page<LogsLogin> result = logsLoginRepository.findRecentLogs(since, pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        verify(logsLoginRepository, times(1)).findRecentLogs(since, pageable);
    }

    @Test
    void testFindByDateRange() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusHours(2);
        LocalDateTime endDate = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10);
        List<LogsLogin> logsInRange = Arrays.asList(loginLog3, loginLog2, loginLog1);
        Page<LogsLogin> logsPage = new PageImpl<>(logsInRange, pageable, logsInRange.size());
        when(logsLoginRepository.findByDateRange(startDate, endDate, pageable)).thenReturn(logsPage);

        // When
        Page<LogsLogin> result = logsLoginRepository.findByDateRange(startDate, endDate, pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        verify(logsLoginRepository, times(1)).findByDateRange(startDate, endDate, pageable);
    }

    @Test
    void testCountByUserId() {
        // Given
        when(logsLoginRepository.countByUserId(1L)).thenReturn(2L);

        // When
        long result = logsLoginRepository.countByUserId(1L);

        // Then
        assertEquals(2L, result);
        verify(logsLoginRepository, times(1)).countByUserId(1L);
    }

    @Test
    void testCountByUserId_ZeroResult() {
        // Given
        when(logsLoginRepository.countByUserId(999L)).thenReturn(0L);

        // When
        long result = logsLoginRepository.countByUserId(999L);

        // Then
        assertEquals(0L, result);
        verify(logsLoginRepository, times(1)).countByUserId(999L);
    }

    @Test
    void testCountByType() {
        // Given
        when(logsLoginRepository.countByType("LOGIN_SUCCESS")).thenReturn(2L);

        // When
        long result = logsLoginRepository.countByType("LOGIN_SUCCESS");

        // Then
        assertEquals(2L, result);
        verify(logsLoginRepository, times(1)).countByType("LOGIN_SUCCESS");
    }

    @Test
    void testCountByType_ZeroResult() {
        // Given
        when(logsLoginRepository.countByType("UNKNOWN_TYPE")).thenReturn(0L);

        // When
        long result = logsLoginRepository.countByType("UNKNOWN_TYPE");

        // Then
        assertEquals(0L, result);
        verify(logsLoginRepository, times(1)).countByType("UNKNOWN_TYPE");
    }

    @Test
    void testFindLastLoginByUser() {
        // Given
        Pageable pageable = PageRequest.of(0, 1);
        List<LogsLogin> lastLogin = Arrays.asList(loginLog2); // Most recent for user 1
        when(logsLoginRepository.findLastLoginByUser(1L, pageable)).thenReturn(lastLogin);

        // When
        List<LogsLogin> result = logsLoginRepository.findLastLoginByUser(1L, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
        verify(logsLoginRepository, times(1)).findLastLoginByUser(1L, pageable);
    }

    @Test
    void testFindLastLoginByUser_EmptyResult() {
        // Given
        Pageable pageable = PageRequest.of(0, 1);
        when(logsLoginRepository.findLastLoginByUser(999L, pageable)).thenReturn(Arrays.asList());

        // When
        List<LogsLogin> result = logsLoginRepository.findLastLoginByUser(999L, pageable);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(logsLoginRepository, times(1)).findLastLoginByUser(999L, pageable);
    }

    @Test
    void testSave() {
        // Given
        LogsLogin newLog = new LogsLogin();
        newLog.setUserId(3L);
        newLog.setType("PASSWORD_RESET");

        LogsLogin savedLog = new LogsLogin();
        savedLog.setId(4L);
        savedLog.setUserId(3L);
        savedLog.setType("PASSWORD_RESET");
        savedLog.setDateCreated(LocalDateTime.now());
        savedLog.setDateUpdated(LocalDateTime.now());

        when(logsLoginRepository.save(any(LogsLogin.class))).thenReturn(savedLog);

        // When
        LogsLogin result = logsLoginRepository.save(newLog);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(3L, result.getUserId());
        assertEquals("PASSWORD_RESET", result.getType());
        assertNotNull(result.getDateCreated());
        assertNotNull(result.getDateUpdated());
        verify(logsLoginRepository, times(1)).save(any(LogsLogin.class));
    }

    @Test
    void testFindById() {
        // Given
        when(logsLoginRepository.findById(1L)).thenReturn(Optional.of(loginLog1));

        // When
        Optional<LogsLogin> result = logsLoginRepository.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals(1L, result.get().getUserId());
        assertEquals("LOGIN_SUCCESS", result.get().getType());
        verify(logsLoginRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(logsLoginRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<LogsLogin> result = logsLoginRepository.findById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(logsLoginRepository, times(1)).findById(999L);
    }

    @Test
    void testDeleteById() {
        // Given
        doNothing().when(logsLoginRepository).deleteById(1L);

        // When
        logsLoginRepository.deleteById(1L);

        // Then
        verify(logsLoginRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAll() {
        // Given
        List<LogsLogin> allLogs = Arrays.asList(loginLog1, loginLog2, loginLog3);
        when(logsLoginRepository.findAll()).thenReturn(allLogs);

        // When
        List<LogsLogin> result = logsLoginRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(logsLoginRepository, times(1)).findAll();
    }

    @Test
    void testDelete() {
        // Given
        doNothing().when(logsLoginRepository).delete(loginLog1);

        // When
        logsLoginRepository.delete(loginLog1);

        // Then
        verify(logsLoginRepository, times(1)).delete(loginLog1);
    }
}
