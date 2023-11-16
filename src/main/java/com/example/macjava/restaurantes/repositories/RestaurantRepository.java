package com.example.macjava.restaurantes.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.macjava.restaurantes.modelos.Restaurante;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurante, Long>, JpaSpecificationExecutor<Restaurante> {

    @Modifying
    @Query ("UPDATE Restaurante p Set p.isDeleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(Long id);

}
