package com.example.macjava.workers.dto;

import com.example.macjava.categories.models.Position;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class WorkersResponseDto {
    private UUID uuid;
    private String dni;
    private String name;
    private String surname;
    private int age;
    private String phone;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Position position;
}
