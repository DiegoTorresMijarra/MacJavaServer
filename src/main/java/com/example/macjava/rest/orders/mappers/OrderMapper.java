package com.example.macjava.rest.orders.mappers;

import com.example.macjava.rest.orders.dto.OrderSaveDto;
import com.example.macjava.rest.orders.dto.OrderUpdateDto;
import com.example.macjava.rest.orders.models.Order;

/**
 * Mapeador de pedidos
 */
public class OrderMapper {
    public static Order saveToModel(OrderSaveDto dto) {
        Order res= Order.builder()
                .clientUUID(dto.getClientUUID())
                .workerUUID(dto.getWorkerUUID())
                .restaurantId(dto.getRestaurantId())
                .build();
        res.setOrderedProducts(dto.getOrderedProducts());
        return res;
    }

    /**
     * Mapea el updatedto a un order <br>
     * Si el original es null, se crea uno nuevo <br>
     * IMPORTANTE: no se debe mandar una lista vacia o incorrecta, hacer la verificacion antes y pasar un null, en ese caso para que mantenga la original
     * @param original Order Original
     * @param dto OrderUpdateDto
     * @return Order resultante de los valores nuevos pasados y los viejos que se mantengan
     */
    public static Order updateToModel(Order original, OrderUpdateDto dto) {
        return Order.builder()
                .clientUUID(dto.getClientUUID()==null?original.getClientUUID():dto.getClientUUID())
                .workerUUID(dto.getWorkerUUID()==null?original.getWorkerUUID():dto.getWorkerUUID())
                .restaurantId(dto.getRestaurantId()==null?original.getRestaurantId():dto.getRestaurantId())
                .orderedProducts(dto.getOrderedProducts()==null?original.getOrderedProducts():dto.getOrderedProducts())
                .build();
    }
}
