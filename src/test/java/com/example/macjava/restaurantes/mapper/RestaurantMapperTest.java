package com.example.macjava.restaurantes.mapper;


import com.example.macjava.restaurantes.dto.NewRestaurantDTO;
import com.example.macjava.restaurantes.dto.UpdatedRestaurantDTO;
import com.example.macjava.restaurantes.modelos.Restaurante;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantMapperTest {
    private final RestaurantMapper restaurantMapperUnderTest = new RestaurantMapper();

    @Test
    void toRestaurant() {
        // Setup
        final NewRestaurantDTO restaurantDTO = new NewRestaurantDTO();
        restaurantDTO.setName("name");
        restaurantDTO.setNumber("number");

        // Run the test
        final Restaurante result = restaurantMapperUnderTest.toRestaurantNew(restaurantDTO);

        // Verify the results
        assertAll("toRestaurant",
                () -> assertEquals("name", result.getName()),
                () -> assertEquals("number", result.getNumber())
        );
    }

    @Test
    void toRestaurantUpdate(){
        final UpdatedRestaurantDTO restaurantDTO = new UpdatedRestaurantDTO();
        restaurantDTO.setName("name");
        restaurantDTO.setNumber("number");
        restaurantDTO.setDeleted(false);

        final Restaurante restaurant = new Restaurante();
        restaurant.setId(1L);
        restaurant.setName("name");
        restaurant.setNumber("number");
        restaurant.setDeleted(false);

        final Restaurante result = restaurantMapperUnderTest.toRestaurantUpdate(restaurantDTO, restaurant);

        assertAll("toRestaurantUpdate",
                () -> assertEquals("name", result.getName()),
                () -> assertEquals("number", result.getNumber()),
                () -> assertFalse(result.isDeleted())
        );
    }
}
