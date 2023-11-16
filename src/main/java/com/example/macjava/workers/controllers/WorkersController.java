package com.example.macjava.workers.controllers;

import com.example.macjava.utils.pagination.PageResponse;
import com.example.macjava.workers.dto.WorkersResponseDto;
import com.example.macjava.workers.dto.WorkersSaveDto;
import com.example.macjava.workers.dto.WorkersUpdateDto;
import com.example.macjava.workers.mappers.WorkersMapper;
import com.example.macjava.workers.models.Workers;
import com.example.macjava.workers.services.WorkersServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
@RequestMapping(value = "/workers")
public class WorkersController {
    private final WorkersServiceImpl workersService;

    @Autowired
    public WorkersController(WorkersServiceImpl workersService) {
        this.workersService = workersService;
    }
    @GetMapping("/{uuid}")
    public ResponseEntity<Workers> findByUUID(@PathVariable UUID uuid){
        log.info("Buscando Empleado con UUID: " + uuid);
        return ResponseEntity.ok(workersService.findByUUID(uuid));
    }
    @GetMapping("/workers")
    public ResponseEntity<PageResponse<Workers>> findAll(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<String> surname,
            @RequestParam(required = false) Optional<Integer> age,
            @RequestParam(required = false) Optional<String> phone,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @RequestParam(required = false) Optional<Integer> antiquierityMin,
            @RequestParam(required = false) Optional<Integer> antiquierityMax,
            @RequestParam(required = false) Optional<Integer> positionId, //me da algun problema al hacerlo long
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "uuid") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        log.info("Buscando Empleados");
        Sort sort= direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Workers> pageResult = workersService.findAll(name, surname, age, phone, isDeleted, antiquierityMin, antiquierityMax,positionId, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @PostMapping("worker")
    public ResponseEntity<WorkersResponseDto> createWorker(@Valid @RequestBody WorkersSaveDto worker){
        log.info("Guardando Empleado con dni: " + worker.getDni());
        return ResponseEntity.ok(WorkersMapper.toWorkersResponseDto(workersService.save(worker)));
    }
    @PutMapping("worker/{uuid}")
    public ResponseEntity<WorkersResponseDto> updateWorker(@PathVariable UUID uuid, @Valid @RequestBody WorkersUpdateDto worker){
        log.info("Actualizando Empleado con UUID: " + uuid);
        return ResponseEntity.ok(WorkersMapper.toWorkersResponseDto(workersService.update(uuid, worker)));
    }

    @Transactional
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
        workersService.updateIsDeletedToTrueById(uuid);
    }

    @GetMapping("/worker/isDeleted/{isDeleted}")
    public ResponseEntity<List<WorkersResponseDto>> findByIsDeleted(@PathVariable Boolean isDeleted){
        log.info("Buscando Empleados con isDeleted: " + isDeleted);
        List <WorkersResponseDto> workers = workersService.findByIsDeleted(isDeleted).stream().map(WorkersMapper::toWorkersResponseDto).toList();//es bastante feo, tal vez deberia devolver solo workers
        return ResponseEntity.ok(workers);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
