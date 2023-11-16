package com.example.macjava.products.services;

import com.example.macjava.products.dto.ProductdtoNew;
import com.example.macjava.products.dto.ProductdtoUpdate;
import com.example.macjava.products.exceptions.ProductNotFound;
import com.example.macjava.products.mapper.ProductMapper;
import com.example.macjava.products.models.Product;
import com.example.macjava.products.repository.ProductRepository;
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
@Service
@CacheConfig(cacheNames = {"productos"})
public class ProductServiceImp implements ProductService{
    ProductRepository repository;
    ProductMapper mapper= new ProductMapper();
    @Autowired
    public ProductServiceImp(ProductRepository repository){
        this.repository = repository;
    }
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

    @Override
    @Cacheable
    public Product findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFound(id));
    }

    @Override
    @CachePut
    public Product save(ProductdtoNew productdtoNew) {
        Product newProduct = mapper.toProductNew(productdtoNew);
        return repository.save(newProduct);
    }

    @Override
    @CachePut
    @Transactional
    public Product update(Long id, ProductdtoUpdate productdtoUpdate) {
        Product OptionalProduct = repository.findById(id).orElseThrow(() -> new ProductNotFound(id));
        Product updateProduct = mapper.toProductUpdate(productdtoUpdate, OptionalProduct);
        return repository.save(updateProduct);
    }

    @Override
    @CacheEvict
    @Transactional
    public void deleteById(Long id) {
        Product OptionalProduct = repository.findById(id).orElseThrow(() -> new ProductNotFound(id));
        repository.updateIsDeletedToTrueById(id);
    }
}
