package com.example.macjava.rest.clients.repository;

import com.example.macjava.rest.clients.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repositorio para la entidad Client
 */
@Repository
public interface ClientsRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {
    /**
     * Actualiza el campo deleted a true
     * @param id id del cliente a actualizar
     */
    @Modifying
    @Query("UPDATE Client p SET p.deleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(UUID id);
}
