package productos.restaurante.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import productos.controllers.ProductController;
import productos.models.Producto;
import productos.service.CrudService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {

    @Mock
    private CrudService<Producto> productoService;

    @InjectMocks
    private ProductController productController;

    @Test
    void testGetProducto() {

        List<Producto> productos = Arrays.asList(new Producto(), new Producto());
        when(productoService.findAll()).thenReturn(productos);


        ResponseEntity<List<Producto>> response = productController.getProducto();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productos, response.getBody());


        verify(productoService, times(1)).findAll();
    }

    @Test
    void testGetProductoById_Existente() {

        Long productId = 1L;
        Producto producto = new Producto();
        producto.setId(productId);
        Optional<Producto> productoOptional = Optional.of(producto);
        when(productoService.findById(productId)).thenReturn(productoOptional);


        ResponseEntity<Optional<Producto>> response = productController.getProducto(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productoOptional, response.getBody());


        verify(productoService, times(1)).findById(productId);
    }

    @Test
    void testGetProductoById_NoExistente() {

        Long productId = 1L;
        when(productoService.findById(productId)).thenReturn(Optional.empty());


        ResponseEntity<Optional<Producto>> response = productController.getProducto(productId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());


        verify(productoService, times(1)).findById(productId);
    }

    @Test
    void testCreateProduct() {

        Producto producto = new Producto();
        when(productoService.save(any(Producto.class))).thenReturn(producto);


        ResponseEntity<Object> response = productController.createProduct(producto);
        assertEquals(HttpStatus.OK, response.getStatusCode());


        verify(productoService, times(1)).save(any(Producto.class));
    }

    @Test
    void testUpdateProducto_Existente() {

        Long productId = 1L;
        Producto existingProduct = new Producto();
        existingProduct.setId(productId);
        Optional<Producto> existingProductOptional = Optional.of(existingProduct);
        when(productoService.findById(productId)).thenReturn(existingProductOptional);
        when(productoService.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Producto productoActualizado = new Producto();
        productoActualizado.setNomPlato("Nuevo Nombre");
        productoActualizado.setPrecio(19.99);


        ResponseEntity<Producto> response = productController.updateProducto(productId, productoActualizado);
        assertEquals(HttpStatus.OK, response.getStatusCode());


        verify(productoService, times(1)).findById(productId);
        verify(productoService, times(1)).save(any(Producto.class));

        // Verificar que el producto se actualizó correctamente
        assertEquals(productoActualizado, response.getBody());
    }

    @Test
    void testUpdateProducto_NoExistente() {

        Long productId = 1L;
        when(productoService.findById(productId)).thenReturn(Optional.empty());


        Producto productoActualizado = new Producto();
        productoActualizado.setNomPlato("Nuevo Nombre");
        productoActualizado.setPrecio(19.99);


        ResponseEntity<Producto> response = productController.updateProducto(productId, productoActualizado);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());


        verify(productoService, times(1)).findById(productId);

        verify(productoService, never()).save(any(Producto.class));
    }
}
