package com.example.macjava.rest.orders.dto;

import com.example.macjava.rest.orders.models.OrderedProduct;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderUpdateDto implements OrderType{
    private UUID clientUUID;
    private UUID workerUUID;
    private Long restaurantId;
    private List<@Valid OrderedProduct> orderedProducts;
}
