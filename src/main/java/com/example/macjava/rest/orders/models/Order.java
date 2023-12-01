package com.example.macjava.rest.orders.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EntityListeners;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("order")
@TypeAlias("Order")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @Builder.Default
    private ObjectId id = new ObjectId();

    @NotNull(message = "El UUID del cliente no puede estar vacio")
    private UUID clientUUID;

    @NotNull(message = "El UUID del cliente no puede estar vacio")
    private UUID workerUUID;

    @NotNull(message = "El id del usuario no puede ser nulo")
    private Long restaurantId;

    @NotNull(message = "El pedido debe tener al menos una línea de pedido")
    private List<@Valid OrderedProduct> orderedProducts;

    @Builder.Default
    private Double totalPrice = 0.0;

    @Builder.Default
    private Integer totalQuantityProducts = 0;

    @Builder.Default
    private Boolean isPaid = false;

    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default
    private Boolean isDeleted = false;

    @JsonProperty("id")
    public String getId() {
        return id.toHexString();
    }

    /**
     *  Establece la lista de productos pedidos
     * @param orderedProducts lista de productos pedidos
     */

    public void setOrderedProducts(List<@Valid OrderedProduct> orderedProducts){
        this.orderedProducts= orderedProducts;
        this.totalPrice = orderedProducts.stream().mapToDouble(OrderedProduct::getTotalPrice).sum();
        this.totalQuantityProducts = orderedProducts.stream().mapToInt(OrderedProduct::getQuantity).sum();
    }
}
