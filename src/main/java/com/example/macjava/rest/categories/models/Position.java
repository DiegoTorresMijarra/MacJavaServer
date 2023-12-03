package com.example.macjava.rest.categories.models;

import com.example.macjava.rest.workers.models.Workers;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo de datos para el tipo de trabajador
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class Position {
    public static final Position SIN_CATEGORIA = Position.builder() //no es igual a la de la base
            .id(-1L)
            .name("NOT_ASSIGNED")
            .build();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador de la categoria", example = "1")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false,columnDefinition = "VARCHAR (13) CONSTRAINT CHECK_NAME CHECK name IN ('MANAGER','COOKER','CLEANER','WAITER','NOT_ASSIGNED')") //podria ser unique
    @Schema(description = "Nombre de la categoria", example = "MANAGER")
    private String name;

    @NotNull
    @DecimalMin(value = "1000.00", message = "El salario no puede ser menor de 1000.00")
    @Schema(description = "Salario de la categoria", example = "1000.00")
    private Double salary;

    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    @Schema(description = "Indica si la categoria ha sido eliminada", example = "false")
    private Boolean isDeleted=false;

    @CreatedBy
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column ( nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de creación de la categoria", example = "2022-01-01 00:00:00")
    private LocalDateTime createdAt=LocalDateTime.now();

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column (columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de actualización de la categoria", example = "2022-01-01 00:00:00")
    private LocalDateTime updatedAt=LocalDateTime.now();

    @OneToMany(mappedBy = "position")
    @JsonBackReference
    @Schema(description = "Lista de trabajadores")
    private List<Workers> workers;
}
