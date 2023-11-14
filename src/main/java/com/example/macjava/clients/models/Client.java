package com.example.macjava.clients.models;


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
    private UUID id;
    @Column(nullable = false,unique = true)
    @NotBlank(message = "El DNI no puede estar vacío")
    @Pattern(regexp = "\\d{8}[A-HJ-NP-TV-Z]", message = "El DNI debe tener el formato válido")
    String dni;
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    String name;
    @Column(nullable = false)
    @NotBlank(message = "El apellido no puede estar vacío")
    String last_name;
    @Column(nullable = false)
    @NotNull(message = "La edad no puede estar vacía")
    @Positive(message = "La edad no puede ser negativa")
    int age;
    @Column(nullable = false)
    @NotBlank(message = "El telefono no puede estar vacío")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos")
    String phone;
    @Column(nullable = false)
    @NotBlank(message = "La imagen no puede estar vacía")
    String image;
    @Column()
    @Builder.Default
    boolean deleted= false;
    @Column(nullable = false)
    LocalDate fecha_cre;
    @Column(nullable = false)
    LocalDate fecha_act;
}
