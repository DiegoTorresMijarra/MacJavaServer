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

@CacheConfig(cacheNames = {"restaurants"})
@Service
public class RestaurantServiceImpl implements RestaurantService{
    RestaurantRepository repository;
    RestaurantMapper map =new RestaurantMapper();


    @Autowired
    public RestaurantServiceImpl(RestaurantRepository repository){
        this.repository=repository;
    }

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

    @Override
    @Cacheable
    public Restaurant findById(Long id) {

        return repository.findById(id).orElseThrow(()->new RestaurantNotFound(id));
    }

    @Override
    @CachePut
    public Restaurant save(NewRestaurantDTO restau) {
        Restaurant savedRestaurant = map.toRestaurantNew(restau);
        return repository.save(savedRestaurant);
    }

    @Override
    @CachePut

    @Transactional
    public Restaurant update(Long id, UpdatedRestaurantDTO restau) {
        Restaurant optionalRestaurant= findById(id);
        Restaurant updatedRestaurant = map.toRestaurantUpdate(restau, optionalRestaurant);
        return repository.save(updatedRestaurant);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Restaurant optionalRestaurant =findById(id);
        repository.updateIsDeletedToTrueById(id);
    }
}
