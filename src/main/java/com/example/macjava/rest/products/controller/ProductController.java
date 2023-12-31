package com.example.macjava.rest.products.controller;

import com.example.macjava.rest.products.dto.ProductdtoNew;
import com.example.macjava.rest.products.dto.ProductdtoUpdate;
import com.example.macjava.rest.products.models.Product;
import com.example.macjava.rest.products.services.ProductService;
import com.example.macjava.utils.pagination.PageResponse;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador de la entidad Product
 * Anotación @RestController para indicar que es un controlador
 */
@RestController
@Tag(name = "Productos", description = "Endpoint de Productos de nuestra tienda")
public class ProductController {
    ProductService service;

    /**
     * Constructor de la clase
     * @param service Servicio de productos
     */
    @Autowired
    public ProductController(ProductService service){
        this.service = service;
    }

    /**
     * Método que obtiene todos los productos que cumplan con los parámetros de búsqueda
     * @param nombre Nombre del producto
     * @param stockMax Stock máximo del producto
     * @param stockMin Stock mínimo del producto
     * @param precioMax Precio máximo del producto
     * @param precioMin Precio mínimo del producto
     * @param gluten indica si el producto tiene gluten
     * @param is_deleted indica si el producto está eliminado
     * @param page Número de página
     * @param size Tamaño de la página
     * @param sortBy Campo de ordenación
     * @param direction Dirección de ordenación
     * @return  Página de productos que cumplan con los parámetros de búsqueda
     */
    @Operation(summary = "Obtiene todos los productos", description = "Obtiene una lista de productos")
    @Parameters({
            @Parameter(name = "nombre", description = "Nombre del producto", example = "Hamburguesa"),
            @Parameter(name = "is_deleted", description = "Si está borrado o no", example = "false"),
            @Parameter(name = "stockMax", description = "Stock máximo del producto", example = "20"),
            @Parameter(name = "stockMin", description = "Stock mínimo del producto", example = "10"),
            @Parameter(name = "precioMax", description = "Precio máximo del producto", example = "20"),
            @Parameter(name = "precioMin", description = "Precio mínimo del producto", example = "10"),
            @Parameter(name = "gluten", description = "Si está gluten o no", example = "false"),
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo de ordenación", example = "id"),
            @Parameter(name = "direction", description = "Dirección de ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de productos"),
            @ApiResponse(responseCode = "400", description = "Petición incorrecta"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/productos")
    public ResponseEntity<PageResponse<Product>> getProducts(
            @RequestParam(required = false) Optional<String> nombre,
            @RequestParam(required = false) Optional<Integer> stockMax,
            @RequestParam(required = false) Optional<Integer> stockMin,
            @RequestParam(required = false) Optional<Double> precioMax,
            @RequestParam(required = false) Optional<Double> precioMin,
            @RequestParam(required = false) Optional<Boolean> gluten,
            @RequestParam(required = false, defaultValue = "false") Optional<Boolean> is_deleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Product> pageResult = service.findAll(nombre,stockMax,stockMin,precioMax,precioMin,gluten,is_deleted, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }
    /**
     * Obtiene un producto por su ID
     * @param id ID del producto a buscar
     * @return Producto que coincida con el ID
     */
    @Operation(summary = "Obtiene un producto por su id", description = "Obtiene un producto por su id")
    @Parameters({
            @Parameter(name = "id", description = "Identificador del producto", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/productos/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    /**
     * Guarda un producto
     * @param productdto Producto a guardar
     * @return Producto guardado
     */
    @Operation(summary = "Crea un producto", description = "Crea un producto")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Producto a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado"),
            @ApiResponse(responseCode = "400", description = "Producto no válido"),
    })
    @PostMapping("/productos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductdtoNew productdto)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(productdto));
    }
    /**
     * Actualiza un producto
     * @param id ID del producto a actualizar
     * @param productdto Producto con la información a actualizar
     * @return Producto actualizado
     */
    @Operation(summary = "Actualiza un producto", description = "Actualiza un producto")
    @Parameters({
            @Parameter(name = "id", description = "Identificador del producto", example = "1", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Producto a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "400", description = "Producto no válido"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/productos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductdtoUpdate productdto) {
        return ResponseEntity.ok(service.update(id,productdto));
    }

    /**
     * Elimina un producto (borrado lógico)
     * @param id ID del producto a eliminar
     */
    @Operation(summary = "Borra un producto", description = "Borra un producto")
    @Parameters({
            @Parameter(name = "id", description = "Identificador del producto", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto borrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    })
    @DeleteMapping("/productos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Manejador de excepciones para los errores de validación
     * @param ex Excepción de validación
     * @return Mapa con los errores de validación
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
