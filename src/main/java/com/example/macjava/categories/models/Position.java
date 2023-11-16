package com.example.macjava.categories.models;

import com.example.macjava.workers.models.Workers;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import javafx.geometry.Pos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

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
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false,columnDefinition = "VARCHAR (13) CONSTRAINT CHECK_NAME CHECK name IN ('MANAGER','COOKER','CLEANER','WAITER','NOT_ASSIGNED')") //podria ser unique
    private String name;

    @NotNull
    @DecimalMin(value = "1000.00", message = "El salario no puede ser menor de 1000.00")
    private Double salary;

    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private Boolean isDeleted=false;

    @CreatedBy
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column ( nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt=LocalDateTime.now();

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column (columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt=LocalDateTime.now();

    @OneToMany(mappedBy = "position")
    @JsonBackReference
    private List<Workers> workers;
}
