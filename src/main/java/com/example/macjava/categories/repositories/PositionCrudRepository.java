package com.example.macjava.categories.repositories;

import com.example.macjava.categories.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PositionCrudRepository extends JpaRepository<Position, Long>, JpaSpecificationExecutor<Position> {
    @Modifying // Para indicar que es una consulta de actualización
    @Query("UPDATE Position p SET p.isDeleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(Long id);
}
