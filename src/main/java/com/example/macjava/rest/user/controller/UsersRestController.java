package com.example.macjava.rest.user.controller;

import com.example.macjava.rest.user.dto.UserInfoResponse;
import com.example.macjava.rest.user.dto.UserRequest;
import com.example.macjava.rest.user.dto.UserResponse;
import com.example.macjava.rest.user.models.User;
import com.example.macjava.rest.user.service.UsersService;
import com.example.macjava.utils.pagination.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("${api.version}/users")
public class UsersRestController {
    private final UsersService usersService;
    private final PedidosService pedidosService;
    @Autowired
    public UsersRestController(UsersService usersService, PedidosService pedidosService) {
        this.usersService = usersService;
        this.pedidosService = pedidosService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<PageResponse<UserResponse>> findAll(
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        // Creamos el objeto de ordenación
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Creamos cómo va a ser la paginación
        Page<UserResponse> pageResult = usersService.findAll(username, email, isDeleted, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<UserInfoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.save(userRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(usersService.update(id, userRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('USER')") // Solo los admin pueden acceder
    public ResponseEntity<UserInfoResponse> me(@AuthenticationPrincipal User user) {
        // Esta autenticado, por lo que devolvemos sus datos ya sabemos su id
        return ResponseEntity.ok(usersService.findById(user.getId()));
    }

    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<UserResponse> updateMe(@AuthenticationPrincipal User user, @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(usersService.update(user.getId(), userRequest));
    }

    @DeleteMapping("/me/profile")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal User user) {
        usersService.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/pedidos")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<PageResponse<Pedido>> getPedidosByUsuario(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(pedidosService.findByIdUsuario(user.getId(), pageable), sortBy, direction));
    }

    @GetMapping("/me/pedidos/{id}")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<Pedido> getPedido(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idPedido
    ) {
        var pedido = pedidosService.findById(idPedido);
        if (!pedido.getIdUsuario().equals(user.getId())) {
            throw new PedidoNotFound(pedido.get_id());
        }
        return ResponseEntity.ok(pedido);
    }

    @PostMapping("/me/pedidos")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Pedido> savePedido(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody Pedido pedido
    ) {
        pedido.setIdUsuario(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidosService.save(pedido));
    }

    @PutMapping("/me/pedidos/{id}")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<Pedido> updatePedido(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idPedido,
            @Valid @RequestBody Pedido pedido) {
        pedido.setIdUsuario(user.getId());
        var pedidoFinal = pedidosService.update(idPedido, pedido);
        if (!pedidoFinal.getIdUsuario().equals(user.getId())) {
            throw new PedidoNotFound(pedido.get_id());
        }
        return ResponseEntity.ok(pedidoFinal);
    }

    @DeleteMapping("/me/pedidos/{id}")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<Void> deletePedido(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idPedido
    ) {
        var pedido = pedidosService.findById(idPedido);
        if (!pedido.getIdUsuario().equals(user.getId())) {
            throw new PedidoNotFound(pedido.get_id());
        }
        pedidosService.delete(idPedido);
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
