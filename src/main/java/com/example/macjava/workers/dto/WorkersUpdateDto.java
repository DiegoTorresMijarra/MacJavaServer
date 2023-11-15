package com.example.macjava.workers.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkersUpdateDto {
    @Pattern(regexp = "\\d{8}[A-HJ-NP-TV-Z]", message = "El DNI debe tener el formato válido")
    private String dni;
    private String name;
    private String surname;
    @Positive(message = "La edad no puede ser negativa")
    private int age;
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos")
    private String phone;
    private Boolean isDeleted;
}
