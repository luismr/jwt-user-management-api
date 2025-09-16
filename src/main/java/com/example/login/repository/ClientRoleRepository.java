package com.example.login.repository;

import com.example.login.entity.ClientRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "clientRoles", path = "client-roles")
@Tag(name = "Client Roles", description = "Client-Role relationship management operations")
public interface ClientRoleRepository extends JpaRepository<ClientRole, ClientRole.ClientRoleId> {

    // Find client roles by client ID
    @RestResource(path = "by-client", rel = "by-client")
    @Operation(summary = "Find client roles by client ID", description = "Retrieves all roles associated with a specific client")
    List<ClientRole> findByIdClient(@Parameter(description = "ID of the client") @Param("idClient") Long idClient);

    // Find client roles by role ID
    @RestResource(path = "by-role", rel = "by-role")
    @Operation(summary = "Find client roles by role ID", description = "Retrieves all clients associated with a specific role")
    List<ClientRole> findByIdRole(@Parameter(description = "ID of the role") @Param("idRole") Long idRole);

    // Count roles for a client
    @Query("SELECT COUNT(cr) FROM ClientRole cr WHERE cr.idClient = :idClient")
    @Operation(summary = "Count roles for a client", description = "Returns the number of roles associated with a specific client")
    long countRolesByClient(@Parameter(description = "ID of the client") @Param("idClient") Long idClient);

    // Count clients for a role
    @Query("SELECT COUNT(cr) FROM ClientRole cr WHERE cr.idRole = :idRole")
    @Operation(summary = "Count clients for a role", description = "Returns the number of clients associated with a specific role")
    long countClientsByRole(@Parameter(description = "ID of the role") @Param("idRole") Long idRole);

    // Check if client has specific role
    @Operation(summary = "Check if client has specific role", description = "Verifies if a specific client has a specific role")
    boolean existsByIdClientAndIdRole(@Parameter(description = "ID of the client") Long idClient, @Parameter(description = "ID of the role") Long idRole);
}
