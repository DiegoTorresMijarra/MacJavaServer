package com.example.macjava.rest.restaurants.controllers;

import com.example.macjava.rest.restaurants.servicios.RestaurantService;
import com.example.macjava.rest.restaurants.dto.NewRestaurantDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.example.macjava.rest.restaurants.dto.UpdatedRestaurantDTO;
import com.example.macjava.rest.restaurants.modelos.Restaurant;
import com.example.macjava.utils.pagination.PageResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador de la entidad Restaurant
 * Anotamos la clase con @RestController para indicar que es un controlador
 * @Parameter service Servicio de restaurantes
 */
@RestController
public class RestaurantController {
    RestaurantService service;

    /**
     * Constructor de la clase
     * @param service Servicio de restaurantes
     */
    @Autowired
    public RestaurantController(RestaurantService service){
        this.service=service;
    }

    /**
     * Método que obtiene todos los restaurantes que cumplan con los parámetros de búsqueda
     * @param name Opcional: nombre del restaurante
     * @param number Opcional: número de teléfono del restaurante
     * @param isDeleted Opcional: indica si el restaurante está eliminado
     * @param page informacion de la paginación
     * @return Pagina de restaurantes que cumplan con los parámetros de búsqueda
     */
    @Operation(summary = "Obtiene todos los restaurantes que cumplan con los parámetros de búsqueda")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "name", description = "Nombre del restaurante", example = "Restaurante"),
            @io.swagger.v3.oas.annotations.Parameter(name = "number", description = "Número de teléfono del restaurante", example = "123456789"),
            @io.swagger.v3.oas.annotations.Parameter(name = "isDeleted", description = "Indica si el restaurante está eliminado", example = "false"),
            @io.swagger.v3.oas.annotations.Parameter(name = "page", description = "Número de página a recuperar", example = "0"),
            @io.swagger.v3.oas.annotations.Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @io.swagger.v3.oas.annotations.Parameter(name = "sortBy", description = "Campo por el que ordenar", example = "id"),
            @io.swagger.v3.oas.annotations.Parameter(name = "direction", description = "Dirección de la ordenación", example = "asc"),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Información de la paginación", required=false)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de restaurantes que cumplan con los parámetros de búsqueda"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en los parámetros de búsqueda"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante no encontrado"),
    })
    @GetMapping("/restaurant")
    public ResponseEntity<PageResponse<Restaurant>> getRestaurants(
            @RequestParam(required=false) Optional<String> name,
            @RequestParam(required = false)Optional<String> number,
            @RequestParam(defaultValue = "false",required = false)Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
            ){
        Sort sort=direction.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Page<Restaurant> pageResult= service.findAll(name, number,isDeleted, PageRequest.of(page,size,sort));
        return ResponseEntity.ok().body(PageResponse.of(pageResult,sortBy,direction));
    }

    /**
     * Obtiene un restaurante por su ID
     * @param id ID del restaurante a buscar
     * @return Restaurante que coincida con el ID
     */
    @Operation(summary = "Obtiene un restaurante por su ID")
    @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "ID del restaurante a buscar", example = "1L")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante no encontrado"),
    })
    @GetMapping ("/restaurant/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Guarda un restaurante
     * @param restaurantDTO RestauranteDTO con la informacion del restaurante a guardar
     * @return Restaurante guardado
     */
    @Operation(summary = "Guarda un restaurante con la información de un RestauranteDTO")
    @Parameter(name = "restaurantDTO", description = "RestauranteDTO con información del restaurante a guardar", required = true)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Restaurante guardado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en los parámetros del Restaurante"),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Restaurante a guardar", required=true)
    @PostMapping("/restaurant")
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody NewRestaurantDTO restaurantDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(restaurantDTO));
    }

    /**
     * Actualiza un restaurante
     * @param id ID del restaurante a actualizar
     * @param restaurantDTO RestauranteDTO con la información a actualizar
     * @return Restaurante actualizado
     */
    @Operation(summary = "Actualiza un restaurante con la información de un RestauranteDTO")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "ID del restaurante a actualizar", example = "1L",required = true),
            @io.swagger.v3.oas.annotations.Parameter(name = "restaurantDTO", description = "RestauranteDTO con información actualizada", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Restaurante a actualizar", required=true)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurante actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en los parámetros del Restaurante"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante no encontrado"),
    })
    @PutMapping("/restaurant/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @Valid @RequestBody UpdatedRestaurantDTO restaurantDTO){
        return ResponseEntity.ok(service.update(id, restaurantDTO));
    }
    /**
     * Elimina un restaurante
     * @param id ID del restaurante a eliminar
     */
    @Operation(summary = "Elimina un restaurante por su ID")
    @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "ID del restaurante a eliminar", example = "1L",required = true)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Restaurante eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en el id del restaurante"),
    })
    @DeleteMapping("/restaurant/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Controla las excepciones de validación
     * @param excep Excepción de validación
     * @return Mapa con los errores de validación
     */
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
