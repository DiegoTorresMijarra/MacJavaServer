package com.example.macjava.restaurantes.servicios;

import com.example.macjava.restaurantes.dto.NewRestaurantDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.macjava.restaurantes.dto.UpdatedRestaurantDTO;
import com.example.macjava.restaurantes.modelos.Restaurante;

import java.util.Optional;

public interface RestaurantService {
    Page<Restaurante> findAll(Optional<String> name, Optional<String> number, Optional<Boolean> isDeleted, Pageable page);
    Restaurante findById(Long id);
    Restaurante save (NewRestaurantDTO restau);
    Restaurante update (Long id, UpdatedRestaurantDTO restau);
    void deleteById(Long id);
}
