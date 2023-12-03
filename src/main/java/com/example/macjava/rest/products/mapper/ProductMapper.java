package com.example.macjava.rest.products.mapper;

import com.example.macjava.rest.products.dto.ProductdtoNew;
import com.example.macjava.rest.products.dto.ProductdtoUpdate;
import com.example.macjava.rest.products.models.Product;

import java.time.LocalDate;

/**
 * Mapeo de un producto
 */
public class ProductMapper {
    /**
     * Mapeo de un producto nuevo
     * @param productdtoNew Producto nuevo
     * @return Producto
     */
    public Product toProductNew(ProductdtoNew productdtoNew) {
        return Product.builder()
                .nombre(productdtoNew.getNombre())
                .stock(productdtoNew.getStock())
                .precio(productdtoNew.getPrecio())
                .gluten(productdtoNew.isGluten())
                .fecha_act(LocalDate.now())
                .fecha_cre(LocalDate.now())
                .build();

    }
    /**
     * Mapeo de un producto actualizado
     * @param productdtoUpdate Producto actualizado
     * @param product Producto
     * @return Producto
     */
    public Product toProductUpdate(ProductdtoUpdate productdtoUpdate, Product product){
        return Product.builder()
                .id(product.getId())
                .nombre(productdtoUpdate.getNombre())
                .stock(productdtoUpdate.getStock())
                .precio(productdtoUpdate.getPrecio())
                .gluten(productdtoUpdate.isGluten())
                .is_deleted(productdtoUpdate.is_deleted())
                .fecha_cre(product.getFecha_cre())
                .fecha_act(LocalDate.now())
                .build();
    }
}
