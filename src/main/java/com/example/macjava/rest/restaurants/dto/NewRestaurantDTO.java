package com.example.macjava.rest.restaurants.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para la creación del restaurante
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewRestaurantDTO {
    @Schema(description = "Nombre del restaurante", example = "McDonalds")
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;
    @Schema(description = "Telefono del restaurante", example = "123456789")
    @NotNull(message = "El número no puede estar en blanco")
    @Pattern(regexp="\\d{9}", message = "Debe tener 9 dígitos")
    private String number;
}
