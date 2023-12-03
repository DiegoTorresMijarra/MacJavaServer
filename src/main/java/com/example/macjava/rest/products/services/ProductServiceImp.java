package com.example.macjava.rest.products.services;

import com.example.macjava.rest.products.dto.ProductdtoNew;
import com.example.macjava.rest.products.dto.ProductdtoUpdate;
import com.example.macjava.rest.products.exceptions.ProductNotFound;
import com.example.macjava.rest.products.mapper.ProductMapper;
import com.example.macjava.rest.products.models.Product;
import com.example.macjava.rest.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación de la interfaz ProductService.
 * Se utiliza la anotación @Service para indicar que es un servicio de Spring.
 * Se utiliza la anotación @CacheConfig para indicar el nombre de la caché.
 */
@Service
@CacheConfig(cacheNames = {"productos"})
public class ProductServiceImp implements ProductService{
    ProductRepository repository;
    ProductMapper mapper= new ProductMapper();
    /**
     * Constructor de la clase
     * @param repository Repositorio de productos
     * (se utiliza la anotación @Autowired para indicar que se trata de una inyección de dependencia).
     */
    @Autowired
    public ProductServiceImp(ProductRepository repository){
        this.repository = repository;
    }

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
    @Override
    public Page<Product> findAll(Optional<String> nombre, Optional<Integer> stockMax, Optional<Integer> stockMin, Optional<Double> precioMax, Optional<Double> precioMin, Optional<Boolean> gluten, Optional<Boolean> is_deleted, Pageable pageable) {
        Specification<Product> specNombre = (root, query, criteriaBuilder) ->
                nombre.map(m -> criteriaBuilder.equal(root.get("nombre"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specStockMax = (root, query, criteriaBuilder) ->
                stockMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("stock"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specStockMin = (root, query, criteriaBuilder) ->
                stockMin.map(p -> criteriaBuilder.greaterThanOrEqualTo(root.get("stock"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specPrecioMax = (root, query, criteriaBuilder) ->
                precioMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("precio"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specPrecioMin = (root, query, criteriaBuilder) ->
                precioMin.map(p -> criteriaBuilder.greaterThanOrEqualTo(root.get("precio"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specIs_Deleted = (root, query, criteriaBuilder) ->
                is_deleted.map(d -> criteriaBuilder.equal(root.get("is_deleted"), d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specGluten = (root, query, criteriaBuilder) ->
                gluten.map(d -> criteriaBuilder.equal(root.get("gluten"), d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> criterio = Specification.where(specNombre)
                .and(specStockMax)
                .and(specStockMin)
                .and(specPrecioMax)
                .and(specPrecioMin)
                .and(specIs_Deleted)
                .and(specGluten);
        return repository.findAll(criterio, pageable);
    }

    /**
     * Método que devuelve un producto dado su ID.
     * @param id ID del producto a buscar
     * @return Producto con el ID indicado
     * @throws ProductNotFound Excepción que se lanza si no se encuentra el producto
     */
    @Override
    @Cacheable
    public Product findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFound(id));
    }

    /**
     * Método que guarda un producto en la base de datos.
     * @param productdtoNew dto de Producto a guardar
     * @return Producto guardado
     */
    @Override
    @CachePut
    public Product save(ProductdtoNew productdtoNew) {
        Product newProduct = mapper.toProductNew(productdtoNew);
        return repository.save(newProduct);
    }

    /**
     * Método que actualiza un producto dado su ID.
     * @param id ID del producto a actualizar
     * @param productdtoUpdate dto de Producto con la información a actualizar
     * @return Producto actualizado
     */
    @Override
    @CachePut
    @Transactional
    public Product update(Long id, ProductdtoUpdate productdtoUpdate) {
        Product OptionalProduct = repository.findById(id).orElseThrow(() -> new ProductNotFound(id));
        Product updateProduct = mapper.toProductUpdate(productdtoUpdate, OptionalProduct);
        return repository.save(updateProduct);
    }

    /**
     * Hace llamada a otro metodo para cambiar el estado del atributo is_deleted a true (borrado lógico)
     * @param id ID del producto a eliminar
     */
    @Override
    @CacheEvict
    @Transactional
    public void deleteById(Long id) {
        Product OptionalProduct = repository.findById(id).orElseThrow(() -> new ProductNotFound(id));
        repository.updateIsDeletedToTrueById(id);
    }
}
