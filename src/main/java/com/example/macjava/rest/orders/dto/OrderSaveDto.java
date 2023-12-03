package com.example.macjava.rest.orders.dto;

import com.example.macjava.rest.orders.models.OrderedProduct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Modelo de datos para ALMACENAR los pedidos
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSaveDto implements OrderType{
    @NotNull(message = "El UUID del cliente no puede ser null")
    private UUID clientUUID;

    @NotNull(message = "El UUID del cliente no puede estar vacio")
    private UUID workerUUID;

    @NotNull(message = "El id del usuario no puede ser nulo")
    private Long restaurantId;

    @NotNull(message = "El pedido debe tener al menos una línea de pedido")
    private List<@Valid OrderedProduct> orderedProducts;
}
