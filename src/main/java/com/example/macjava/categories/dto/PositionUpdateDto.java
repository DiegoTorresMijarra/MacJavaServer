package com.example.macjava.categories.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionUpdateDto {
    private String name;
    private Boolean isDeleted;
    private Double salary;
    //private LocalDateTime createdAt;
}
