package com.example.macjava.rest.user.controller;

import com.example.macjava.rest.user.dto.UserInfoResponse;
import com.example.macjava.rest.user.dto.UserRequest;
import com.example.macjava.rest.user.dto.UserResponse;
import com.example.macjava.rest.user.models.User;
import com.example.macjava.rest.user.service.UsersService;
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
@Tag(name = "Usuarios", description = "Endpoint usuarios de la tienda")
public class UsersRestController {
    private final UsersService usersService;
    private final PedidosService pedidosService;
    @Autowired
    public UsersRestController(UsersService usersService, PedidosService pedidosService) {
        this.usersService = usersService;
        this.pedidosService = pedidosService;
    }

    @Operation(summary = "Listar todos los usuarios", description = "Listar todos los usuarios")
    @Parameters({
         @Parameter(name = "username", description = "Filtrar por nombre de usuario"),
         @Parameter(name = "email", description = "Filtrar por email"),
         @Parameter(name = "isDeleted", description = "Filtrar por estado"),
         @Parameter(name = "page", description = "Paginación"),
         @Parameter(name = "size", description = "Tamaño de la paginación"),
         @Parameter(name = "sortBy", description = "Ordenar por"),
         @Parameter(name = "direction", description = "Dirección")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagina de usuarios")
    })
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

    @Operation(summary = "Listar usuario por ID", description = "Listar usuario por ID")
    @Parameters({
            @Parameter(name = "id", description = "ID del usuario")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<UserInfoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.findById(id));
    }
    @Operation(summary = "Crear usuario", description = "Crear usuario")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Usuario a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.save(userRequest));
    }
    @Operation(summary = "Actualizar usuario", description = "Actualizar usuario")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Usuario a actualizar", required = true)
    @Parameters({
            @Parameter(name = "id", description = "ID del usuario")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(usersService.update(id, userRequest));
    }

    @Operation(summary = "Eliminar usuario", description = "Eliminar usuario")
    @Parameters({
            @Parameter(name = "id", description = "ID del usuario")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Obtener usuario actual", description = "Obtener usuario actual")
    @Parameters({
            @Parameter(name = "user", description = "Usuario activo")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('USER')") // Solo los admin pueden acceder
    public ResponseEntity<UserInfoResponse> me(@AuthenticationPrincipal User user) {
        // Esta autenticado, por lo que devolvemos sus datos ya sabemos su id
        return ResponseEntity.ok(usersService.findById(user.getId()));
    }

    @Operation(summary = "Actualizar usuario actual", description = "Actualizar usuario actual")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Usuario a actualizar", required = true)
    @Parameters({
            @Parameter(name = "user", description = "Usuario activo")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<UserResponse> updateMe(@AuthenticationPrincipal User user, @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(usersService.update(user.getId(), userRequest));
    }

    @Operation(summary = "Eliminar usuario actual", description = "izar usuario actual")
    @Parameters({
            @Parameter(name = "user", description = "Usuario activo")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/me/profile")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal User user) {
        usersService.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener pedidos por usuario", description = "Obtener pedidos por usuario")
    @Parameters({
            @Parameter(name = "user", description = "Usuario activo"),
            @Parameter(name = "page", description = "Paginación"),
            @Parameter(name = "size", description = "Tamaño de la paginación"),
            @Parameter(name = "sortBy", description = "Ordenar por"),
            @Parameter(name = "direction", description = "Dirección")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagina de pedidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
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

    @Operation(summary = "Obtener pedido por ID", description = "Obtener pedido por ID")
    @Parameters({
            @Parameter(name = "user", description = "Usuario activo"),
            @Parameter(name = "id", description = "ID del pedido")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagina de pedidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
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

    @Operation(summary = "Crear pedido", description = "Crear pedido")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pedido a crear", required = true)
    @Parameters({
            @Parameter(name = "user", description = "Usuario activo")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/me/pedidos")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Pedido> savePedido(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody Pedido pedido
    ) {
        pedido.setIdUsuario(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidosService.save(pedido));
    }

    @Operation(summary = "Actualizar pedido", description = "Actualizar pedido")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pedido a actualizar", required = true)
    @Parameters({
            @Parameter(name = "user", description = "Usuario activo"),
            @Parameter(name = "id", description = "ID del pedido")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
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

    @Operation(summary = "Eliminar pedido", description = "Eliminar pedido")
    @Parameters({
            @Parameter(name = "user", description = "Usuario activo"),
            @Parameter(name = "id", description = "ID del pedido")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
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
