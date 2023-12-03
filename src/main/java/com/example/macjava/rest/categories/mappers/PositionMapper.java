package com.example.macjava.rest.categories.mappers;

import com.example.macjava.rest.categories.dto.PositionResponseDto;
import com.example.macjava.rest.categories.dto.PositionSaveDto;
import com.example.macjava.rest.categories.dto.PositionUpdateDto;
import com.example.macjava.rest.categories.models.Position;

public class PositionMapper {
    public static Position toModel(PositionSaveDto positionSaveDto) {
        return Position.builder()
                .name(positionSaveDto.getName())
                .salary(positionSaveDto.getSalary())
                .build();
    }
    public static Position toModel(Position original, PositionUpdateDto positionUpdateDto) {
        return Position.builder()
                .id(original.getId())
                .salary(positionUpdateDto.getSalary())
                .name(positionUpdateDto.getName())
                .createdAt(original.getCreatedAt())
                .isDeleted(original.getIsDeleted())
                .build();
    }
    public static PositionResponseDto toPositionResponseDto(Position position){
        return PositionResponseDto.builder()
                .id(position.getId())
                .name(position.getName())
                .salary(position.getSalary())
                .isDeleted(position.getIsDeleted())
                .createdAt(position.getCreatedAt())
                .updatedAt(position.getUpdatedAt())
                .build();
    }

}
