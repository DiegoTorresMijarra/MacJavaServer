package com.example.macjava.rest.restaurantes.controllers;

import com.example.macjava.rest.restaurantes.servicios.RestaurantService;
import com.example.macjava.rest.restaurantes.dto.NewRestaurantDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import com.example.macjava.rest.restaurantes.dto.UpdatedRestaurantDTO;
import com.example.macjava.rest.restaurantes.modelos.Restaurante;
import com.example.macjava.utils.pagination.PageResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador de la clase Restaurante de tipo RESTController
 * Se usa el repositorio de la clase y se inyecta mediante la anotacion Autowired
 */
@RestController
@PreAuthorize("hasRole('USER')")
@Tag(name = "Restaurantes", description = "Endpoint de restaurantes de nuestra tienda")
public class RestaurantController {
    RestaurantService service;

    @Autowired
    public RestaurantController(RestaurantService service){
        this.service=service;
    }

    @Operation(summary = "Obtener todos los restaurantes", description = "Obtener todos los restaurantes")
    @Parameters({
            @Parameter(name = "name", description = "Filtrar por nombre"),
            @Parameter(name = "number", description = "Filtrar por numero de contacto"),
            @Parameter(name = "isDeleted", description = "Filtrar por estado"),
            @Parameter(name = "page", description = "Paginacion"),
            @Parameter(name = "size", description = "Tamanio de la paginacion"),
            @Parameter(name = "sortBy", description = "Ordenar por"),
            @Parameter(name = "direction", description = "Ascendente o descendente")
    })
    @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Pagina de restaurantes")
    })
    @GetMapping("/restaurantes")
    public ResponseEntity<PageResponse<Restaurante>> getRestaurants(
            @RequestParam(required=false) Optional<String> name,
            @RequestParam(required = false)Optional<String> number,
            @RequestParam(defaultValue = "false",required = false)Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
            ){
        Sort sort=direction.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Page<Restaurante> pageResult= service.findAll(name, number,isDeleted, PageRequest.of(page,size,sort));
        return ResponseEntity.ok().body(PageResponse.of(pageResult,sortBy,direction));
    }

    @Operation(summary = "Obtener un restaurante", description = "Obtener un restaurante")
    @Parameters({
            @Parameter(name = "id", description = "Id del restaurante")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante"),
            @ApiResponse(responseCode = "404", description = "Restaurante no encontrado")
    })
    @GetMapping ("/restaurantes/{id}")
    public ResponseEntity<Restaurante> getRestaurant(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear un nuevo restaurante", description = "Crear un nuevo restaurante")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Restaurante a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante creado"),
            @ApiResponse(responseCode = "400", description = "Error de validacion")
    })
    @PostMapping("/restaurantes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Restaurante> createRestaurant(@Valid @RequestBody NewRestaurantDTO restaurantDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(restaurantDTO));
    }

    @Operation(summary = "Actualizar un restaurante", description = "Actualizar un restaurante")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Restaurante a actualizar", required = true)
    @Parameters({
            @Parameter(name = "id", description = "Id del restaurante")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante actualizado"),
            @ApiResponse(responseCode = "400", description = "Error de validacion"),
            @ApiResponse(responseCode = "404", description = "Restaurante no encontrado")
    })
    @PutMapping("/restaurantes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Restaurante> updateRestaurant(@PathVariable Long id, @Valid @RequestBody UpdatedRestaurantDTO restaurantDTO){
        return ResponseEntity.ok(service.update(id, restaurantDTO));
    }

    @Operation(summary = "Eliminar un restaurante", description = "Eliminar un restaurante")
    @Parameters({
            @Parameter(name = "id", description = "Id del restaurante")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurante actualizado"),
            @ApiResponse(responseCode = "404", description = "Restaurante no encontrado")
    })
    @DeleteMapping("/restaurantes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleValidationExceptions(MethodArgumentNotValidException excep){
        Map<String,String> errors = new HashMap<>();
        excep.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
