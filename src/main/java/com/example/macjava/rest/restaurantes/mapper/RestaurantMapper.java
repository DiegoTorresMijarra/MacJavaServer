package com.example.macjava.rest.restaurantes.mapper;

import com.example.macjava.rest.restaurantes.dto.NewRestaurantDTO;
import com.example.macjava.rest.restaurantes.dto.UpdatedRestaurantDTO;
import com.example.macjava.rest.restaurantes.modelos.Restaurante;

import java.time.LocalDate;

public class RestaurantMapper {

    public Restaurante toRestaurantNew(NewRestaurantDTO restau){
        return Restaurante.builder()
                .name(restau.getName())
                .number(restau.getNumber())
                .creationD(LocalDate.now())
                .modificationD(LocalDate.now())
                .build();
    }

    public Restaurante toRestaurantUpdate (UpdatedRestaurantDTO restaudto , Restaurante realRestau){
        return Restaurante.builder()
                .id(realRestau.getId())
                .name(restaudto.getName())
                .number(restaudto.getNumber())
                .isDeleted(restaudto.isDeleted())
                .creationD(realRestau.getCreationD())
                .modificationD(LocalDate.now())
                .build();
    }
}
