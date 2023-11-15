package com.example.macjava.workers.services;

import com.example.macjava.categories.models.Position;
import com.example.macjava.workers.dto.WorkersSaveDto;
import com.example.macjava.workers.dto.WorkersUpdateDto;
import com.example.macjava.workers.exceptions.WorkersNotFound;
import com.example.macjava.workers.mappers.WorkersMapper;
import com.example.macjava.workers.models.Workers;
import com.example.macjava.workers.repositories.WorkersCrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@CacheConfig(cacheNames = "workers")
public class WorkersServiceImpl implements WorkersService{
    private final WorkersCrudRepository workersCrudRepository;

    @Autowired
    public WorkersServiceImpl(WorkersCrudRepository workersCrudRepository) {
        log.info("Iniciando Servicio de Empleados");
        this.workersCrudRepository = workersCrudRepository;
    }

    //hay q revisarlo
    @Override
    public List<Workers> findAll(Specification<Workers> specification) {
        log.info("Buscando Empleados");
        return workersCrudRepository.findAll(specification);
    }

    @Override
    public Workers findByUUID(UUID uuid) {
        log.info("Buscando Empleado con UUID: " + uuid);
        return workersCrudRepository.findById(uuid).orElseThrow(()->new WorkersNotFound("UUID: "+ uuid));
    }

    @Override
    public Workers save(WorkersSaveDto workers, Position position) {
        log.info("Guardando Empleado con dni");
        return workersCrudRepository.save(WorkersMapper.toModel(workers, position));
    }

    @Override
    public Workers update(UUID uuid, WorkersUpdateDto workers, Position position) {
        log.info("Actualizando Empleado con UUID: " + uuid);
        Workers original= findByUUID(uuid);
        return workersCrudRepository.save(WorkersMapper.toModel(original,workers, position));
    }

    @Override
    public void deleteByUUID(UUID uuid) {
        log.info("Eliminando Empleado con UUID: " + uuid);
        workersCrudRepository.deleteById(uuid);
    }

    @Override
    public Workers findByDni(String dni) {
        log.info("Buscando empleado con DNI: " + dni);
        return workersCrudRepository.findByDni(dni).orElseThrow(()-> new WorkersNotFound("DNI: " + dni));
    }

    @Override
    public List<Workers> findByIsDeleted(Boolean isDeleted) {
        log.info("Buscando Empleados con isDeleted: " + isDeleted);
        return workersCrudRepository.findByIsDeleted(isDeleted);
    }

    @Override
    public void updateIsDeletedToTrueById(UUID uuid) {
        log.info("Actualizando Empleado con UUID: " + uuid);
        workersCrudRepository.updateIsDeletedToTrueById(uuid);
    }
}
