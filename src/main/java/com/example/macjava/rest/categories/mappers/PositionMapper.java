package com.example.macjava.rest.categories.mappers;

import com.example.macjava.rest.categories.dto.PositionResponseDto;
import com.example.macjava.rest.categories.dto.PositionSaveDto;
import com.example.macjava.rest.categories.dto.PositionUpdateDto;
import com.example.macjava.rest.categories.models.Position;

/**
 * Mapeador de la entidad Position
 */
public class PositionMapper {
    /**
     * Convierte un objeto PositionSaveDto a un objeto Position
     * @param positionSaveDto   objeto PositionSaveDto
     * @return objeto Position
     */
    public static Position toModel(PositionSaveDto positionSaveDto) {
        return Position.builder()
                .name(positionSaveDto.getName())
                .salary(positionSaveDto.getSalary())
                .build();
    }

    /**
     * Actualiza una posicion con los datos de PositionUpdateDto
     * insertando en el nuevo objeto los datos del original si no se han modificado en el PositionUpdateDto
     * @param original objeto Position
     * @param positionUpdateDto objeto PositionUpdateDto
     * @return objeto Position actualizado
     */
    public static Position toModel(Position original, PositionUpdateDto positionUpdateDto) {
        return Position.builder()
                .id(original.getId())
                .salary(positionUpdateDto.getSalary())
                .name(positionUpdateDto.getName())
                .createdAt(original.getCreatedAt())
                .isDeleted(original.getIsDeleted())
                .build();
    }

    /**
     * Convierte un objeto Position a un objeto PositionResponseDto
     * @param position   objeto Position
     * @return  objeto PositionResponseDto
     */
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
