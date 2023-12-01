package com.example.macjava.rest.workers.models;

import com.example.macjava.rest.categories.models.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name="WORKERS")
@EntityListeners(AuditingEntityListener.class)
public class Workers {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT RANDOM_UUID()")
    @Builder.Default
    @Schema(description = "Identificador del trabajador", example = "12345678-1234-1234-1234-123456789012")
    private UUID uuid=UUID.randomUUID();
    @Schema(description = "DNI del trabajador", example = "12345678G")
    @Column(nullable = false, unique = true)//podria ser unique
    @NotBlank(message ="DNI cannot be blank")
    @Pattern(regexp = "\\d{8}[A-HJ-NP-TV-Z]", message = "El DNI debe tener el formato válido")
    private String dni;
    @Schema(description = "Nombre del trabajador", example = "Pepe")
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;
    @Schema(description = "Apellido del trabajador", example = "Perez")
    @Column(nullable = false)
    @NotBlank(message = "El apellido no puede estar vacío")
    private String surname;
    @Schema(description = "Edad del trabajador", example = "25")
    @Column(nullable = false)
    @NotNull(message = "La edad no puede estar vacía")
    @Positive(message = "La edad no puede ser negativa")
    private int age;
    @Schema(description = "Telefono del trabajador", example = "123456789")
    @Column(nullable = false)
    @NotBlank(message = "El telefono no puede estar vacío")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos")
    private String phone;
    @Schema(description = "Trabajador borrado", example = "true")
    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private Boolean isDeleted =false;
    @Schema(description = "Fecha de creación del trabajador", example = "2022-01-01T00:00:00")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column (updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt=LocalDateTime.now();
    @Schema(description = "Fecha de actualización del trabajador", example = "2022-01-01T00:00:00")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column (columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt=LocalDateTime.now();
    @Schema(description = "Posición del trabajador", example = "1")
    @ManyToOne
    @JoinColumn(name = "position_id")
    @NotNull(message = "La categoria no puede estar vacía")
    private Position position;
}
