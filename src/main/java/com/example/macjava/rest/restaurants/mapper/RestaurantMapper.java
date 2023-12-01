package com.example.macjava.rest.restaurants.mapper;

import com.example.macjava.rest.restaurants.dto.NewRestaurantDTO;
import com.example.macjava.rest.restaurants.dto.UpdatedRestaurantDTO;
import com.example.macjava.rest.restaurants.modelos.Restaurant;

import java.time.LocalDate;

public class RestaurantMapper {

    public Restaurant toRestaurantNew(NewRestaurantDTO restau){
        return Restaurant.builder()
                .name(restau.getName())
                .number(restau.getNumber())
                .creationD(LocalDate.now())
                .modificationD(LocalDate.now())
                .build();
    }

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
