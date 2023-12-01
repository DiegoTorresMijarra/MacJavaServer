package com.example.macjava.rest.restaurants.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedRestaurantDTO {
    @Schema(description = "Nombre del restaurante", example = "Restaurante")
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;
    @Schema(description = "Telefono del restaurante", example = "123456789")
    @NotNull(message = "El número no puede estar en blanco")
    @Pattern(regexp="\\d{9}", message = "Debe tener 9 dígitos")
    private String number;
    @Schema(description = "Restaurante borrado", example = "false")
    @Builder.Default
    boolean isDeleted = false;
}
