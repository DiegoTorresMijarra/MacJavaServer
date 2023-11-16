package restaurantes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestaurantNotFound extends RestaurantException{
    public RestaurantNotFound(Long id){
        super("Restaurante con id "+ id +" no encontrado.");
    }

    public RestaurantNotFound(String adress){
        super("Restaurante con direccion "+ adress +" no encontrado.");
    }
}
