package com.example.macjava.workers.mappers;

import com.example.macjava.categories.models.Position;
import com.example.macjava.workers.dto.WorkersResponseDto;
import com.example.macjava.workers.dto.WorkersSaveDto;
import com.example.macjava.workers.dto.WorkersUpdateDto;
import com.example.macjava.workers.models.Workers;

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
                .dni((workersUpdate.getDni()==null) ?   workersOriginal.getDni() : workersUpdate.getDni())
                .name((workersUpdate.getName()==null) ? workersOriginal.getName() : workersUpdate.getName())
                .surname((workersUpdate.getSurname()==null) ? workersOriginal.getSurname() : workersUpdate.getSurname())
                .age((workersUpdate.getAge()==0) ? workersOriginal.getAge() : workersUpdate.getAge())
                .phone((workersUpdate.getPhone()==null) ? workersOriginal.getPhone() : workersUpdate.getPhone())
                .isDeleted((workersUpdate.getIsDeleted()==null) ? workersOriginal.getIsDeleted() : workersUpdate.getIsDeleted())
                .position(position.equals(Position.SIN_CATEGORIA) ? workersOriginal.getPosition() : position)
                .createdAt(workersOriginal.getCreatedAt())
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
