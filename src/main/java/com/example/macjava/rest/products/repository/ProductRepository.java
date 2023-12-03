package com.example.macjava.rest.products.repository;

import com.example.macjava.rest.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de productos que extiende de JpaRepository y JpaSpecificationExecutor
 * para poder realizar operaciones de persistencia sobre la base de datos.
 * También se utiliza la anotación @Repository para indicar que es un repositorio de Spring.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    /**
     * Método que actualiza el campo is_deleted a true para un producto dado su ID.
     * @param id ID del producto a actualizar
     * (se utiliza la anotación @Query para indicar la consulta a realizar).
     */
    @Modifying
    @Query("UPDATE Product p SET p.is_deleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(Long id);
}
