package com.example.macjava.workers.services;

import com.example.macjava.categories.models.Position;
import com.example.macjava.workers.dto.WorkersSaveDto;
import com.example.macjava.workers.dto.WorkersUpdateDto;
import com.example.macjava.workers.models.Workers;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public interface WorkersService {
    List<Workers> findAll(Specification<Workers> specification);
    Workers findByUUID(UUID uuid);
    Workers save(WorkersSaveDto workers, Position position);
    Workers update(UUID uuid, WorkersUpdateDto workers, Position position);
    void deleteByUUID(UUID uuid);
    Workers findByDni(String dni);
    List<Workers> findByIsDeleted(Boolean isDeleted);
    void updateIsDeletedToTrueById(UUID uuid); //podria ser workers
}
