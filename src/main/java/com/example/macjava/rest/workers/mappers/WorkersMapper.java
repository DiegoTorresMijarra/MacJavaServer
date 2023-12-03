package com.example.macjava.rest.workers.mappers;

import com.example.macjava.rest.categories.models.Position;
import com.example.macjava.rest.workers.dto.WorkersResponseDto;
import com.example.macjava.rest.workers.models.Workers;
import com.example.macjava.rest.workers.dto.WorkersSaveDto;
import com.example.macjava.rest.workers.dto.WorkersUpdateDto;
import jakarta.validation.Valid;

/**
 * Mapeador para la entidad Workers
 */
public class WorkersMapper {
    /**
     * Mapea los datos del dto a la entidad
     * @param workersSaveDto   dto con los datos del trabajador
     * @param position        posición del trabajador
     * @return entidad creada con los datos del dto
     */
    public static Workers toModel(WorkersSaveDto workersSaveDto, Position position) {
        return Workers.builder()
                .dni(workersSaveDto.getDni())
                .name(workersSaveDto.getName())
                .surname(workersSaveDto.getSurname())
                .age(workersSaveDto.getAge())
                .phone(workersSaveDto.getPhone())
                .position(position)
                .build();
    }

    /**
     * Mapea los datos del dto a la entidad
     * @param workersOriginal  entidad original
     * @param workersUpdate   dto con los datos a actualizar
     * @param position       posición del trabajador
     * @return entidad actualizada con los datos del dto
     */
    public static Workers toModel(Workers workersOriginal, WorkersUpdateDto workersUpdate, Position position) {
        return Workers.builder()
                .uuid(workersOriginal.getUuid())
                .dni(workersUpdate.getDni())
                .name(workersUpdate.getName())
                .surname(workersUpdate.getSurname())
                .age(workersUpdate.getAge())
                .phone(workersUpdate.getPhone())
                .position(position.equals(Position.SIN_CATEGORIA)?workersOriginal.getPosition():position)
                .createdAt(workersOriginal.getCreatedAt())
                .isDeleted(workersOriginal.getIsDeleted())
                .build();
    }

    /**
     * Mapea los datos de la entidad a la respuesta
     * @param workers entidad con los datos del trabajador
     * @return dto con los datos de la entidad
     */
    public static WorkersResponseDto toWorkersResponseDto(Workers workers){
        return WorkersResponseDto.builder()
                .uuid(workers.getUuid())
                .name(workers.getName())
                .surname(workers.getSurname())
                .dni(workers.getDni())
                .phone(workers.getPhone())
                .createdAt(workers.getCreatedAt())
                .updatedAt(workers.getUpdatedAt())
                .position(workers.getPosition())
                .age(workers.getAge())
                .isDeleted(workers.getIsDeleted())
                .build();
    }
}
