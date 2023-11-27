package com.example.macjava.rest.workers.dto;

import com.example.macjava.rest.categories.models.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
