package com.example.macjava.workers.services;

import com.example.macjava.categories.models.Position;
import com.example.macjava.workers.dto.WorkersSaveDto;
import com.example.macjava.workers.dto.WorkersUpdateDto;
import com.example.macjava.workers.models.Workers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkersService {
    Page<Workers> findAll(Optional<String> name, Optional<String> surname, Optional<Integer> age, Optional<String> phone,
                          Optional<Boolean> isDeleted, Optional<Integer> antiquierityMin, Optional<Integer> antiquierityMax,Optional<Integer> positionId, Pageable pageable);
    Workers findByUUID(UUID uuid);
    Workers save(WorkersSaveDto workers);
    Workers update(UUID uuid, WorkersUpdateDto workers);
    void deleteByUUID(UUID uuid);
    Workers findByDni(String dni);
    List<Workers> findByIsDeleted(Boolean isDeleted);
    void updateIsDeletedToTrueById(UUID uuid); //podria ser workers
}
