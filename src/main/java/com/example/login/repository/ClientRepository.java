package com.example.login.repository;

import com.example.login.entity.Client;
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

@RepositoryRestResource(collectionResourceRel = "clients", path = "clients")
@Tag(name = "Clients", description = "Client management operations")
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Find client by external ID
    @RestResource(path = "by-external-id", rel = "by-external-id")
    @Operation(summary = "Find client by external ID", description = "Retrieves a client by its external identifier")
    Optional<Client> findByExternalId(@Parameter(description = "External ID of the client") @Param("externalId") String externalId);

    // Find client by name
    @RestResource(path = "by-name", rel = "by-name")
    @Operation(summary = "Find client by name", description = "Retrieves a client by its exact name")
    Optional<Client> findByName(@Parameter(description = "Name of the client") @Param("name") String name);

    // Find clients by name containing (case insensitive)
    @RestResource(path = "by-name-containing", rel = "by-name-containing")
    @Operation(summary = "Find clients by name containing", description = "Retrieves clients whose names contain the specified text (case insensitive)")
    List<Client> findByNameContainingIgnoreCase(@Parameter(description = "Text to search for in client names") @Param("name") String name);

    // Check if external ID exists
    @Operation(summary = "Check if external ID exists", description = "Verifies if a client with the specified external ID exists")
    boolean existsByExternalId(@Parameter(description = "External ID to check") String externalId);

    // Check if name exists
    @Operation(summary = "Check if name exists", description = "Verifies if a client with the specified name exists")
    boolean existsByName(@Parameter(description = "Name to check") String name);

    // Find clients with users (using relationship)
    @Query("SELECT DISTINCT c FROM Client c JOIN FETCH c.users")
    @RestResource(path = "with-users", rel = "with-users")
    @Operation(summary = "Find clients with users", description = "Retrieves all clients that have associated users")
    List<Client> findClientsWithUsers();

    // Find clients that have specific role
    @Query("SELECT DISTINCT c FROM Client c JOIN c.clientRoles cr JOIN cr.role r WHERE r.description = :roleDescription")
    @RestResource(path = "with-role", rel = "with-role")
    @Operation(summary = "Find clients by role description", description = "Retrieves clients that have a specific role")
    List<Client> findClientsByRoleDescription(@Parameter(description = "Role description to search for") @Param("roleDescription") String roleDescription);

    // Count users for each client
    @Query("SELECT c, SIZE(c.users) FROM Client c")
    @RestResource(path = "user-counts", rel = "user-counts")
    @Operation(summary = "Get user counts per client", description = "Retrieves a list of clients with their user counts")
    List<Object[]> findClientUserCounts();

    // Find clients with active users
    @Query("SELECT DISTINCT c FROM Client c JOIN c.users u WHERE u.status = 'ACTIVE'")
    @RestResource(path = "with-active-users", rel = "with-active-users")
    @Operation(summary = "Find clients with active users", description = "Retrieves clients that have at least one active user")
    List<Client> findClientsWithActiveUsers();
}
