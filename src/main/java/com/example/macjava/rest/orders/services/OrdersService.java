package com.example.macjava.rest.orders.services;

import com.example.macjava.rest.orders.dto.OrderSaveDto;
import com.example.macjava.rest.orders.dto.OrderUpdateDto;
import com.example.macjava.rest.orders.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Servicio para los pedidos
 */
public interface OrdersService {
    List<Order> findAll();
    Page<Order> findAll(Pageable pageable);
    Order findById(ObjectId objectId);
    Order save(OrderSaveDto order);
    void deleteById(ObjectId objectId);
    Order updateOrder(ObjectId objectId, OrderUpdateDto dto);
    Page<Order> findByClientUUID (UUID clientUUID, Pageable pageable);
    Boolean existsByClientUUID (UUID clientUUID);
    Page<Order> findByWorkerUUID (UUID workerUUID, Pageable pageable);
    Boolean existsByWorkerUUID (UUID workerUUID);
    Page<Order> findByRestaurantId (Long restaurantId, Pageable pageable);
    Boolean existsByRestaurantId (Long restaurantId);
    Order updateIsPaidById(ObjectId objectId, Boolean isPaid);

    Order updateIsDeletedById(ObjectId objectId, Boolean isPaid);
}
