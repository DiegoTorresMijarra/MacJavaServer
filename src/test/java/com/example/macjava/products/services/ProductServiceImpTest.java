package com.example.macjava.products.services;

import com.example.macjava.products.dto.ProductdtoNew;
import com.example.macjava.products.dto.ProductdtoUpdate;
import com.example.macjava.products.exceptions.ProductNotFound;
import com.example.macjava.products.mapper.ProductMapper;
import com.example.macjava.products.models.Product;
import com.example.macjava.products.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImpTest {
    Product product1 = Product.builder()
            .id(1L)
            .nombre("test")
            .precio(5.99)
            .stock(10)
            .gluten(true)
            .is_deleted(false)
            .fecha_cre(LocalDate.now())
            .fecha_act(LocalDate.now())
            .build();
    Product product2 = Product.builder()
            .id(2L)
            .nombre("test2")
            .precio(7.99)
            .stock(7)
            .gluten(false)
            .is_deleted(false)
            .fecha_cre(LocalDate.now())
            .fecha_act(LocalDate.now())
            .build();
    @Mock
    private ProductRepository repository;
    ProductMapper mapper = new ProductMapper();
    @InjectMocks
    private ProductServiceImp service;
    @Test
    void findAll_ShouldReturnAllClients_WhenNoParametersProvided() {
        List<Product> expectedClients = Arrays.asList(product1, product2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Product> actualPage = service.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(), pageable);
        assertAll("findAll",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ShouldReturnClientsByName_WhenNameParameterProvided() {
        Optional<String> nombre = Optional.of("test");
        List<Product> expectedClients = List.of(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Product> actualPage = service.findAll(nombre, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll("findAllWithDni",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ShouldReturnClientsByStockMax_WhenStockParameterProvided() {
        Optional<Integer> stockmax = Optional.of(10);
        List<Product> expectedClients = List.of(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Product> actualPage = service.findAll(Optional.empty(),stockmax, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(), pageable);
        assertAll("findAllWithDni",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnClientsByStockMin_WhenStockParameterProvided() {
        Optional<Integer> stockmin = Optional.of(10);
        List<Product> expectedClients = List.of(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Product> actualPage = service.findAll(Optional.empty(), Optional.empty(),stockmin,  Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(), pageable);
        assertAll("findAllWithDni",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnClientsByPrecioMax_WhenPrecioParameterProvided() {
        Optional<Double> precioMax = Optional.of(5.99);
        List<Product> expectedClients = List.of(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Product> actualPage = service.findAll(Optional.empty(), Optional.empty(), Optional.empty(), precioMax,  Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll("findAllWithDni",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ShouldReturnClientsByPrecioMin_WhenPrecioParameterProvided() {
        Optional<Double> precioMin = Optional.of(5.99);
        List<Product> expectedClients = List.of(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Product> actualPage = service.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),  precioMin, Optional.empty(), Optional.empty(), pageable);
        assertAll("findAllWithDni",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnClientsByGluten_WhenGlutenParameterProvided() {
        Optional<Boolean> gluten = Optional.of(true);
        List<Product> expectedClients = List.of(product1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Product> actualPage = service.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),  Optional.empty(), gluten, Optional.empty(), pageable);
        assertAll("findAllWithDni",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ShouldReturnClientsByDeleted_WhenDeletedParameterProvided() {
        Optional<Boolean> deleted = Optional.of(true);
        List<Product> expectedClients = List.of(product2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Product> actualPage = service.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),  Optional.empty(), Optional.empty(), deleted, pageable);
        assertAll("findAllWithDni",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findById() {
        when(repository.findById(product1.getId())).thenReturn(Optional.of(product1));
        Product actualProduct = service.findById(1L);
        assertAll("findById",
                () -> assertNotNull(actualProduct),
                () -> assertEquals(product1, actualProduct)
        );
    }

    @Test
    void findById_NotFound() {
        when(repository.findById(product1.getId())).thenReturn(Optional.empty());
        assertThrows(ProductNotFound.class, () -> service.findById(1L));
    }
    @Test
    void save() {
        ProductdtoNew productdtoNew = ProductdtoNew.builder()
                .nombre("test")
                .stock(1)
                .precio(1.0)
                .gluten(true)
                .build();
        Product newProduct = mapper.toProductNew(productdtoNew);
        when(repository.save(newProduct)).thenReturn(newProduct);
        Product actualProduct = service.save(productdtoNew);
        assertAll("save",
                () -> assertNotNull(actualProduct),
                () -> assertEquals(newProduct, actualProduct)
        );
        verify(repository, times(1)).save(newProduct);
    }

    @Test
    void update() {
        ProductdtoUpdate productdtoUpdate = ProductdtoUpdate.builder()
                .nombre("test")
                .stock(1)
                .precio(1.0)
                .gluten(true)
                .build();
        when(repository.findById(product1.getId())).thenReturn(Optional.of(product1));
        Product updateProduct = mapper.toProductUpdate(productdtoUpdate, product1);
        when(repository.save(updateProduct)).thenReturn(updateProduct);
        Product actualProduct = service.update(1L, productdtoUpdate);
        assertAll("update",
                () -> assertNotNull(actualProduct),
                () -> assertEquals(updateProduct, actualProduct)
        );
        verify(repository, times(1)).findById(product1.getId());
        verify(repository, times(1)).save(updateProduct);
    }

    @Test
    void update_NotFound() {
        ProductdtoUpdate productdtoUpdate = ProductdtoUpdate.builder()
                .nombre("test")
                .stock(1)
                .precio(1.0)
                .gluten(true)
                .build();
        when(repository.findById(product1.getId())).thenReturn(Optional.empty());
        assertThrows(ProductNotFound.class, () -> service.update(product1.getId(), productdtoUpdate));
    }

    @Test
    void deleteById() {
        when(repository.findById(product1.getId())).thenReturn(Optional.of(product1));
        service.deleteById(product1.getId());
        verify(repository, times(1)).updateIsDeletedToTrueById(product1.getId());
    }

    @Test
    void deleteById_NotFound() {
        when(repository.findById(product1.getId())).thenReturn(Optional.empty());
        assertThrows(ProductNotFound.class, () -> service.deleteById(product1.getId()));
        verify(repository, times(0)).updateIsDeletedToTrueById(product1.getId());
    }
}