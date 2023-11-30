package com.example.macjava.rest.categories.controllers;

import com.example.macjava.rest.categories.dto.PositionResponseDto;
import com.example.macjava.rest.categories.dto.PositionSaveDto;
import com.example.macjava.rest.categories.dto.PositionUpdateDto;
import com.example.macjava.rest.categories.mappers.PositionMapper;
import com.example.macjava.rest.categories.models.Position;
import com.example.macjava.rest.categories.services.PositionServiceImpl;
import com.example.macjava.utils.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@PreAuthorize("hasRole('USER')")
@RequestMapping(value = "/positions")
public class PositionController {
    private final PositionServiceImpl positionService;

    @Autowired
    public PositionController(PositionServiceImpl positionService) {
        this.positionService = positionService;
    }
    @Operation(summary = "Obtiene una categoria por su id", description = "Obtiene una categoria por su id")
    @Parameters({
            @Parameter(name = "id", description = "Identificador de la categoria", example = "1", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Position> findById(@PathVariable Long id){
        log.info("Buscando Posicion con id: " + id);
        return ResponseEntity.ok(positionService.findById(id));
    }

    @Operation(summary = "Obtiene todas las categorias", description = "Obtiene todas las categorias")
    @Parameters({
            @Parameter(name = "name", description = "Nombre de la categoria", example = "Programador"),
            @Parameter(name = "salaryMin", description = "Salario minimo de la categoria", example = "1000"),
            @Parameter(name = "salaryMax", description = "Salario maximo de la categoria", example = "2000"),
            @Parameter(name = "isDeleted", description = "Indica si la categoria esta eliminada", example = "true"),
            @Parameter(name = "page", description = "Pagina", example = "0"),
            @Parameter(name = "size", description = "Tamanio de la pagina", example = "10"),
            @Parameter(name = "sortBy", description = "Ordenamiento", example = "id"),
            @Parameter(name = "direction", description = "Ascendente o descendente", example = "asc")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categorias")
    })
    @GetMapping("/positions")
    public ResponseEntity<PageResponse<Position>> findAll(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<Integer> salaryMin,
            @RequestParam(required = false) Optional<Integer> salaryMax,
            @RequestParam(required = false,defaultValue = "false") Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        log.info("Buscando Posiciones");
        Sort sort= direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Position> pageResult = positionService.findAll(name, salaryMin, salaryMax, isDeleted, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }
    @Operation(summary = "Crea una nueva categoria", description = "Crea una nueva categoria")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Categoria a crear", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Creado"),
            @ApiResponse(responseCode = "400", description = "Error de validacion")
    })
    @PostMapping("position")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity <PositionResponseDto> createPosition(@Valid @RequestBody PositionSaveDto position){
        log.info("Guardando Posicion con nombre: " + position.getName());
        return ResponseEntity.ok(PositionMapper.toPositionResponseDto(positionService.save(position)));
    }
    @Operation(summary = "Actualiza una categoria", description = "Actualiza una categoria")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Categoria a actualizar", required = true)
    @Parameters({
            @Parameter(name = "id", description = "Identificador de la categoria", example = "1", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizado"),
            @ApiResponse(responseCode = "400", description = "Error de validacion"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @PutMapping("position/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PositionResponseDto> updatePosition(@PathVariable Long id, @Valid @RequestBody PositionUpdateDto position){
        log.info("Actualizando Posicion con id: " + id);
        return ResponseEntity.ok(PositionMapper.toPositionResponseDto(positionService.update(id, position)));
    }

    @Operation(summary = "Elimina una categoria", description = "Elimina una categoria")
    @Parameters({
            @Parameter(name = "id", description = "Identificador de la categoria", example = "1", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Eliminado"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @DeleteMapping("position/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePosition(@PathVariable Long id){
        log.info("Eliminando Posicion con Id: " + id);
        positionService.deleteById(id);
    }

    @Operation(summary = "Actualiza el campo isDeleted de una categoria", description = "Actualiza el campo isDeleted de una categoria")
    @Parameters({
            @Parameter(name = "id", description = "Identificador de la categoria", example = "1", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizado"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @PutMapping("position/isDeleted/{id}")
    public void updateIsDeletedToTrueById(@PathVariable Long id){
        log.info("Actualizando a TRUE isDeleted de la Posición con el id: " + id);
        positionService.updateIsDeletedToTrueById(id);
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
