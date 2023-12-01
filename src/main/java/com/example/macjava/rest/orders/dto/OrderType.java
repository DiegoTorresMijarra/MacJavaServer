package com.example.macjava.rest.orders.dto;

import com.example.macjava.rest.orders.models.OrderedProduct;

import java.util.List;
import java.util.UUID;

public interface OrderType {
    public final UUID NO_UUID=UUID.fromString("00000000-0000-0000-0000-000000000000");
    public final Long NO_LONG=-1L;
    UUID getClientUUID();
    UUID getWorkerUUID();
    Long getRestaurantId();
    List<OrderedProduct> getOrderedProducts();
}
