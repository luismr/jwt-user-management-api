package com.example.login.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "Client entity representing organizations or companies in the system")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned")
    @EqualsAndHashCode.Include
    @Schema(description = "Unique identifier for the client", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "external_id", length = 64, unique = true)
    @Schema(description = "External identifier for integration with other systems", example = "SEARS-HOME-SERVICES", maxLength = 64)
    private String externalId;

    @Column(name = "name", length = 128, nullable = false, unique = true)
    @Schema(description = "Unique name of the client organization", example = "Acme Corporation", maxLength = 128)
    private String name;

    @CreationTimestamp
    @Column(name = "date_created", nullable = false, updatable = false)
    @Schema(description = "Timestamp when the client was created", example = "2023-10-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    @Column(name = "date_updated", nullable = false)
    @Schema(description = "Timestamp when the client was last updated", example = "2023-10-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dateUpdated;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "client", "loginLogs", "userRoles"})
    private List<User> users;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "client", "role", "userRoles"})
    private List<ClientRole> clientRoles;

    // Custom constructors for convenience
    public Client(String name) {
        this.name = name;
    }

    public Client(String externalId, String name) {
        this.externalId = externalId;
        this.name = name;
    }
}