package com.example.macjava.rest.orders.dto;

import com.example.macjava.rest.orders.models.OrderedProduct;

import java.util.List;
import java.util.UUID;

/**
 * Interfaz para los pedidos
 */
public interface OrderType {
    UUID getClientUUID();
    UUID getWorkerUUID();
    Long getRestaurantId();
    List<OrderedProduct> getOrderedProducts();
}
