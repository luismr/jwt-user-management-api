package com.example.login.repository;

import com.example.login.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "roles", path = "roles")
@Tag(name = "Roles", description = "Role management operations")
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Find role by description
    @RestResource(path = "by-description", rel = "by-description")
    @Operation(summary = "Find role by description", description = "Retrieves a role by its exact description")
    Optional<Role> findByDescription(@Parameter(description = "Description of the role") @Param("description") String description);

    // Find roles by internal flag
    @RestResource(path = "by-internal", rel = "by-internal")
    @Operation(summary = "Find roles by internal flag", description = "Retrieves roles filtered by internal/external status")
    List<Role> findByInternal(@Parameter(description = "Whether to find internal (true) or external (false) roles") @Param("internal") Boolean internal);

    // Find roles by description containing (case insensitive)
    @RestResource(path = "by-description-containing", rel = "by-description-containing")
    @Operation(summary = "Find roles by description containing", description = "Retrieves roles whose descriptions contain the specified text (case insensitive)")
    List<Role> findByDescriptionContainingIgnoreCase(@Parameter(description = "Text to search for in role descriptions") @Param("description") String description);

    // Find external roles (not internal)
    @RestResource(path = "external", rel = "external")
    @Operation(summary = "Find external roles", description = "Retrieves all external roles (not internal) ordered by description")
    List<Role> findByInternalFalseOrderByDescriptionAsc();

    // Find internal roles
    @RestResource(path = "internal", rel = "internal")
    @Operation(summary = "Find internal roles", description = "Retrieves all internal roles ordered by description")
    List<Role> findByInternalTrueOrderByDescriptionAsc();

    // Check if description exists
    @Operation(summary = "Check if description exists", description = "Verifies if a role with the specified description exists")
    boolean existsByDescription(@Parameter(description = "Description to check") String description);
}
