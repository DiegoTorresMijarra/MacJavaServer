package com.example.macjava.rest.categories.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionResponseDto {
    @Schema(description = "Identificador de la categoria", example = "1")
    private Long id;
    @Schema(description = "Nombre de la categoria", example = "Gerente")
    private String name;
    @Schema(description = "Salario de la categoria", example = "1000.0")
    private Double salary;
    @Schema(description = "Estado de la categoria", example = "true")
    private Boolean isDeleted;
    @Schema(description = "Fecha de creacion de la categoria", example = "2022-01-01T00:00:00")
    private LocalDateTime createdAt;
    @Schema(description = "Fecha de actualizacion de la categoria", example = "2022-01-01T00:00:00")
    private LocalDateTime updatedAt;
}
