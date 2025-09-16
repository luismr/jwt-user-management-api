package com.example.login.repository;

import com.example.login.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "userRoles", path = "user-roles")
@Tag(name = "User Roles", description = "User-Role relationship management operations")
public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {

    // Find user roles by user ID
    @RestResource(path = "by-user", rel = "by-user")
    @Operation(summary = "Find user roles by user ID", description = "Retrieves all roles associated with a specific user")
    List<UserRole> findByIdUser(@Parameter(description = "ID of the user") @Param("idUser") Long idUser);

    // Find user roles by client ID
    @RestResource(path = "by-client", rel = "by-client")
    @Operation(summary = "Find user roles by client ID", description = "Retrieves all user-role relationships for a specific client")
    List<UserRole> findByIdClient(@Parameter(description = "ID of the client") @Param("idClient") Long idClient);

    // Find user roles by role ID
    @RestResource(path = "by-role", rel = "by-role")
    @Operation(summary = "Find user roles by role ID", description = "Retrieves all users associated with a specific role")
    List<UserRole> findByIdRole(@Parameter(description = "ID of the role") @Param("idRole") Long idRole);

    // Find user roles by user and client
    @RestResource(path = "by-user-and-client", rel = "by-user-and-client")
    @Operation(summary = "Find user roles by user and client", description = "Retrieves all roles for a specific user within a specific client")
    List<UserRole> findByIdUserAndIdClient(@Parameter(description = "ID of the user") @Param("idUser") Long idUser, @Parameter(description = "ID of the client") @Param("idClient") Long idClient);

    // Custom query to find users with specific role in a specific client
    @Query("SELECT ur FROM UserRole ur WHERE ur.idRole = :idRole AND ur.idClient = :idClient")
    @RestResource(path = "by-role-and-client", rel = "by-role-and-client")
    @Operation(summary = "Find users by role and client", description = "Retrieves all users with a specific role within a specific client")
    List<UserRole> findByRoleAndClient(@Parameter(description = "ID of the role") @Param("idRole") Long idRole, @Parameter(description = "ID of the client") @Param("idClient") Long idClient);

    // Count roles for a user in a specific client
    @Query("SELECT COUNT(ur) FROM UserRole ur WHERE ur.idUser = :idUser AND ur.idClient = :idClient")
    @Operation(summary = "Count roles for user in client", description = "Returns the number of roles for a specific user within a specific client")
    long countRolesByUserAndClient(@Parameter(description = "ID of the user") @Param("idUser") Long idUser, @Parameter(description = "ID of the client") @Param("idClient") Long idClient);

    // Check if user has specific role in specific client
    @Operation(summary = "Check if user has specific role in client", description = "Verifies if a specific user has a specific role within a specific client")
    boolean existsByIdUserAndIdClientAndIdRole(@Parameter(description = "ID of the user") Long idUser, @Parameter(description = "ID of the client") Long idClient, @Parameter(description = "ID of the role") Long idRole);
}
