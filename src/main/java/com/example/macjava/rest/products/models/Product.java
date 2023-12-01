package com.example.macjava.rest.products.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @Column(nullable = false)
    @Positive(message = "El precio no puede ser negativo")
    @NotNull(message = "El precio no puede estar vacío")
    private Double precio;
    @Column(nullable = false)
    @PositiveOrZero(message = "El stock no puede ser negativo")
    @NotNull(message = "El stock no puede estar vacío")
    private Integer stock;
    @Column()
    @Builder.Default
    private boolean gluten = true;
    @Column()
    @Builder.Default
    private boolean is_deleted = false;
    @Column(nullable = false)
    LocalDate fecha_cre;
    @Column(nullable = false)
    LocalDate fecha_act;
}
