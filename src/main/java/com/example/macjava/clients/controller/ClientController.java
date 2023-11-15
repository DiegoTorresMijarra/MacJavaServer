package com.example.macjava.clients.controller;

import com.example.macjava.clients.dto.ClientdtoNew;
import com.example.macjava.clients.dto.ClientdtoUpdated;
import com.example.macjava.clients.models.Client;
import com.example.macjava.clients.service.ClientService;
import com.example.macjava.clients.utils.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
public class ClientController {
    ClientService service;
    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
    }
    @GetMapping("/clientes")
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
    @GetMapping("/clientes/{id}")
    public ResponseEntity<Client> getProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }
    @PostMapping("/clientes")
    public ResponseEntity<Client> createProduct(@Valid @RequestBody ClientdtoNew client)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(client));
    }
    @PutMapping("/clientes/{id}")
    public ResponseEntity<Client> updateProduct(@PathVariable UUID id, @Valid @RequestBody ClientdtoUpdated client) {
        return ResponseEntity.ok(service.update(id,client));
    }
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
