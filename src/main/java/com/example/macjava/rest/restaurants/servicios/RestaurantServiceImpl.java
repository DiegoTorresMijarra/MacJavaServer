package com.example.macjava.rest.restaurants.servicios;

import com.example.macjava.rest.restaurants.dto.NewRestaurantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.macjava.rest.restaurants.dto.UpdatedRestaurantDTO;
import com.example.macjava.rest.restaurants.exceptions.RestaurantNotFound;
import com.example.macjava.rest.restaurants.mapper.RestaurantMapper;
import com.example.macjava.rest.restaurants.modelos.Restaurant;
import com.example.macjava.rest.restaurants.repositories.RestaurantRepository;

import java.util.Optional;

/**
 * Implementación de la interfaz RestaurantService
 * Proporciona los métodos para gestionar los restaurantes
 * Anotamos la clase con @Service para indicar que es un servicio
 * Anotamos la clase con @CacheConfig para indicar que el nombre de la cache es "restaurants"
 * @Parameter repository Repositorio de restaurantes
 * @Parameter map Mapeador de restaurantes
 */
@CacheConfig(cacheNames = {"restaurants"})
@Service
public class RestaurantServiceImpl implements RestaurantService{
    RestaurantRepository repository;
    RestaurantMapper map =new RestaurantMapper();

    /**
     * Constructor de la clase
     * @param repository Repositorio de restaurantes
     */
    @Autowired
    public RestaurantServiceImpl(RestaurantRepository repository){
        this.repository=repository;
    }

    /**
     * Método que obtiene todos los restaurantes que cumplan con los parámetros de búsqueda
     * @param name Opcional: nombre del restaurante
     * @param number Opcional: número de teléfono del restaurante
     * @param isDeleted Opcional: indica si el restaurante está eliminado
     * @param page informacion de la paginación
     * @return Pagina de restaurantes que cumplan con los parámetros de búsqueda
     */
    @Override
    public Page<Restaurant> findAll(Optional<String> name, Optional<String> number, Optional<Boolean> isDeleted, Pageable page) {
        //Criterio busqueda nombre
        Specification<Restaurant> speNameRes=(root, query, criteriaBuilder)->
                name.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + m.toLowerCase() + "%")) // Buscamos por nombre
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))); // Si no hay nombre, no filtramos
        //Criterio busqueda numero
        Specification<Restaurant> speNumRes = (root, query, criteriaBuilder) ->
                number.map(m -> criteriaBuilder.equal(root.get("number"), m)) // Buscamos por numero
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))); // Si no hay numero, no filtramos
        //Criterio busqueda isDeleted
        Specification<Restaurant> speIsDeleted = (root, query, criteriaBuilder) ->
                isDeleted.map(d -> criteriaBuilder.equal(root.get("isDeleted"), d)) // Buscamos por deleted
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))); // Si no hay deleted, no filtramos
        Specification<Restaurant>criterio =Specification.where(speNameRes)
                .and(speNumRes).and(speIsDeleted);
        return repository.findAll(criterio,page);
    }

    /**
     * Método que obtiene un restaurante por su ID
     * @param id ID del restaurante a buscar
     * @return Restaurante que coincida con el ID
     */
    @Override
    @Cacheable
    public Restaurant findById(Long id) {

        return repository.findById(id).orElseThrow(()->new RestaurantNotFound(id));
    }

    /**
     * Método que guarda un restaurante con la información de un RestauranteDTO
     * @param restau RestauranteDTO con la informacion del restaurante a guardar
     * @return Restaurante guardado
     */
    @Override
    @CachePut
    public Restaurant save(NewRestaurantDTO restau) {
        Restaurant savedRestaurant = map.toRestaurantNew(restau);
        return repository.save(savedRestaurant);
    }

    /**
     * Método que actualiza un restaurante con la información de un RestauranteDTO
     * @param id ID del restaurante a actualizar
     * @param restau RestauranteDTO con la información a actualizar
     * @return Restaurante actualizado
     */
    @Override
    @CachePut
    @Transactional
    public Restaurant update(Long id, UpdatedRestaurantDTO restau) {
        Restaurant optionalRestaurant= findById(id);
        Restaurant updatedRestaurant = map.toRestaurantUpdate(restau, optionalRestaurant);
        return repository.save(updatedRestaurant);
    }

    /**
     * Método que llama a un metodo del repositorio
     * para actualizar el campo isDeleted a true de un restaurante (realizar un borrado lógico)
     * @param id ID del restaurante a eliminar
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        Restaurant optionalRestaurant =findById(id);
        repository.updateIsDeletedToTrueById(id);
    }
}
