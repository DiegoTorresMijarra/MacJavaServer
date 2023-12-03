package com.example.macjava.rest.restaurants.mapper;


import com.example.macjava.rest.restaurants.dto.NewRestaurantDTO;
import com.example.macjava.rest.restaurants.dto.UpdatedRestaurantDTO;
import com.example.macjava.rest.restaurants.modelos.Restaurant;
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
        final Restaurant result = restaurantMapperUnderTest.toRestaurantNew(restaurantDTO);

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

        final Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("name");
        restaurant.setNumber("number");
        restaurant.setDeleted(false);

        final Restaurant result = restaurantMapperUnderTest.toRestaurantUpdate(restaurantDTO, restaurant);

        assertAll("toRestaurantUpdate",
                () -> assertEquals("name", result.getName()),
                () -> assertEquals("number", result.getNumber()),
                () -> assertFalse(result.isDeleted())
        );
    }
}
