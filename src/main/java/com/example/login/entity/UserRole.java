package com.example.login.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_roles")
@IdClass(UserRole.UserRoleId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserRole {

    @Id
    @Column(name = "id_user", columnDefinition = "bigint unsigned")
    @EqualsAndHashCode.Include
    private Long idUser;

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
    @JoinColumn(name = "id_user", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "id_client", referencedColumnName = "id_client", insertable = false, updatable = false),
        @JoinColumn(name = "id_role", referencedColumnName = "id_role", insertable = false, updatable = false)
    })
    private ClientRole clientRole;

    // Composite key class with Lombok
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UserRoleId implements Serializable {
        private Long idUser;
        private Long idClient;
        private Long idRole;
    }

    // Custom constructor for convenience
    public UserRole(Long idUser, Long idClient, Long idRole) {
        this.idUser = idUser;
        this.idClient = idClient;
        this.idRole = idRole;
    }
}