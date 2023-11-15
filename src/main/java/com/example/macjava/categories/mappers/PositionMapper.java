package com.example.macjava.categories.mappers;

import com.example.macjava.categories.dto.PositionSaveDto;
import com.example.macjava.categories.dto.PositionUpdateDto;
import com.example.macjava.categories.models.Position;

public class PositionMapper {
    public static Position toModel(PositionSaveDto positionSaveDto) {
        return Position.builder()
                .name(positionSaveDto.getName())
                .build();
    }
    public static Position toModel(Position original,PositionUpdateDto positionUpdateDto) {
        return Position.builder()
                .name((positionUpdateDto.getName()==null) ? original.getName() : positionUpdateDto.getName())
                .isDeleted((positionUpdateDto.getIsDeleted()==null) ? original.getIsDeleted() : positionUpdateDto.getIsDeleted())
                .build();
    }
}
