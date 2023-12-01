package com.example.macjava.rest.clients.controller;

import com.example.macjava.rest.clients.dto.ClientdtoNew;
import com.example.macjava.rest.clients.dto.ClientdtoUpdated;
import com.example.macjava.rest.clients.models.Client;
import com.example.macjava.rest.clients.service.ClientService;
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
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@PreAuthorize("hasRole('USER')")
@Tag(name = "Clientes", description = "Endpoint de Clientes de nuestra tienda")
public class ClientController {
    ClientService service;
    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
    }
    @Operation(summary = "Obtiene todos los Clientes", description = "Obtiene todos los Clientes")
    @Parameters({
            @Parameter(name = "dni", description = "DNI del cliente", required = false),
            @Parameter(name = "name", description = "Nombre del cliente", required = false),
            @Parameter(name = "last_name", description = "Apellidos del cliente", required = false),
            @Parameter(name = "age", description = "Edad del cliente", required = false),
            @Parameter(name = "ageMax", description = "Edad maxima del cliente", required = false),
            @Parameter(name = "ageMin", description = "Edad minima del cliente", required = false),
            @Parameter(name = "phone", description = "Telefono del cliente", required = false),
            @Parameter(name = "deleted", description = "Eliminado del cliente", required = false),
            @Parameter(name = "page", description = "Pagina del cliente", required = false),
            @Parameter(name = "size", description = "Tamanio de la pagina del cliente", required = false),
            @Parameter(name = "sortBy", description = "Ordenamiento del cliente", required = false),
            @Parameter(name = "direction", description = "Ordenamiento del cliente", required = false)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagina de clientes"),
    })
    @GetMapping("/clientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<Client>> getProducts(
            @RequestParam(required = false) Optional<String> dni,
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<String> last_name,
            @RequestParam(required = false) Optional<Integer> age,
            @RequestParam(required = false) Optional<Integer> ageMax,
            @RequestParam(required = false) Optional<Integer> ageMin,
            @RequestParam(required = false) Optional<String> phone,
            @RequestParam(defaultValue = "false", required = false) Optional<Boolean> deleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Client> pageResult = service.findAll(dni,name, last_name, age, ageMax, ageMin, phone, deleted, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }
    @Operation(summary = "Obtiene un cliente por id", description = "Obtiene un cliente por id")
    @Parameters({
            @Parameter(name = "id", description = "Id del cliente", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente"),
            @ApiResponse(responseCode = "404", description = "No se encuentra el cliente")
    })
    @GetMapping("/clientes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Client> getProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }
    @Operation(summary = "Crea un nuevo cliente", description = "Crea un nuevo cliente")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cliente a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado"),
            @ApiResponse(responseCode = "400", description = "Error al crear el cliente")
    })
    @PostMapping("/clientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Client> createProduct(@Valid @RequestBody ClientdtoNew client)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(client));
    }
    @Operation(summary = "Actualiza un cliente", description = "Actualiza un cliente")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cliente a actualizar", required = true)
    @Parameters({
            @Parameter(name = "id", description = "Id del cliente", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado"),
            @ApiResponse(responseCode = "400", description = "Error al actualizar el cliente"),
            @ApiResponse(responseCode = "404", description = "No se encuentra el cliente")
    })
    @PutMapping("/clientes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Client> updateProduct(@PathVariable UUID id, @Valid @RequestBody ClientdtoUpdated client) {
        return ResponseEntity.ok(service.update(id,client));
    }
    @Operation(summary = "Elimina un cliente", description = "Elimina un cliente")
    @Parameters({
            @Parameter(name = "id", description = "Id del cliente", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado"),
            @ApiResponse(responseCode = "404", description = "No se encuentra el cliente")
    })
    @DeleteMapping("/clientes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualiza la imagen de un cliente", description = "Actualiza la imagen de un cliente")
    @Parameters({
            @Parameter(name = "id", description = "Id del cliente", required = true),
            @Parameter(name = "file", description = "Imagen del cliente", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen actualizada"),
            @ApiResponse(responseCode = "400", description = "Error al actualizar la imagen"),
            @ApiResponse(responseCode = "404", description = "No se encuentra el cliente")
    })
    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Client> nuevoProducto(
            @PathVariable UUID id,
            @RequestPart("file") MultipartFile file) {
        List<String> permittedContentTypes = List.of("image/png", "image/jpg", "image/jpeg", "image/gif");
        try {
            String contentType = file.getContentType();

            if (!file.isEmpty() && contentType != null && !contentType.isEmpty() && permittedContentTypes.contains(contentType.toLowerCase())) {
                return ResponseEntity.ok(service.updateImage(id, file, true));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el producto válida o esta está vacía");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede saber el tipo de la imagen");
        }
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
