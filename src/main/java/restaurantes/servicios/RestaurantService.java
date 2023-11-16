package restaurantes.servicios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import restaurantes.dto.NewRestaurantDTO;
import restaurantes.dto.UpdatedRestaurantDTO;
import restaurantes.models.Restaurant;

import java.util.Optional;

public interface RestaurantService {
    Page<Restaurant> findAll(Optional<String> name, Optional<String> number, Optional<Boolean> isDeleted, Optional<String> adress, Pageable page);
    Restaurant findById(Long id);
    Restaurant findByAdress(String adress);
    Restaurant save (NewRestaurantDTO restau);
    Restaurant update (Long id, UpdatedRestaurantDTO restau);
    void deleteById(Long id);
}
