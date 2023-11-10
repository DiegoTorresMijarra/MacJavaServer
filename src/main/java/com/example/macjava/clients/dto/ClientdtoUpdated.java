package com.example.macjava.clients.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientdtoUpdated {
    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(min = 9, max = 9, message = "El DNI debe tener 9 caracteres")
    @Pattern(regexp = "\\d{8}[A-HJ-NP-TV-Z]", message = "El DNI debe tener el formato válido")
    String dni;
    @NotBlank(message = "El nombre no puede estar vacío")
    String name;
    @NotBlank(message = "El apellido no puede estar vacío")
    String last_name;
    @NotNull(message = "La edad no puede estar vacía")
    @Positive(message = "La edad no puede ser negativa")
    int age;
    @NotNull(message = "El telefono no puede estar vacío")
    @Positive(message = "El telefono no puede ser negativo")
    @Size(min = 9, max = 9, message = "El telefono debe tener 9 dígitos")
    int phone;
    @Builder.Default
    boolean deleted = false;
}
