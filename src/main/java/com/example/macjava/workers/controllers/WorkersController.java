package com.example.macjava.workers.controllers;

import com.example.macjava.categories.models.Position;
import com.example.macjava.categories.services.PositionServiceImpl;
import com.example.macjava.workers.dto.WorkersResponseDto;
import com.example.macjava.workers.dto.WorkersSaveDto;
import com.example.macjava.workers.dto.WorkersUpdateDto;
import com.example.macjava.workers.mappers.WorkersMapper;
import com.example.macjava.workers.models.Workers;
import com.example.macjava.workers.services.WorkersServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = "/workers")
public class WorkersController {
    private final WorkersServiceImpl workersService;
    private final PositionServiceImpl positionService;

    @Autowired
    public WorkersController(WorkersServiceImpl workersService, PositionServiceImpl positionService) {
        this.workersService = workersService;
        this.positionService = positionService;
    }
    @GetMapping("/{uuid}")
    public ResponseEntity<Workers> findByUUID(UUID uuid){
        log.info("Buscando Empleado con UUID: " + uuid);
        return ResponseEntity.ok(workersService.findByUUID(uuid));
    }
    /*
    @GetMapping("/workers")
    public ResponseEntity<List<Workers>> findAll(){
        log.info("Buscando Empleados");
        return ResponseEntity.ok(workersService.findAll());
    }

     */
    @PostMapping("worker")
    public ResponseEntity<WorkersResponseDto> createWorker(@Valid @RequestBody WorkersSaveDto worker){
        log.info("Guardando Empleado con dni: " + worker.getDni());
        Position position= positionService.findById(worker.getPositionId());
        return ResponseEntity.ok(WorkersMapper.toWorkersResponseDto(workersService.save(worker,position)));
    }
    @PutMapping("worker/{uuid}")
    public ResponseEntity<WorkersResponseDto> updateWorker(@PathVariable UUID uuid, @Valid @RequestBody WorkersUpdateDto worker){
        Position position;
        if(worker.getPositionId()==-1L){
            position=Position.SIN_CATEGORIA;
        }
        else
            position=positionService.findById(worker.getPositionId());

        return ResponseEntity.ok(WorkersMapper.toWorkersResponseDto(workersService.update(uuid, worker, position)));
    }

    @DeleteMapping("worker/{uuid}")
    public void deleteByUUID(@PathVariable UUID uuid){
        log.info("Eliminando Empleado con UUID: " + uuid);
        workersService.deleteByUUID(uuid);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<WorkersResponseDto> findByDni(@PathVariable String dni){
        log.info("Buscancando Empleado con dni: " + dni);
        return ResponseEntity.ok(WorkersMapper.toWorkersResponseDto(workersService.findByDni(dni)));
    }

    @PutMapping("/worker/isDeleted/{uuid}")
    public void updateIsDeletedToTrueById(@PathVariable UUID uuid){
        log.info("Actualizando a TRUE el valor isDeleted del empleado con UUID: " + uuid);
        workersService.findByUUID(uuid);
        workersService.updateIsDeletedToTrueById(uuid);
    }

    @GetMapping("/worker/isDeleted/{isDeleted}")
    public ResponseEntity<List<WorkersResponseDto>> findByIsDeleted(@PathVariable Boolean isDeleted){
        log.info("Buscando Empleados con isDeleted: " + isDeleted);
        List <WorkersResponseDto> workers = workersService.findByIsDeleted(isDeleted).stream().map(WorkersMapper::toWorkersResponseDto).toList();//es bastante feo, tal vez deberia devolver solo workers
        return ResponseEntity.ok(workers);
    }
}
