package restaurantes.mapper;



import restaurantes.dto.NewRestaurantDTO;
import restaurantes.dto.UpdatedRestaurantDTO;
import restaurantes.models.Restaurant;

import java.time.LocalDate;

public class RestaurantMapper {

    public Restaurant toRestaurantNew(NewRestaurantDTO restau){
        return Restaurant.builder()
                .name(restau.getName())
                .number(restau.getNumber())
                .adress(restau.getAdress())
                .creationD(LocalDate.now())
                .modificationD(LocalDate.now())
                .build();
    }

    public Restaurant toRestaurantUpdate (UpdatedRestaurantDTO restaudto , Restaurant realRestau){
        return Restaurant.builder()
                .id(realRestau.getId())
                .name(restaudto.getName())
                .number(restaudto.getNumber())
                .adress(restaudto.getAdress())
                .isDeleted(restaudto.isDeleted())
                .creationD(realRestau.getCreationD())
                .modificationD(LocalDate.now())
                .build();
    }
}
