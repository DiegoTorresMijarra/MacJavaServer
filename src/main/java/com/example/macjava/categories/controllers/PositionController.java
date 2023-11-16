package com.example.macjava.categories.controllers;

import com.example.macjava.categories.dto.PositionResponseDto;
import com.example.macjava.categories.dto.PositionSaveDto;
import com.example.macjava.categories.dto.PositionUpdateDto;
import com.example.macjava.categories.mappers.PositionMapper;
import com.example.macjava.categories.models.Position;
import com.example.macjava.categories.services.PositionServiceImpl;
import com.example.macjava.utils.pagination.PageResponse;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = "/positions")
public class PositionController {
    private final PositionServiceImpl positionService;

    @Autowired
    public PositionController(PositionServiceImpl positionService) {
        this.positionService = positionService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Position> findById(@PathVariable Long id){
        log.info("Buscando Posicion con id: " + id);
        return ResponseEntity.ok(positionService.findById(id));
    }

    @GetMapping("/positions")
    public ResponseEntity<PageResponse<Position>> findAll(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<Integer> salaryMin,
            @RequestParam(required = false) Optional<Integer> salaryMax,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
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
    @PostMapping("position")
    public ResponseEntity <PositionResponseDto> createPosition(@Valid @RequestBody PositionSaveDto position){
        log.info("Guardando Posicion con nombre: " + position.getName());
        return ResponseEntity.ok(PositionMapper.toPositionResponseDto(positionService.save(position)));
    }
    @PutMapping("position/{id}")
    public ResponseEntity<PositionResponseDto> updatePosition(@PathVariable Long id, @Valid @RequestBody PositionUpdateDto position){
        log.info("Actualizando Posicion con id: " + id);
        return ResponseEntity.ok(PositionMapper.toPositionResponseDto(positionService.update(id, position)));
    }

    @DeleteMapping("position/{id}")
    public void deletePosition(@PathVariable Long id){
        log.info("Eliminando Posicion con Id: " + id);
        positionService.deleteById(id);
    }
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
