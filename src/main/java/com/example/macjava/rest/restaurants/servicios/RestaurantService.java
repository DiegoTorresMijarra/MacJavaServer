package com.example.macjava.rest.restaurants.servicios;

import com.example.macjava.rest.restaurants.dto.NewRestaurantDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.macjava.rest.restaurants.dto.UpdatedRestaurantDTO;
import com.example.macjava.rest.restaurants.modelos.Restaurant;

import java.util.Optional;

public interface RestaurantService {
    Page<Restaurant> findAll(Optional<String> name, Optional<String> number, Optional<Boolean> isDeleted, Pageable page);
    Restaurant findById(Long id);
    Restaurant save (NewRestaurantDTO restau);
    Restaurant update (Long id, UpdatedRestaurantDTO restau);
    void deleteById(Long id);
}
