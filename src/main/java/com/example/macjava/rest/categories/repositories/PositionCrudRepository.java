package com.example.macjava.rest.categories.repositories;

import com.example.macjava.rest.categories.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Position
 */
@Repository
public interface PositionCrudRepository extends JpaRepository<Position, Long>, JpaSpecificationExecutor<Position> {
    /**
     * Actualiza el campo isDeleted a true
     * @param id id de la posición a actualizar
     */
    @Modifying // Para indicar que es una consulta de actualización
    @Query("UPDATE Position p SET p.isDeleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(Long id);
}
