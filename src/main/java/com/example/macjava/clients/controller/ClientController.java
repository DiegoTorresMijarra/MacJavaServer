package com.example.macjava.clients.controller;

import com.example.macjava.clients.dto.ClientdtoNew;
import com.example.macjava.clients.dto.ClientdtoUpdated;
import com.example.macjava.clients.models.Client;
import com.example.macjava.clients.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class ClientController {
    ClientService service;
    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
    }
    @GetMapping("/clientes")
    public ResponseEntity<List<Client>> getProducts() {
        return ResponseEntity.ok(service.findAll());
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
