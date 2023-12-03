package com.example.macjava.rest.restaurants.mapper;

import com.example.macjava.rest.restaurants.dto.NewRestaurantDTO;
import com.example.macjava.rest.restaurants.dto.UpdatedRestaurantDTO;
import com.example.macjava.rest.restaurants.modelos.Restaurant;

import java.time.LocalDate;
/**
 * Clase que mapea los datos de un restaurante
 */
public class RestaurantMapper {
    /**
     * Mapea los datos de un restaurante nuevo a un restaurante
     * @param restau Datos del restaurante nuevo
     * @return
     */
    public Restaurant toRestaurantNew(NewRestaurantDTO restau){
        return Restaurant.builder()
                .name(restau.getName())
                .number(restau.getNumber())
                .creationD(LocalDate.now())
                .modificationD(LocalDate.now())
                .build();
    }

    /**
     * Mapea los datos de un restaurante actualizado a un restaurante
     * @param restaudto Datos del restaurante actualizado
     * @param realRestau   Datos del restaurante actualizado
     * @return Devuelve un restaurante
     */
    public Restaurant toRestaurantUpdate (UpdatedRestaurantDTO restaudto , Restaurant realRestau){
        return Restaurant.builder()
                .id(realRestau.getId())
                .name(restaudto.getName())
                .number(restaudto.getNumber())
                .isDeleted(restaudto.isDeleted())
                .creationD(realRestau.getCreationD())
                .modificationD(LocalDate.now())
                .build();
    }
}
