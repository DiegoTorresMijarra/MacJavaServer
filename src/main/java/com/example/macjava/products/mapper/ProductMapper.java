package com.example.macjava.products.mapper;

import com.example.macjava.products.dto.ProductdtoNew;
import com.example.macjava.products.dto.ProductdtoUpdate;
import com.example.macjava.products.models.Product;

import java.time.LocalDate;

public class ProductMapper {
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
