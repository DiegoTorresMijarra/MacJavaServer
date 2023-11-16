package com.example.macjava.workers.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkersUpdateDto {
    private String dni;
    private String name;
    private String surname;
    private int age;
    private String phone;
    private Boolean isDeleted;
    @Builder.Default
    private Long positionId=-1L;
}
