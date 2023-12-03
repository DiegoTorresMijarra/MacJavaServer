package com.example.macjava.rest.workers.controllers;

import com.example.macjava.rest.workers.services.WorkersServiceImpl;
import com.example.macjava.utils.pagination.PageResponse;
import com.example.macjava.rest.workers.dto.WorkersResponseDto;
import com.example.macjava.rest.workers.dto.WorkersSaveDto;
import com.example.macjava.rest.workers.dto.WorkersUpdateDto;
import com.example.macjava.rest.workers.mappers.WorkersMapper;
import com.example.macjava.rest.workers.models.Workers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controlador para el recurso de Empleados
 */
@RestController
@Slf4j
@RequestMapping(value = "/workers")
@PreAuthorize("hasRole('ADMIN')")
public class WorkersController {
    private final WorkersServiceImpl workersService;

    @Autowired
    public WorkersController(WorkersServiceImpl workersService) {
        this.workersService = workersService;
    }

    /**
     * Busca un Empleado por su UUID
     * @param uuid UUID del Empleado
     * @return Empleado encontrado
     */
    @Operation(summary = "Busca Empleado por su UUID", description = "Busca un Empleado por su UUID")
    @Parameters({
            @Parameter(name = "uuid", description = "UUID del Empleado", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<Workers> findByUUID(@PathVariable UUID uuid){
        log.info("Buscando Empleado con UUID: " + uuid);
        return ResponseEntity.ok(workersService.findByUUID(uuid));
    }

    /**
     * Busca todos los Empleados con paginación filtrados por los parámetros
     * @param name opcional: nombre del Empleado
     * @param surname opcional: apellidos del Empleado
     * @param age opcional: edad del Empleado
     * @param phone opcional: teléfono del Empleado
     * @param isDeleted opcional: si esta borradp
     * @param antiquierityMin opcional: antiguedad minima del Empleado
     * @param antiquierityMax opcional: antiguedad maxima del Empleado
     * @param positionId opcional: id de la posicion del Empleado
     * @param page número de página
     * @param size tamaño de la página
     * @param sortBy criterio de ordenamiento
     * @param direction dirección del ordenamiento
     * @return lista de Empleados paginados y filtrado
     */
    @Operation(summary = "Busca todos los Empleados", description = "Busca todos los Empleados")
    @Parameters({
            @Parameter(name = "name", description = "Nombre del Empleado"),
            @Parameter(name = "surname", description = "Apellidos del Empleado"),
            @Parameter(name = "age", description = "Edad del Empleado"),
            @Parameter(name = "phone", description = "Teléfono del Empleado"),
            @Parameter(name = "isDeleted", description = "Borrado del Empleado"),
            @Parameter(name = "antiquierityMin", description = "Antiguedad mínima del Empleado"),
            @Parameter(name = "antiquierityMax", description = "Antiguedad máxima del Empleado"),
            @Parameter(name = "positionId", description = "ID de la Posición del Empleado"),
            @Parameter(name = "page", description = "Número de página"),
            @Parameter(name = "size", description = "Tamaño de la página"),
            @Parameter(name = "sortBy", description = "Criterio de ordenamiento"),
            @Parameter(name = "direction", description = "Dirección del ordenamiento")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/workers")
    public ResponseEntity<PageResponse<Workers>> findAll(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<String> surname,
            @RequestParam(required = false) Optional<Integer> age,
            @RequestParam(required = false) Optional<String> phone,
            @RequestParam(required = false, defaultValue = "false" ) Optional<Boolean> isDeleted,
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

    /**
     * Crea un nuevo Empleado
     * @param worker Empleado a crear
     * @return Empleado creado
     */
    @Operation(summary = "Crea un nuevo Empleado", description = "Crea un nuevo Empleado")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Empleado a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("worker")
    public ResponseEntity<WorkersResponseDto> createWorker(@Valid @RequestBody WorkersSaveDto worker){
        log.info("Guardando Empleado con dni: " + worker.getDni());
        return ResponseEntity.ok(WorkersMapper.toWorkersResponseDto(workersService.save(worker)));
    }

    /**
     * Actualiza un Empleado
     * @param uuid UUID del Empleado
     * @param worker DTO con los datos a actualizar
     * @return Empleado actualizado
     */
    @Operation(summary = "Actualiza un Empleado", description = "Actualiza un Empleado")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Empleado a actualizar", required = true)
    @Parameters({
            @Parameter(name = "uuid", description = "UUID del Empleado", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PutMapping("worker/{uuid}")
    public ResponseEntity<WorkersResponseDto> updateWorker(@PathVariable UUID uuid, @Valid @RequestBody WorkersUpdateDto worker){
        log.info("Actualizando Empleado con UUID: " + uuid);
        return ResponseEntity.ok(WorkersMapper.toWorkersResponseDto(workersService.update(uuid, worker)));
    }

    /**
     * Elimina un Empleado
     * @param uuid UUID del Empleado
     */
    @Operation(summary = "Elimina un Empleado", description = "Elimina un Empleado")
    @Parameters({
            @Parameter(name = "uuid", description = "UUID del Empleado", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @Transactional
    @DeleteMapping("worker/{uuid}")
    public void deleteByUUID(@PathVariable UUID uuid){
        log.info("Eliminando Empleado con UUID: " + uuid);
        workersService.deleteByUUID(uuid);
    }

    /**
     * Busca un Empleado por su dni
     * @param dni dni del Empleado
     * @return Empleado encontrado
     */
    @Operation(summary = "Busca Empleado por su dni", description = "Busca un Empleado por su dni")
    @Parameters({
            @Parameter(name = "dni", description = "DNI del Empleado", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/dni/{dni}")
    public ResponseEntity<WorkersResponseDto> findByDni(@PathVariable String dni){
        log.info("Buscancando Empleado con dni: " + dni);
        return ResponseEntity.ok(WorkersMapper.toWorkersResponseDto(workersService.findByDni(dni)));
    }

    /**
     * Actualiza el valor de isDeleted a TRUE por su UUID
     * @param uuid UUID del Empleado
     */
    @Operation(summary = "Actualiza el valor de isDeleted a TRUE por su UUID", description = "Actualiza el valor de isDeleted a TRUE por su UUID")
    @Parameters({
            @Parameter(name = "uuid", description = "UUID del Empleado", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PutMapping("/worker/isDeleted/{uuid}")
    public void updateIsDeletedToTrueById(@PathVariable UUID uuid){
        log.info("Actualizando a TRUE el valor isDeleted del empleado con UUID: " + uuid);
        workersService.updateIsDeletedToTrueById(uuid);
    }

    /**
     * Busca Empleados por su isDeleted
     * @param isDeleted isDeleted del Empleado
     * @return Lista de Empleados encontrados
     */
    @Operation(summary = "Busca Empleados por su isDeleted", description = "Busca Empleados por su isDeleted")
    @Parameters({
            @Parameter(name = "isDeleted", description = "isDeleted del Empleado", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/worker/isDeleted/{isDeleted}")
    public ResponseEntity<List<WorkersResponseDto>> findByIsDeleted(@PathVariable Boolean isDeleted){
        log.info("Buscando Empleados con isDeleted: " + isDeleted);
        List <WorkersResponseDto> workers = workersService.findByIsDeleted(isDeleted).stream().map(WorkersMapper::toWorkersResponseDto).toList();//es bastante feo, tal vez deberia devolver solo workers
        return ResponseEntity.ok(workers);
    }

    /**
     * Maneja las excepciones de validación
     * @param ex excepción
     * @return mapa con los errores de validación
     */
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
