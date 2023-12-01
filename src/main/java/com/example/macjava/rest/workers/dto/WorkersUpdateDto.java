package com.example.macjava.rest.workers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private String dni;
    @Schema(description = "Nombre del trabajador", example = "Pepe")
    private String name;
    @Schema(description = "Apellidos del trabajador", example = "Perez")
    private String surname;
    @Schema(description = "Edad del trabajador", example = "25")
    private int age;
    @Schema(description = "Telefono del trabajador", example = "123456789")
    private String phone;
    @Schema(description = "Trabajador eliminado", example = "false")
    private Boolean isDeleted;
    @Schema(description = "Posicion del trabajador", example = "1")
    @Builder.Default
    private Long positionId=-1L;
}
