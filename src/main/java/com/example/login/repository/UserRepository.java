package com.example.login.repository;

import com.example.login.entity.User;
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

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
@Tag(name = "Users", description = "User management operations")
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username
    @RestResource(path = "by-username", rel = "by-username")
    Optional<User> findByUsername(@Param("username") String username);

    // Find user by email
    @RestResource(path = "by-email", rel = "by-email")
    Optional<User> findByEmail(@Param("email") String email);

    // Find users by client ID
    @RestResource(path = "by-client", rel = "by-client")
    List<User> findByIdClient(@Param("idClient") Long idClient);

    // Find users by status
    @RestResource(path = "by-status", rel = "by-status")
    List<User> findByStatus(@Param("status") User.UserStatus status);

    // Find users by name containing (case insensitive)
    @RestResource(path = "by-name-containing", rel = "by-name-containing")
    List<User> findByNameContainingIgnoreCase(@Param("name") String name);

    // Custom query to find users by name or username pattern
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.name) LIKE LOWER(CONCAT('%', :pattern, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :pattern, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :pattern, '%'))")
    @RestResource(path = "search", rel = "search")
    Page<User> searchByPattern(@Param("pattern") String pattern, Pageable pageable);

    // Find active users
    @RestResource(path = "active", rel = "active")
    List<User> findByStatusOrderByNameAsc(@Param("status") User.UserStatus status);

    // Check if username exists
    boolean existsByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);

    // Count users by client
    @Query("SELECT COUNT(u) FROM User u WHERE u.idClient = :idClient")
    long countByIdClient(@Param("idClient") Long idClient);

    // Find users by client using relationship
    @RestResource(path = "by-client-entity", rel = "by-client-entity")
    List<User> findByClient_ExternalId(@Param("externalId") String externalId);

    // Find users with their client information (using JOIN)
    @Query("SELECT u FROM User u JOIN FETCH u.client c WHERE c.name LIKE %:clientName%")
    @RestResource(path = "by-client-name", rel = "by-client-name")
    List<User> findByClientNameContaining(@Param("clientName") String clientName);

    // Find users who have login logs
    @Query("SELECT DISTINCT u FROM User u JOIN u.loginLogs l")
    @RestResource(path = "with-login-logs", rel = "with-login-logs")
    List<User> findUsersWithLoginLogs();

    // Find users with specific role through UserRole relationship
    @Query("SELECT DISTINCT u FROM User u JOIN u.userRoles ur JOIN ur.clientRole cr JOIN cr.role r WHERE r.description = :roleDescription")
    @RestResource(path = "by-role", rel = "by-role")
    List<User> findUsersByRoleDescription(@Param("roleDescription") String roleDescription);
}
