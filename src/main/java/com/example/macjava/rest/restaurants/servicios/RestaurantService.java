package com.example.macjava.rest.restaurants.servicios;

import com.example.macjava.rest.restaurants.dto.NewRestaurantDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.macjava.rest.restaurants.dto.UpdatedRestaurantDTO;
import com.example.macjava.rest.restaurants.modelos.Restaurant;

import java.util.Optional;

/**
 * Interfaz de servicio de la entidad Restaurant
 */
public interface RestaurantService {
    /**
     * Método que obtiene todos los restaurantes
     * @param name Opcional: nombre del restaurante
     * @param number Opcional: número de teléfono del restaurante
     * @param isDeleted Opcional: indica si el restaurante está eliminado
     * @param page informacion de la paginación
     * @return Lista de restaurantes que cumplan con los parámetros de búsqueda
     */
    Page<Restaurant> findAll(Optional<String> name, Optional<String> number, Optional<Boolean> isDeleted, Pageable page);

    /**
     * Método que obtiene un restaurante por su ID
     * @param id ID del restaurante a buscar
     * @return  Restaurante que coincida con el ID
     */
    Restaurant findById(Long id);

    /**
     * Método que guarda un restaurante con la información de un RestauranteDTO
     * @param restau RestauranteDTO con la informacion del restaurante a guardar
     * @return Restaurante guardado
     */
    Restaurant save (NewRestaurantDTO restau);

    /**
     * Método que actualiza un restaurante con la información de un RestauranteDTO
     * @param id ID del restaurante a actualizar
     * @param restau RestauranteDTO con la información a actualizar
     * @return Restaurante actualizado
     */
    Restaurant update (Long id, UpdatedRestaurantDTO restau);
    /**
     * Método que elimina un restaurante por su ID
     * @param id ID del restaurante a eliminar
     */
    void deleteById(Long id);
}
