package com.example.macjava.rest.products.services;

import com.example.macjava.rest.products.dto.ProductdtoNew;
import com.example.macjava.rest.products.dto.ProductdtoUpdate;
import com.example.macjava.rest.products.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interfaz que define los métodos que debe implementar el servicio de productos.
 */
public interface ProductService {
    /**
     * Método que devuelve todos los productos de la base de datos.
     * @param nombre Nombre del producto
     * @param stockMax Stock máximo del producto
     * @param stockMin Stock mínimo del producto
     * @param precioMax Precio máximo del producto
     * @param precioMin Precio mínimo del producto
     * @param gluten indica si el producto tiene gluten
     * @param is_deleted indica si el producto está eliminado
     * @param pageable Información de la paginación
     * @return Página de productos que cumplan con los parámetros de búsqueda
     */
    Page<Product> findAll(Optional<String> nombre, Optional<Integer> stockMax, Optional<Integer> stockMin, Optional<Double> precioMax, Optional<Double> precioMin , Optional<Boolean> gluten, Optional<Boolean> is_deleted, Pageable pageable);

    /**
     * Método que devuelve un producto dado su ID.
     * @param id ID del producto a buscar
     * @return  Producto con el ID indicado
     */
    Product findById(Long id);

    /**
     * Método que guarda un producto en la base de datos.
     * @param productdtoNew dto de Producto a guardar
     * @return Producto guardado
     */
   Product save(ProductdtoNew productdtoNew);

    /**
     * Método que actualiza un producto dado su ID.
     * @param id ID del producto a actualizar
     * @param productdtoUpdate dto de Producto con la información a actualizar
     * @return Producto actualizado
     */
   Product update (Long id, ProductdtoUpdate productdtoUpdate);
   /**
     * Método que elimina un producto dado su ID.
     * @param id ID del producto a eliminar
     */
   void deleteById(Long id);
}
