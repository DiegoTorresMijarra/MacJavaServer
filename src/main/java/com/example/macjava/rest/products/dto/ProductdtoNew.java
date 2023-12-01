package com.example.macjava.rest.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductdtoNew {
    @Schema(description = "Nombre del producto", example = "Coca Cola")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @Schema(description = "Precio del producto", example = "12.0")
    @Positive(message = "El precio no puede ser negativo")
    @NotNull(message = "El precio no puede estar vacío")
    private double precio;
    @Schema(description = "Stock del producto", example = "10")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    @NotNull(message = "El stock no puede estar vacío")
    private Integer stock;
    @Builder.Default
    @Schema(description = "Gluten del producto" , example = "true")
    private boolean gluten = true;
}
