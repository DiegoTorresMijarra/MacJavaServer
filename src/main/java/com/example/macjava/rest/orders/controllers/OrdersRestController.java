package com.example.macjava.rest.orders.controllers;

import com.example.macjava.rest.orders.dto.OrderSaveDto;
import com.example.macjava.rest.orders.dto.OrderUpdateDto;
import com.example.macjava.rest.orders.models.Order;
import com.example.macjava.rest.orders.services.OrdersServiceImpl;
import com.example.macjava.rest.user.models.Role;
import com.example.macjava.rest.user.models.User;
import com.example.macjava.utils.pagination.PageResponse;
import com.example.macjava.utils.pagination.PaginationLinksUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controlador para los pedidos
 */
@RestController
@RequestMapping("${api.version}/orders")
@Slf4j
@PreAuthorize("hasRole('USER')")
public class OrdersRestController {
    private final OrdersServiceImpl ordersService;
    private final PaginationLinksUtils paginationLinksUtils;

    public OrdersRestController(OrdersServiceImpl ordersService, PaginationLinksUtils paginationLinksUtils) {
        this.ordersService = ordersService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Obtiene todos los pedidos
     * debe ser ADMIN quien realice la petición
     * @return ResponseEntity con la lista de pedidos
     */
    @Operation(summary = "Obtiene todos los pedidos", description = "Obtiene todos los pedidos en una lista")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de pedidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tiene permisos para realizar la operación"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se ha encontrado el pedido")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listAll")
    public ResponseEntity<List<Order>> findAll() {
        log.info("Obteniendo todos los pedidos");
        return ResponseEntity.ok(ordersService.findAll());
    }

    /**
     * Obtiene todos los pedidos pàginados debe tener el rol ADMIN
     * @param page numero de pagina
     * @param size tamaño de la pagina
     * @param sortBy campo por el que se ordena
     * @param direction direccion de la ordenacion
     * @param request
     * @return ResponseEntity con la lista de pedidos
     */
    @Operation(summary = "Obtiene todos los pedidos paginados", description = "Obtiene todos los pedidos en una lista paginada")
    @Parameters({
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo por el que se ordena", example = "id"),
            @Parameter(name = "direction", description = "Dirección de la ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de pedidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tiene permisos para realizar la operación"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se ha encontrado el pedido")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<PageResponse<Order>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Obteniendo todos los pedidos pageados");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        Page<Order> pageResult = ordersService.findAll(PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Obtiene un pedido por su id
     * @param idPedido
     * @return ResponseEntity con el pedido
     */
    @Operation(description = "Obtiene un pedido por su id")
    @Parameter(name = "idPedido", description = "Id del pedido que se quiere buscar", example = "60f0b0b9e3b9f83f7c7e3b9f")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el pedido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") ObjectId idPedido) {
        log.info("Obteniendo pedido con id: " + idPedido);
        return ResponseEntity.ok(ordersService.findById(idPedido));
    }

    /**
     * Permite al usuario(USER) crear un pedido, pero este debe estar creado obligatoriamente bajo su UUID. Si no, devuelve un HttpStatus.FORBIDDEN
     * @param order OrderSaveDto a crear
     * @param user User que crea el pedido
     * @return ResponseEntity con el pedido creado, o forbidden si no tiene el uuid correcto
     */
    @Operation(description = "Crea un pedido asociado a un usuario")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pedido a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar la operación"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
    })
    @Transactional
    @PostMapping("/orders")
    public ResponseEntity<Order> saveOrder(@RequestBody @Valid OrderSaveDto order,
                                           @AuthenticationPrincipal User user
    ){
        if(user==null||user.getId()!=order.getWorkerUUID()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        log.info("Guardando pedido:{}", order);
        return ResponseEntity.status(HttpStatus.CREATED).body(ordersService.save(order));
    }
    /**
     * Permite al Administrador(ADMIN) crear un pedido, bajo la titularidad de cualquier otro empleado. <br>
     * En un futuro, y si se añade un rol de SUPERADMIN, podria limitarse por restaurante, subordinados...
     * @param order OrderSaveDto a crear
     * @return ResponseEntity con el pedido creado
     */
    @Operation(description = "Almacena un pedido asociado a un usuario ADMIN")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pedido a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar la operación"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @PostMapping("/ordersAdmin")
    public ResponseEntity<Order> saveOrderAdmin(@RequestBody @Valid OrderSaveDto order){
        log.info("Guardando pedido:{}", order);
        return ResponseEntity.status(HttpStatus.CREATED).body(ordersService.save(order));
    }

    /**
     * Borra un pedido por su id, solo puede hacerlo el ADMIN
     * @param idPedido id del pedido a borrar
     * @return ResponseEntity con el pedido borrado
     */
    @Operation(description = "Borra un pedido por su id, borrado por un administrador")
    @Parameter(name = "idPedido", description = "Id del pedido que se quiere borrar", example = "60f0b0b9e3b9f83f7c7e3b9f")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido borrado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar la operación"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el pedido")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable("id") ObjectId idPedido) {
        log.info("Borrando el pedido con id: " + idPedido);
        ordersService.deleteById(idPedido);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza un pedido por su id, solo puede hacerlo el ADMIN
     * @param idPedido id del pedido a actualizar
     * @param dto OrderUpdateDto con los datos a actualizar
     * @return ResponseEntity con el pedido actualizado
     */
    @Operation(description = "Actualiza un pedido por su id, actualizado por un administrador")
    @Parameter(name = "idPedido", description = "Id del pedido que se quiere actualizar", example = "60f0b0b9e3b9f83f7c7e3b9f")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del pedido a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar la operación"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el pedido"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @PutMapping("{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") ObjectId idPedido, @RequestBody @Valid OrderUpdateDto dto){
        log.info("Actualizando pedido con id: " + idPedido.toHexString());

        return ResponseEntity.ok(ordersService.updateOrder(idPedido,dto));
    }

    /**
     * Busca si existe algun pedido del cliente con el uuid dado
     * @param clientUUID uuid del cliente
     * @return ResponseEntity con un booleano
     */
    @Operation(description = "Busca si existe algun pedido del cliente con el uuid dado")
    @Parameter(name = "clientUUID", description = "UUID del cliente", example = "60f0b0b9e3b9f83f7c7e3b9f")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el pedido")
    })
    @GetMapping("/clientExists/{id}")
    public ResponseEntity<Boolean> existsByClientUUID(@PathVariable("id") UUID clientUUID){
        log.info("Buscando si existe algun pedido del cliente con uuid: " + clientUUID);
        return ResponseEntity.ok(ordersService.existsByClientUUID(clientUUID));
    }

    /**
     * Busca los pedidos del cliente con el uuid dado
     * @param clientUUID uuid del cliente
     * @param page numero de pagina
     * @param size tamaño de la pagina
     * @param sortBy campo por el que se ordena
     * @param direction direccion de la ordenacion
     * @param request
     * @return ResponseEntity con la lista de pedidos del cliente
     */
    @Operation(description = "Busca los pedidos del cliente con el uuid dado")
    @Parameter(name = "clientUUID", description = "UUID del cliente", example = "60f0b0b9e3b9f83f7c7e3b9f")
    @Parameters({
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Número de elementos por página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo por el que se ordena", example = "id"),
            @Parameter(name = "direction", description = "Dirección de la ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el pedido")
    })
    @GetMapping("/client/{id}")
    public ResponseEntity<PageResponse<Order>> findByClientUUID(@PathVariable("id") UUID clientUUID,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                        @RequestParam(defaultValue = "asc") String direction,
                                                        HttpServletRequest request){
        log.info("Buscando los pedido pageados del cliente con uuid: " + clientUUID);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        Page<Order> pageResult = ordersService.findByClientUUID(clientUUID,PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }
    /*

    /**
     * Busca si existe algun pedido del Trabajador con el uuid dado
     * @param workerUUID uuid del trabajador
     * @param user User que realiza la peticion
     * @return ResponseEntity con un booleano
     */
    @Operation(description = "Busca si existe algun pedido del Trabajador con el uuid dado")
    @Parameter(name = "workerUUID", description = "UUID del trabajador", example = "60f0b0b9e3b9f83f7c7e3b9f")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar la operación"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el pedido")
    })
    @GetMapping("/workerExists/{id}")
    public ResponseEntity<Boolean> existsByWorkerUUID(@PathVariable("id") UUID workerUUID,
                                                      @AuthenticationPrincipal User user){
        if(user.getId()!=workerUUID){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        log.info("Buscando si existe algun pedido del Trabajador con uuid: " + workerUUID);
        return ResponseEntity.ok(ordersService.existsByWorkerUUID(workerUUID));
    }

    /**
     * Busca los pedidos del Trabajador con el uuid dado
     * @param workerUUID uuid del trabajador
     * @param page numero de pagina
     * @param size tamaño de la pagina
     * @param sortBy campo por el que se ordena
     * @param direction direccion de la ordenacion
     * @param request
     * @param user User que realiza la peticion
     * @return ResponseEntity con la lista de pedidos del trabajador
     */
    @Operation(description = "Busca los pedidos del Trabajador con el uuid dado")
    @Parameter(name = "workerUUID", description = "UUID del trabajador", example = "60f0b0b9e3b9f83f7c7e3b9f")
    @Parameters({
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Número de elementos por página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo por el que se ordena", example = "id"),
            @Parameter(name = "direction", description = "Dirección de la ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar la operación"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el pedido")
    })
    @GetMapping("/worker/{id}")
    public ResponseEntity<PageResponse<Order>> findWorkerUUID(@PathVariable("id") UUID workerUUID,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(defaultValue = "id") String sortBy,
                                                              @RequestParam(defaultValue = "asc") String direction,
                                                              HttpServletRequest request,
                                                              @AuthenticationPrincipal User user){
        if(user.getId()!=workerUUID){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        log.info("Buscando los pedido pageados del Trabajador con uuid: " + workerUUID);

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        Page<Order> pageResult = ordersService.findByWorkerUUID(workerUUID,PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

     */

    @GetMapping("/restaurantExists/{id}")
    public ResponseEntity<Boolean> existsByRestaurantId(@PathVariable("id") Long clientId){
        log.info("Buscando si existe algun pedido del cliente con uuid: " + clientId);
        return ResponseEntity.ok(ordersService.existsByRestaurantId(clientId));
    }

    /**
     * Busca los pedidos del restaurante con el id dado
     * @param clientId id del restaurante
     * @param page numero de pagina
     * @param size tamaño de la pagina
     * @param sortBy campo por el que se ordena
     * @param direction direccion de la ordenacion
     * @param request
     * @return ResponseEntity con la lista de pedidos del restaurante
     */
    @Operation(description = "Busca los pedidos del restaurante con el id dado")
    @Parameter(name = "clientId", description = "Id del restaurante", example = "1")
    @Parameters({
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Número de elementos por página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo por el que se ordena", example = "id"),
            @Parameter(name = "direction", description = "Dirección de la ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el pedido")
    })
    @GetMapping("/restaurant/{id}")
    public ResponseEntity<PageResponse<Order>> findByRestaurantId(@PathVariable("id") Long clientId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(defaultValue = "id") String sortBy,
                                                                @RequestParam(defaultValue = "asc") String direction,
                                                                HttpServletRequest request){
        log.info("Buscando los pedido pageados del cliente con uuid: " + clientId);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        Page<Order> pageResult = ordersService.findByRestaurantId(clientId,PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Actualiza el isPaid del pedido con el id dado, solo puede hacerlo el ADMIN
     * @param objectId id del pedido
     * @param isPaid indica si el pedido ha sido pagado
     * @return ResponseEntity con el pedido actualizado
     */
    @Operation(description = "Actualiza un pedido con el id de este, indica si ha sido pagado")
    @Parameters({
            @Parameter(name = "id", description = "Id del pedido", example = "60f0b0b9e3b9f83f7c7e3b9f"),
            @Parameter(name = "isPaid", description = "Indica si el pedido ha sido pagado", example = "true")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el pedido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar la operación")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @PutMapping ("/isPaid/{id}")
    public ResponseEntity<Order> updateIsPaidById(
            @PathVariable (value = "id")  ObjectId objectId,
            @RequestParam  (value = "isPaid",required = true) Boolean isPaid){
        log.info("Actualizando isPaid del pedido con id: "+objectId.toHexString()+" a "+ isPaid);
        return ResponseEntity.ok(ordersService.updateIsPaidById(objectId, isPaid));
    }

    /**
     * Actualiza el isDeleted del pedido con el id dado, solo puede hacerlo el ADMIN
     * @param objectId id del pedido
     * @param isDeleted indica si el pedido va ha ser borrado
     * @return ResponseEntity con el pedido actualizado
     */
    @Operation(description = "Actualiza un pedido con el id de este, indica si ha sido borrado")
    @Parameters({
            @Parameter(name = "id", description = "Id del pedido", example = "60f0b0b9e3b9f83f7c7e3b9f"),
            @Parameter(name = "isDeleted", description = "Indica si el pedido ha sido borrado", example = "true")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el pedido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar la operación")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @PutMapping ("/isDeleted/{id}")
    public ResponseEntity<Order> updateisDeletedById(
            @PathVariable (value = "id")  ObjectId objectId,
            @RequestParam  (value = "isDeleted",required = true) Boolean isDeleted){
        log.info("Actualizando isPaid del pedido con id: "+objectId.toHexString()+" a "+ isDeleted);
        return ResponseEntity.ok(ordersService.updateIsDeletedById(objectId, isDeleted));
    }

    /**
     * Manejador de excepciones de Validación: 400 Bad Request
     * @param ex excepción
     * @return Mapa de errores de validación con el campo y el mensaje
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
