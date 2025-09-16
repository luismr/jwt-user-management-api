package com.example.login.repository;

import com.example.login.entity.LogsLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "logsLogins", path = "logs-logins")
@Tag(name = "Login Logs", description = "Login logging and audit operations")
public interface LogsLoginRepository extends JpaRepository<LogsLogin, Long> {

    // Find login logs by user ID
    @RestResource(path = "by-user", rel = "by-user")
    @Operation(summary = "Find login logs by user ID", description = "Retrieves paginated login logs for a specific user, ordered by creation date descending")
    Page<LogsLogin> findByUserIdOrderByDateCreatedDesc(@Parameter(description = "ID of the user") @Param("userId") Long userId, Pageable pageable);

    // Find login logs by type
    @RestResource(path = "by-type", rel = "by-type")
    @Operation(summary = "Find login logs by type", description = "Retrieves paginated login logs of a specific type, ordered by creation date descending")
    Page<LogsLogin> findByTypeOrderByDateCreatedDesc(@Parameter(description = "Type of login log") @Param("type") String type, Pageable pageable);

    // Find login logs by user and type
    @RestResource(path = "by-user-and-type", rel = "by-user-and-type")
    @Operation(summary = "Find login logs by user and type", description = "Retrieves login logs for a specific user and type, ordered by creation date descending")
    List<LogsLogin> findByUserIdAndTypeOrderByDateCreatedDesc(@Parameter(description = "ID of the user") @Param("userId") Long userId, @Parameter(description = "Type of login log") @Param("type") String type);

    // Find recent login logs (last 24 hours)
    @Query("SELECT ll FROM LogsLogin ll WHERE ll.dateCreated >= :since ORDER BY ll.dateCreated DESC")
    @RestResource(path = "recent", rel = "recent")
    @Operation(summary = "Find recent login logs", description = "Retrieves paginated login logs created after a specific date, ordered by creation date descending")
    Page<LogsLogin> findRecentLogs(@Parameter(description = "Start date for filtering logs") @Param("since") LocalDateTime since, Pageable pageable);

    // Find login logs by date range
    @Query("SELECT ll FROM LogsLogin ll WHERE ll.dateCreated BETWEEN :startDate AND :endDate ORDER BY ll.dateCreated DESC")
    @RestResource(path = "by-date-range", rel = "by-date-range")
    @Operation(summary = "Find login logs by date range", description = "Retrieves paginated login logs within a specific date range, ordered by creation date descending")
    Page<LogsLogin> findByDateRange(@Parameter(description = "Start date of the range") @Param("startDate") LocalDateTime startDate, @Parameter(description = "End date of the range") @Param("endDate") LocalDateTime endDate, Pageable pageable);

    // Count login attempts by user
    @Query("SELECT COUNT(ll) FROM LogsLogin ll WHERE ll.userId = :userId")
    @Operation(summary = "Count login attempts by user", description = "Returns the total number of login attempts for a specific user")
    long countByUserId(@Parameter(description = "ID of the user") @Param("userId") Long userId);

    // Count login attempts by type
    @Query("SELECT COUNT(ll) FROM LogsLogin ll WHERE ll.type = :type")
    @Operation(summary = "Count login attempts by type", description = "Returns the total number of login attempts of a specific type")
    long countByType(@Parameter(description = "Type of login log") @Param("type") String type);

    // Find last login for user
    @Query("SELECT ll FROM LogsLogin ll WHERE ll.userId = :userId ORDER BY ll.dateCreated DESC")
    @Operation(summary = "Find last login for user", description = "Retrieves the most recent login logs for a specific user")
    List<LogsLogin> findLastLoginByUser(@Parameter(description = "ID of the user") @Param("userId") Long userId, Pageable pageable);

    // Find logs with user information using relationship
    @Query("SELECT ll FROM LogsLogin ll JOIN FETCH ll.user u WHERE u.username = :username")
    @RestResource(path = "by-username", rel = "by-username")
    @Operation(summary = "Find login logs by username", description = "Retrieves login logs for a user by their username")
    List<LogsLogin> findByUserUsername(@Parameter(description = "Username of the user") @Param("username") String username);

    // Find logs by client using relationship
    @Query("SELECT ll FROM LogsLogin ll JOIN ll.user u JOIN u.client c WHERE c.externalId = :clientExternalId")
    @RestResource(path = "by-client", rel = "by-client")
    @Operation(summary = "Find login logs by client", description = "Retrieves login logs for all users belonging to a specific client")
    List<LogsLogin> findByClientExternalId(@Parameter(description = "External ID of the client") @Param("clientExternalId") String clientExternalId);

    // Find login stats by client
    @Query("SELECT c.name, COUNT(ll) FROM LogsLogin ll JOIN ll.user u JOIN u.client c GROUP BY c.name")
    @RestResource(path = "stats-by-client", rel = "stats-by-client")
    @Operation(summary = "Find login stats by client", description = "Retrieves login statistics grouped by client name")
    List<Object[]> findLoginStatsByClient();
}
