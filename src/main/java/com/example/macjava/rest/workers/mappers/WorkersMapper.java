package com.example.macjava.rest.workers.mappers;

import com.example.macjava.rest.categories.models.Position;
import com.example.macjava.rest.workers.dto.WorkersResponseDto;
import com.example.macjava.rest.workers.models.Workers;
import com.example.macjava.rest.workers.dto.WorkersSaveDto;
import com.example.macjava.rest.workers.dto.WorkersUpdateDto;
import jakarta.validation.Valid;

public class WorkersMapper {

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
