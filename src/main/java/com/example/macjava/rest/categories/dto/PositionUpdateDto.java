package com.example.macjava.rest.categories.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Objeto de transferencia de datos para la actualización de la categoria
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionUpdateDto {
    @Schema(description = "Nombre de la categoria", example = "Gerente")
    private String name;
    @Schema(description = "Estado de la categoria", example = "true")
    private Boolean isDeleted = false;
    @Schema(description = "Salario de la categoria", example = "1000.00")
    private Double salary;
    //private LocalDateTime createdAt;
}
