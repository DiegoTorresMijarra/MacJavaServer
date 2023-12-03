package com.example.macjava.rest.workers.repositories;

import com.example.macjava.rest.workers.models.Workers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para la entidad Workers
 */
@Repository
public interface WorkersCrudRepository extends JpaRepository<Workers, UUID>, JpaSpecificationExecutor<Workers> {

    /**
     * Busca un trabajador por su dni
     * @param dni  dni del trabajador a buscar
     * @return trabajador encontrado
     */
    @Query("SELECT w FROM Workers w WHERE w.dni LIKE :dni")
    Optional<Workers> findByDni(String dni);

    /**
     * Busca trabajadores borrados o no (borrado logico)
     * @param isDeleted  indica si los trabajadores buscados estan borrados o no
     * @return Lista de trabajadores encontrados
     */
    @Query("SELECT w FROM Workers w WHERE w.isDeleted=:isDeleted")
    List<Workers> findByIsDeleted(Boolean isDeleted);

    /**
     * Actualiza el campo isDeleted a true
     * @param uuid id del trabajador a actualizar
     */
    @Modifying // Para indicar que es una consulta de actualización
    @Query("UPDATE Workers w SET w.isDeleted = true WHERE w.uuid = :uuid")
    // Consulta de actualización
    void updateIsDeletedToTrueById(UUID uuid);

    //buscar por nombre, edad  etc
}
