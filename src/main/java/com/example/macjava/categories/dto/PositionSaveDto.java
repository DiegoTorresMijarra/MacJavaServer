package com.example.macjava.categories.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PositionSaveDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotNull
    @DecimalMin(value = "1000.00", message = "El salario no puede ser menor de 1000.00")
    private Double salary;
}