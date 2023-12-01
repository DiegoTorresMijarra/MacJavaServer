package com.example.macjava.rest.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USUARIOS")
@EntityListeners(AuditingEntityListener.class) // Para que sea auditada y se autorellene
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificar del usuario", example = "1")
    private Long id;
    @Schema(description = "Nombre del usuario", example = "Jaime")
    @NotBlank(message = "nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;
    @Schema(description = "Apellidos del usuario", example = "Lopez")
    @Column(nullable = false)
    @NotBlank(message = "apellidos no puede estar vacío")
    private String apellidos;
    @Schema(description = "Username del usuario", example = "jlopez")
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username no puede estar vacío")
    private String username;
    @Schema(description = "Email del usuario", example = "jlopez@")
    @Column(unique = true, nullable = false)
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @NotBlank(message = "Email no puede estar vacío")
    private String email;
    @Schema(description = "Password del usuario", example = "jlopez")
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    @Column(nullable = false)
    private String password;
    @Schema(description = "Usuario creado", example = "2022-12-01T00:00:00")
    @CreationTimestamp
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Schema(description = "Usuario actualizado", example = "2022-12-01T00:00:00")
    @UpdateTimestamp
    @Column(updatable = true, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Schema(description = "Usuario eliminado", example = "false")
    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted = false;
    @Schema(description = "Roles del usuario", example = "ADMIN")
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        // email in our case
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }
}
