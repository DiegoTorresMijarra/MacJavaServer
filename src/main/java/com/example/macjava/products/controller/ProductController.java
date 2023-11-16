package com.example.macjava.products.controller;

import com.example.macjava.products.dto.ProductdtoNew;
import com.example.macjava.products.dto.ProductdtoUpdate;
import com.example.macjava.products.models.Product;
import com.example.macjava.products.services.ProductService;
import com.example.macjava.products.utils.PageResponse;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProductController {
    ProductService service;
    @Autowired
    public ProductController(ProductService service){
        this.service = service;
    }
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
    @GetMapping("/productos/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    @PostMapping("/productos")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductdtoNew productdto)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(productdto));
    }
    @PutMapping("/productos/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductdtoUpdate productdto) {
        return ResponseEntity.ok(service.update(id,productdto));
    }
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
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
