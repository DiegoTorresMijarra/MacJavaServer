package restaurantes.mapper;


import org.junit.jupiter.api.Test;
import restaurantes.dto.NewRestaurantDTO;
import restaurantes.dto.UpdatedRestaurantDTO;
import restaurantes.models.Restaurant;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestaurantMapperTest {
    private final RestaurantMapper restaurantMapperUnderTest = new RestaurantMapper();

    @Test
    void toRestaurant() {
        // Setup
        final NewRestaurantDTO restaurantDTO = new NewRestaurantDTO();
        restaurantDTO.setName("name");
        restaurantDTO.setNumber("number");
        restaurantDTO.setAdress("adress");

        // Run the test
        final Restaurant result = restaurantMapperUnderTest.toRestaurantNew(restaurantDTO);

        // Verify the results
        assertAll("toRestaurant",
                () -> assertEquals("name", result.getName()),
                () -> assertEquals("number", result.getNumber()),
                () -> assertEquals("adress", result.getAdress()));
    }

    @Test
    void toRestaurantUpdate(){
        final UpdatedRestaurantDTO restaurantDTO = new UpdatedRestaurantDTO();
        restaurantDTO.setName("name");
        restaurantDTO.setNumber("number");
        restaurantDTO.setAdress("adress");
        restaurantDTO.setDeleted(false);

        final Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("name");
        restaurant.setNumber("number");
        restaurant.setAdress("adress");
        restaurant.setDeleted(false);

        final Restaurant result = restaurantMapperUnderTest.toRestaurantUpdate(restaurantDTO, restaurant);

        assertAll("toRestaurantUpdate",
                () -> assertEquals("name", result.getName()),
                () -> assertEquals("number", result.getNumber()),
                () -> assertEquals("adress", result.getAdress()),
                () -> assertEquals(false, result.isDeleted()));
    }
}
