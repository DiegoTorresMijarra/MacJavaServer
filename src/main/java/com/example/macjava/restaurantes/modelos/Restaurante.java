package com.example.macjava.restaurantes.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="RESTAURANTS")
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;
    @Column
    @NotNull(message = "El nombre no puede estar en blanco")
    @Pattern(regexp="\\d{9}", message = "Debe tener 9 dígitos")
    private String number;
    @Column()
    @Builder.Default
    private boolean isDeleted=false;
    @Column
    private LocalDate creationD;
    @Column
    private LocalDate modificationD;

    //@Column
    //@OneToMany
    //private Personal  worker;

    //@Column(unique=true)
    //@NotBlank(message="La direccion no debe estar vacía")
    //private Adress adress;

}
