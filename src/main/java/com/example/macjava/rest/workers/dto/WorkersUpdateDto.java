package com.example.macjava.rest.workers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkersUpdateDto {
    @Schema(description = "DNI del trabajador", example = "12345678G")
    @NotBlank(message ="DNI cannot be blank")
    @Pattern(regexp = "\\d{8}[A-HJ-NP-TV-Z]", message = "El DNI debe tener el formato válido")
    private String dni;
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre del trabajador", example = "Pepe")
    private String name;
    @Schema(description = "Apellidos del trabajador", example = "Perez")
    @NotBlank(message = "El apellido no puede estar vacío")
    private String surname;
    @Schema(description = "Edad del trabajador", example = "25")
    @NotNull(message = "La edad no puede estar vacía")
    @Positive(message = "La edad no puede ser negativa")
    private int age;
    @Schema(description = "Telefono del trabajador", example = "123456789")
    @NotBlank(message = "El telefono no puede estar vacío")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos")
    private String phone;
    @Schema(description = "Posicion del trabajador", example = "1")
    @Builder.Default
    private Long positionId=-1L;
}
