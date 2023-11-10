package com.example.macjava.clients.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CLIENTS")
public class Client {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false,unique = true)
    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(min = 9, max = 9, message = "El DNI debe tener 9 caracteres")
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
    @NotNull(message = "El telefono no puede estar vacío")
    @Positive(message = "El telefono no puede ser negativo")
    @Size(min = 9, max = 9, message = "El telefono debe tener 9 dígitos")
    int phone;
    @Column(columnDefinition = "boolean default false")
    boolean deleted= false;
}
