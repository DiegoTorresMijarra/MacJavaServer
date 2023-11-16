package com.example.macjava.workers.dto;

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
public class WorkersSaveDto {
    @NotBlank(message ="DNI cannot be blank")
    @Pattern(regexp = "\\d{8}[A-HJ-NP-TV-Z]", message = "El DNI debe tener el formato válido")
    private String dni;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String surname;

    @NotNull(message = "La edad no puede estar vacía")
    @Positive(message = "La edad no puede ser negativa")
    int age;

    @Column(nullable = false)
    @NotBlank(message = "El telefono no puede estar vacío")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos")
    private String phone;

    @NotNull(message="El Cargo no puede estar vacío")
    @Positive(message = "El cargo no puede ser negativo o 0")
    private Long positionId;
}
