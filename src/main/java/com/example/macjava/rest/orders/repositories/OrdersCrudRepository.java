package com.example.macjava.rest.orders.repositories;

import com.example.macjava.rest.orders.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;

/**
 * Repositorio para los pedidos
 */
public interface OrdersCrudRepository extends MongoRepository<Order, ObjectId> {

    /**
     * Busca un pedido por el id del cliente
     * @param clientUUID id del cliente
     * @param pageable paginación
     * @return pedido encontrado
     */
    Page<Order> findByClientUUID (UUID clientUUID, Pageable pageable);
    Boolean existsByClientUUID (UUID clientUUID);

    /**
     * Busca un pedido por el id del trabajador
     * @param workerUUID id del trabajador
     * @param pageable paginación
     * @return pedido encontrado
     */
    Page<Order> findByWorkerUUID (UUID workerUUID, Pageable pageable);
    Boolean existsByWorkerUUID (UUID workerUUID);
    Page<Order> findByRestaurantId (Long restaurantId, Pageable pageable);
    Boolean existsByRestaurantId (Long restaurantId);
}
