package com.example.macjava.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCTS")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @Column(nullable = false)
    @Positive(message = "El precio no puede ser negativo")
    @NotNull(message = "El precio no puede estar vacío")
    private double precio;
    @Column()
    @Builder.Default
    private boolean gluten = true;
    @Column()
    @Builder.Default
    private boolean is_deleted = false;
}
