package com.example.login.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "client_roles")
@IdClass(ClientRole.ClientRoleId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClientRole {

    @Id
    @Column(name = "id_client", columnDefinition = "bigint unsigned")
    @EqualsAndHashCode.Include
    private Long idClient;

    @Id
    @Column(name = "id_role", columnDefinition = "bigint unsigned")
    @EqualsAndHashCode.Include
    private Long idRole;

    @CreationTimestamp
    @Column(name = "date_created", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    @Column(name = "date_updated", nullable = false)
    private LocalDateTime dateUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", insertable = false, updatable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_role", insertable = false, updatable = false)
    private Role role;

    @OneToMany(mappedBy = "clientRole", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserRole> userRoles;

    // Composite key class with Lombok
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ClientRoleId implements Serializable {
        private Long idClient;
        private Long idRole;
    }

    // Custom constructor for convenience
    public ClientRole(Long idClient, Long idRole) {
        this.idClient = idClient;
        this.idRole = idRole;
    }
}