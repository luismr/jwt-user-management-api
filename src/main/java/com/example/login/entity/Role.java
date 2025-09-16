package com.example.login.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "description", length = 191, nullable = false, unique = true)
    private String description;

    @Column(name = "internal", nullable = false, columnDefinition = "tinyint(1) default 0")
    @Builder.Default
    private Boolean internal = false;

    @CreationTimestamp
    @Column(name = "date_created", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    @Column(name = "date_updated", nullable = false)
    private LocalDateTime dateUpdated;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ClientRole> clientRoles;

    // Custom constructors for convenience
    public Role(String description) {
        this.description = description;
        this.internal = false;
    }

    public Role(String description, Boolean internal) {
        this.description = description;
        this.internal = internal;
    }
}