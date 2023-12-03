package com.example.macjava.rest.restaurants.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.macjava.rest.restaurants.modelos.Restaurant;

/**
 * Interfaz de repositorio de la entidad Restaurant
 * Extiende de JpaRepository para obtener los métodos básicos de un CRUD
 * y JpaSpecificationExecutor para obtener los métodos de especificación de JPA
 */
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {

    /**
     * Método que actualiza el campo isDeleted a true de un restaurante
     * @param id ID del restaurante a actualizar
     */
    @Modifying
    @Query ("UPDATE Restaurant p Set p.isDeleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(Long id);

}
