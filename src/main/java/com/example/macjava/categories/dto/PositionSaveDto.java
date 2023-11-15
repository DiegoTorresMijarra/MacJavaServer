package com.example.macjava.categories.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PositionSaveDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;
}
