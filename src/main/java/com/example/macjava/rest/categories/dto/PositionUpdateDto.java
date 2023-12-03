package com.example.macjava.rest.categories.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionUpdateDto {
    @Schema(description = "Nombre de la categoria", example = "Gerente")
    @Column(nullable = false,columnDefinition = "VARCHAR (13) CONSTRAINT CHECK_NAME CHECK name IN ('MANAGER','COOKER','CLEANER','WAITER','NOT_ASSIGNED')") //podria ser unique
    private String name;
    @NotNull
    @DecimalMin(value = "1000.00", message = "El salario no puede ser menor de 1000.00")
    @Schema(description = "Salario de la categoria", example = "1000.00")
    private Double salary;
}
