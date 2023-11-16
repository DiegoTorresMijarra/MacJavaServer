package com.example.macjava.workers.models;

import com.example.macjava.categories.models.Position;
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
@Table
@EntityListeners(AuditingEntityListener.class)
public class Workers {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT RANDOM_UUID()")
    @Builder.Default
    private UUID uuid=UUID.randomUUID();

    @Column(nullable = false)//podria ser unique
    @NotBlank(message ="DNI cannot be blank")
    @Pattern(regexp = "\\d{8}[A-HJ-NP-TV-Z]", message = "El DNI debe tener el formato válido")
    private String dni;

    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "El apellido no puede estar vacío")
    private String surname;

    @Column(nullable = false)
    @NotNull(message = "La edad no puede estar vacía")
    @Positive(message = "La edad no puede ser negativa")
    private int age;

    @Column(nullable = false)
    @NotBlank(message = "El telefono no puede estar vacío")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos")
    private String phone;

    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private Boolean isDeleted =false;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column (updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt=LocalDateTime.now();

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column (columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt=LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "position_id")
    @NotNull(message = "La categoria no puede estar vacía")
    private Position position;
}
