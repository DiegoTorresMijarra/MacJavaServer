package com.example.macjava.categories.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PositionUpdateDto {
    private String name;
    private Boolean isDeleted;
    //private LocalDateTime createdAt;
}
