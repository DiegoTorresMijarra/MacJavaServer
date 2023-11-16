package com.example.macjava.categories.mappers;

import com.example.macjava.categories.dto.PositionResponseDto;
import com.example.macjava.categories.dto.PositionSaveDto;
import com.example.macjava.categories.dto.PositionUpdateDto;
import com.example.macjava.categories.models.Position;

public class PositionMapper {
    public static Position toModel(PositionSaveDto positionSaveDto) {
        return Position.builder()
                .name(positionSaveDto.getName())
                .salary(positionSaveDto.getSalary())
                .build();
    }
    public static Position toModel(Position original,PositionUpdateDto positionUpdateDto) {
        return Position.builder()
                .id(original.getId())
                .salary((positionUpdateDto.getSalary()==null) ? original.getSalary() : positionUpdateDto.getSalary())
                .name((positionUpdateDto.getName()==null) ? original.getName() : positionUpdateDto.getName())
                .isDeleted((positionUpdateDto.getIsDeleted()==null) ? original.getIsDeleted() : positionUpdateDto.getIsDeleted())
                .createdAt(original.getCreatedAt())
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
