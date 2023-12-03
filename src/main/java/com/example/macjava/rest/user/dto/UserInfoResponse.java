package com.example.macjava.rest.user.dto;

import com.example.macjava.rest.user.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Objeto de transferencia de datos para la respuesta del usuario
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    @Schema(description = "ID del usuario", example = "1")
    private UUID id;
    @Schema(description = "Nombre del usuario", example = "John")
    private String nombre;
    @Schema(description = "Apellidos del usuario", example = "Doe")
    private String apellidos;
    @Schema(description = "Username del usuario", example = "johndoe")
    private String username;
    @Schema(description = "Email del usuario", example = "johndoe@me.com")
    private String email;
    @Schema(description = "Roles del usuario", example = "USER")
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
    @Schema(description = "Usuario eliminado", example = "false")
    @Builder.Default
    private Boolean isDeleted = false;
    @Schema(description = "Pedidos del usuario")
    @Builder.Default
    private List<String> pedidos = new ArrayList<>();
}
