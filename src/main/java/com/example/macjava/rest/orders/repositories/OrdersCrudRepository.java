package com.example.macjava.rest.orders.repositories;

import com.example.macjava.rest.orders.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;

public interface OrdersCrudRepository extends MongoRepository<Order, ObjectId> {
    Page<Order> findByClientUUID (UUID clientUUID, Pageable pageable);
    Boolean existsByClientUUID (UUID clientUUID);
    Page<Order> findByWorkerUUID (UUID workerUUID, Pageable pageable);
    Boolean existsByWorkerUUID (UUID workerUUID);
    Page<Order> findByRestaurantId (Long restaurantId, Pageable pageable);
    Boolean existsByRestaurantId (Long restaurantId);
}
