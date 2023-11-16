package com.example.macjava.services;

import com.example.macjava.dto.ProductdtoNew;
import com.example.macjava.dto.ProductdtoUpdate;
import com.example.macjava.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    Page<Product> findAll(Optional<String> nombre,Optional<Integer> stockMax,Optional<Integer> stockMin,Optional<Double> precio, Optional<Double> precioMax, Optional<Double> precioMin ,Optional<Boolean> gluten,Optional<Boolean> is_deleted,Pageable pageable);
   Product findById(Long id);
   Product save(ProductdtoNew productdtoNew);
   Product update (Long id,ProductdtoUpdate productdtoUpdate);
   void deleteById(Long id);
}
