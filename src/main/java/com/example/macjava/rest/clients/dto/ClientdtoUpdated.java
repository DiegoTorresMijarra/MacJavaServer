package com.example.macjava.rest.clients.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para la actualización del cliente
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientdtoUpdated {
    @Schema(description = "DNI del cliente", example = "12345678A")
    @NotBlank(message = "El DNI no puede estar vacío")
    @Pattern(regexp = "\\d{8}[A-HJ-NP-TV-Z]", message = "El DNI debe tener el formato válido")
    String dni;
    @Schema(description = "Nombre del cliente", example = "Juan")
    @NotBlank(message = "El nombre no puede estar vacío")
    String name;
    @Schema(description = "Apellido del cliente", example = "Peña")
    @NotBlank(message = "El apellido no puede estar vacío")
    String last_name;
    @Schema(description = "Edad del cliente", example = "25")
    @NotNull(message = "La edad no puede estar vacía")
    @Positive(message = "La edad no puede ser negativa")
    int age;
    @Schema(description = "Telefono del cliente", example = "123456789")
    @NotBlank(message = "El telefono no puede estar vacío")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos")
    String phone;
    @Schema(description = "Imagen del cliente", example = "https://example.com/image.jpg")
    @NotBlank(message = "La imagen no puede estar vacía")
    String image;
    @Schema(description = "Indica si el cliente ha sido eliminado", example = "false")
    @Builder.Default
    boolean deleted = false;
}
