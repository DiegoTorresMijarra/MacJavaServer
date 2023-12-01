package com.example.macjava.rest.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpRequest {
    @Schema(description = "Nombre", example = "John")
    @NotBlank(message = "Nombre no puede estar vacío")
    private String nombre;
    @Schema(description = "Apellidos", example = "Doe")
    @NotBlank(message = "Apellidos no puede estar vacío")
    private String apellidos;
    @Schema(description = "Username", example = "johndoe")
    @NotBlank(message = "Username no puede estar vacío")
    private String username;
    @Schema(description = "Email", example = "johndoe@localhost")
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @NotBlank(message = "Email no puede estar vacío")
    private String email;
    @Schema(description = "Password", example = "johndoe33")
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    private String password;
    @Schema(description = "Password de comprobación", example = "johndoe33")
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password de comprobación debe tener al menos 5 caracteres")
    private String passwordComprobacion;

}
