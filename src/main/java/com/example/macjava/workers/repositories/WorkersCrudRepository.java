package com.example.macjava.workers.repositories;

import com.example.macjava.workers.models.Workers;
import org.hibernate.annotations.SQLSelect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkersCrudRepository extends JpaRepository<Workers, UUID>, JpaSpecificationExecutor<Workers> {

    @Query("SELECT w FROM Workers w WHERE w.dni LIKE :dni")
    Optional<Workers> findByDni(String dni);

    @Query("SELECT w FROM Workers w WHERE w.isDeleted=:isDeleted")
    List<Workers> findByIsDeleted(Boolean isDeleted);
    // Actualizar el producto con isDeleted a true
    @Modifying // Para indicar que es una consulta de actualización
    @Query("UPDATE Workers w SET w.isDeleted = true WHERE w.uuid = :uuid")
    // Consulta de actualización
    void updateIsDeletedToTrueById(UUID uuid);

    //buscar por nombre, edad  etc
}
