package com.example.macjava.rest.orders.controllers;

import com.example.macjava.rest.orders.dto.OrderSaveDto;
import com.example.macjava.rest.orders.dto.OrderUpdateDto;
import com.example.macjava.rest.orders.models.Order;
import com.example.macjava.rest.orders.services.OrdersServiceImpl;
import com.example.macjava.utils.pagination.PageResponse;
import com.example.macjava.utils.pagination.PaginationLinksUtils;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("${api.version}/orders")
@Slf4j
//@PreAuthorize("hasRole('ADMIN')")
public class OrdersRestController {
    private final OrdersServiceImpl ordersService;
    private final PaginationLinksUtils paginationLinksUtils;

    public OrdersRestController(OrdersServiceImpl ordersService, PaginationLinksUtils paginationLinksUtils) {
        this.ordersService = ordersService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Order>> findAll() {
        log.info("Obteniendo todos los pedidos");
        return ResponseEntity.ok(ordersService.findAll());
    }
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
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") ObjectId idPedido) {
        log.info("Obteniendo pedido con id: " + idPedido);
        return ResponseEntity.ok(ordersService.findById(idPedido));
    }

    @Transactional
    @PostMapping("/orders")
    public ResponseEntity<Order> saveOrder(@RequestBody @Valid OrderSaveDto order){
        log.info("Guardando pedido:{}", order);
        return ResponseEntity.status(HttpStatus.CREATED).body(ordersService.save(order));
    }
    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable("id") ObjectId idPedido) {
        log.info("Borrando el pedido con id: " + idPedido);
        ordersService.deleteById(idPedido);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PutMapping("{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") ObjectId idPedido, @RequestBody @Valid OrderUpdateDto dto){
        log.info("Actualizando pedido con id: " + idPedido.toHexString());

        return ResponseEntity.ok(ordersService.updateOrder(idPedido,dto));
    }
    @GetMapping("/clientExists/{id}")
    public ResponseEntity<Boolean> existsByClientUUID(@PathVariable("id") UUID clientUUID){
        log.info("Buscando si existe algun pedido del cliente con uuid: " + clientUUID);
        return ResponseEntity.ok(ordersService.existsByClientUUID(clientUUID));
    }
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
    @GetMapping("/workerExists/{id}")
    public ResponseEntity<Boolean> existsByWorkerUUID(@PathVariable("id") UUID workerUUID){
        log.info("Buscando si existe algun pedido del Trabajador con uuid: " + workerUUID);
        return ResponseEntity.ok(ordersService.existsByWorkerUUID(workerUUID));
    }
    @GetMapping("/worker/{id}")
    public ResponseEntity<PageResponse<Order>> findWorkerUUID(@PathVariable("id") UUID workerUUID,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(defaultValue = "id") String sortBy,
                                                                @RequestParam(defaultValue = "asc") String direction,
                                                                HttpServletRequest request){
        log.info("Buscando los pedido pageados del Trabajador con uuid: " + workerUUID);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        Page<Order> pageResult = ordersService.findByWorkerUUID(workerUUID,PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }
    @GetMapping("/restaurantExists/{id}")
    public ResponseEntity<Boolean> existsByRestaurantId(@PathVariable("id") Long clientId){
        log.info("Buscando si existe algun pedido del cliente con uuid: " + clientId);
        return ResponseEntity.ok(ordersService.existsByRestaurantId(clientId));
    }
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

    @Transactional
    @PutMapping ("/isPaid/{id}")
    public ResponseEntity<Order> updateIsPaidById(
            @PathVariable (value = "id")  ObjectId objectId,
            @RequestParam  (value = "isPaid",required = true) Boolean isPaid){
        log.info("Actualizando isPaid del pedido con id: "+objectId.toHexString()+" a "+ isPaid);
        return ResponseEntity.ok(ordersService.updateIsPaidById(objectId, isPaid));
    }

    @Transactional
    @PutMapping ("/isDeleted/{id}")
    public ResponseEntity<Order> updateisDeletedById(
            @PathVariable (value = "id")  ObjectId objectId,
            @RequestParam  (value = "isDeleted",required = true) Boolean isDeleted){
        log.info("Actualizando isPaid del pedido con id: "+objectId.toHexString()+" a "+ isDeleted);
        return ResponseEntity.ok(ordersService.updateIsDeletedById(objectId, isDeleted));
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
