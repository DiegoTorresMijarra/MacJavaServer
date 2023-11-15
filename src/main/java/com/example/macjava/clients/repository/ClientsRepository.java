package com.example.macjava.clients.repository;

import com.example.macjava.clients.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ClientsRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {
    @Modifying
    @Query("UPDATE Client p SET p.deleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(UUID id);
}
