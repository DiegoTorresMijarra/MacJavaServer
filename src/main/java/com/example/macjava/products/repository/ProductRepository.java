package com.example.macjava.products.repository;

import com.example.macjava.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Modifying
    @Query("UPDATE Product p SET p.is_deleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(Long id);
}
