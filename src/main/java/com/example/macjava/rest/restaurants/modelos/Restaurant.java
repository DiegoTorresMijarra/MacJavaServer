package com.example.macjava.rest.restaurants.modelos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="RESTAURANTS")
/**
 * Clase que representa el modelo de datos de un restaurante
 * @param id Identificador del restaurante
 * @param name Nombre del restaurante
 * @param number Numero de telefono del restaurante
 * @param isDeleted Boolean que indica si el restaurante ha sido eliminado
 * @param creationD Fecha de creación del restaurante
 * @param modificationD Fecha de modificación del restaurante
 */
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del restaurante",example = "1")
    private Long id;
    @Schema(description = "Nombre del restaurante",example = "Mcdonalds")
    @Column
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;
    @Schema(description = "Telefono del restaurante",example = "123456789")
    @Column
    @NotNull(message = "El numero no puede estar en blanco")
    @Pattern(regexp="\\d{9}", message = "Debe tener 9 dígitos")
    private String number;
    @Schema(description = "Restaurante eliminado",example = "false")
    @Column()
    @Builder.Default
    private boolean isDeleted=false;
    @Column
    @Schema(description = "Fecha de creacion del restaurante",example = "2022-01-01")
    private LocalDate creationD;
    @Column
    @Schema(description = "Fecha de modificacion del restaurante",example = "2022-01-01")
    private LocalDate modificationD;


}
