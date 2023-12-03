package com.example.macjava.rest.orders.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de datos para los productos de un pedido
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderedProduct {
    @Min(value = 1, message = "La cantidad del producto no puede ser negativa")
    @Builder.Default
    private Integer quantity = 1;
    @Min(value = 1, message = "El id del producto debe ser mayor de 1")
    private Long productId;
    @Positive(message = "El precio del producto no puede ser negativo")
    @Builder.Default
    private double  productPrice= 1.0;
    @Builder.Default
    private double totalPrice = 1.0;

    /**
     * Establece la cantidad del producto y actualiza el precio total del pedido
     * @param quantity nueva cantidad
     */
    public void setQuantity(@Min(value = 0) Integer quantity) {
        this.quantity = quantity;
        this.calculateTotalPrice();
    }

    /**
     * Establece el precio del producto y actualiza el total
     * @param productPrice product price
     */
    public void setProductPrice(@Positive Double productPrice){
        this.productPrice = productPrice;
        this.calculateTotalPrice();
    }

    /**
     * Calcula el precio total del producto
     */
    public void calculateTotalPrice (){
        setTotalPrice(productPrice * quantity);
    }
}
