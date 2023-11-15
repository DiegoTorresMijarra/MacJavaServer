package com.example.macjava.categories.models;

import com.example.macjava.workers.models.Workers;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    public static final Position SIN_CATEGORIA = null;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false,columnDefinition = "VARCHAR (13) UNIQUE CONSTRAINT CHECK_NAME CHECK NOMBRE IN ('MANAGER','COOKER','CLEANER','WAITER','NOT_ASSIGNED')")
    private String name;

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
