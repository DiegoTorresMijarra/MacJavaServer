package com.example.macjava.rest.workers.dto;

import com.example.macjava.rest.categories.models.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkersResponseDto {
    @Schema(description = "UUID del trabajador", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID uuid;
    @Schema(description = "DNI del trabajador", example = "12345678")
    private String dni;
    @Schema(description = "Nombre del trabajador", example = "John")
    private String name;
    @Schema(description = "Apellidos del trabajador", example = "Doe")
    private String surname;
    @Schema(description = "Edad del trabajador", example = "30")
    private int age;
    @Schema(description = "Teléfono del trabajador", example = "123456789")
    private String phone;
    @Schema(description = "Trabajador eliminado", example = "false")
    private Boolean isDeleted;
    @Schema(description = "Fecha de creación del trabajador", example = "2022-01-01T00:00:00")
    private LocalDateTime createdAt;
    @Schema(description = "Fecha de actualización del trabajador", example = "2022-01-01T00:00:00")
    private LocalDateTime updatedAt;
    @Schema(description = "Posición del trabajador", example = "1")
    private Position position;
}
