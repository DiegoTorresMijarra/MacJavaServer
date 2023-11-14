package com.example.macjava.clients.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientdtoNew {
    @NotBlank(message = "El DNI no puede estar vacío")
    @Pattern(regexp = "\\d{8}[A-HJ-NP-TV-Z]", message = "El DNI debe tener el formato válido")
    String dni;
    @NotBlank(message = "El nombre no puede estar vacío")
    String name;
    @NotBlank(message = "El apellido no puede estar vacío")
    String last_name;
    @NotNull(message = "La edad no puede estar vacía")
    @Positive(message = "La edad no puede ser negativa")
    int age;
    @NotBlank(message = "El telefono no puede estar vacío")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos")
    String phone;
    @NotBlank(message = "La imagen no puede estar vacía")
    String image;
}
