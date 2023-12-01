package com.example.macjava.rest.clients.models;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CLIENTS")
public class Client {
    @Id
    @Schema(description = "Identificador del cliente", example = "d5d5d5d5-d5d5-d5d5-d5d5-d5d5d5d5d5d5")
    private UUID id;
    @Schema(description = "DNI del cliente", example = "12345678G")
    @Column(nullable = false, unique = true)//puede ser unique
    @NotBlank(message = "El DNI no puede estar vacío")
    @Pattern(regexp = "\\d{8}[A-HJ-NP-TV-Z]", message = "El DNI debe tener el formato válido")
    String dni;
    @Schema(description = "Nombre del cliente", example = "Pepe")
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    String name;
    @Schema(description = "Apellido del cliente", example = "Perez")
    @Column(nullable = false)
    @NotBlank(message = "El apellido no puede estar vacío")
    String last_name;
    @Schema(description = "Edad del cliente", example = "30")
    @Column(nullable = false)
    @NotNull(message = "La edad no puede estar vacía")
    @Positive(message = "La edad no puede ser negativa")
    int age;
    @Schema(description = "Telefono del cliente", example = "123456789")
    @Column(nullable = false)
    @NotBlank(message = "El telefono no puede estar vacío")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos")
    String phone;
    @Schema(description = "Imagen del cliente", example = "https://example.com/image.jpg")
    @Column(nullable = false)
    @NotBlank(message = "La imagen no puede estar vacía")
    String image;
    @Schema(description = "Eliminado del cliente", example = "false")
    @Column()
    @Builder.Default
    boolean deleted= false;
    @Schema(description = "Fecha de creación del cliente", example = "2022-01-01")
    @Column(nullable = false)
    LocalDate fecha_cre;
    @Schema(description = "Fecha de actualización del cliente", example = "2022-01-01")
    @Column(nullable = false)
    LocalDate fecha_act;
}
