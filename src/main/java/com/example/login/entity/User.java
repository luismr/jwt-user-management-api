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
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"passwordHash", "passwordSalt"}) // Exclude sensitive data from toString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "User entity representing system users with authentication and profile information")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned")
    @EqualsAndHashCode.Include
    @Schema(description = "Unique identifier for the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "id_client", columnDefinition = "bigint unsigned")
    @Schema(description = "ID of the client this user belongs to", example = "1")
    private Long idClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "users", "clientRoles"})
    private Client client;

    @Column(name = "username", length = 128, nullable = false, unique = true)
    @Schema(description = "Unique username for authentication", example = "admin", maxLength = 128)
    private String username;

    @Column(name = "password_hash", length = 255, nullable = false)
    @Schema(description = "Hashed password for authentication", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String passwordHash;

    @Column(name = "password_salt", length = 255, nullable = false)
    @Schema(description = "Salt used for password hashing", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String passwordSalt;

    @Enumerated(EnumType.STRING)
    @Column(name = "password_type", nullable = false, columnDefinition = "enum('BCRYPT','SHA256','MD5','SHA512') default 'MD5'")
    @Builder.Default
    @Schema(description = "Type of password hashing algorithm used", example = "BCRYPT", allowableValues = {"BCRYPT", "SHA256", "MD5", "SHA512"})
    private PasswordType passwordType = PasswordType.MD5;

    @Column(name = "name", length = 128, nullable = false)
    @Schema(description = "Full name of the user", example = "John Doe", maxLength = 128)
    private String name;

    @Column(name = "email", length = 128, nullable = false, unique = true)
    @Schema(description = "Unique email address of the user", example = "john.doe@example.com", maxLength = 128, format = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "enum('ACTIVE','INACTIVE','SUSPENDED') default 'ACTIVE'")
    @Builder.Default
    @Schema(description = "Current status of the user account", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED"})
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "date_last_login")
    @Schema(description = "Timestamp of the user's last login", example = "2023-10-15T10:30:00")
    private LocalDateTime dateLastLogin;

    @CreationTimestamp
    @Column(name = "date_created", nullable = false, updatable = false)
    @Schema(description = "Timestamp when the user was created", example = "2023-10-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    @Column(name = "date_updated", nullable = false)
    @Schema(description = "Timestamp when the user was last updated", example = "2023-10-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dateUpdated;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user"})
    private List<LogsLogin> loginLogs;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user", "clientRole"})
    private List<UserRole> userRoles;

    // Enums
    public enum PasswordType {
        BCRYPT, SHA256, MD5, SHA512;
        
        /**
         * Get PasswordType from string (case-insensitive)
         * @param typeString the string representation of the password type
         * @return the corresponding PasswordType
         * @throws IllegalArgumentException if the string doesn't match any known type
         */
        public static PasswordType fromString(String typeString) {
            if (typeString == null) {
                throw new IllegalArgumentException("Password type cannot be null");
            }
            try {
                return PasswordType.valueOf(typeString.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown password type: " + typeString + 
                    ". Supported types: " + java.util.Arrays.toString(PasswordType.values()));
            }
        }
    }

    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }

    // Custom constructor for convenience
    public User(String username, String passwordHash, String passwordSalt, String name, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.name = name;
        this.email = email;
        this.passwordType = PasswordType.MD5; // Set default value
        this.status = UserStatus.ACTIVE; // Set default value
    }
}