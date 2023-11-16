package com.example.macjava.products.services;

import com.example.macjava.products.dto.ProductdtoNew;
import com.example.macjava.products.dto.ProductdtoUpdate;
import com.example.macjava.products.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    Page<Product> findAll(Optional<String> nombre, Optional<Integer> stockMax, Optional<Integer> stockMin, Optional<Double> precioMax, Optional<Double> precioMin , Optional<Boolean> gluten, Optional<Boolean> is_deleted, Pageable pageable);
   Product findById(Long id);
   Product save(ProductdtoNew productdtoNew);
   Product update (Long id,ProductdtoUpdate productdtoUpdate);
   void deleteById(Long id);
}
