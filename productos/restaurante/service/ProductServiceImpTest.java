package productos.restaurante.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import productos.models.Producto;
import productos.repositories.CrudRepository;
import productos.service.CrudService;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductServiceImpTest {

    @Autowired
    private CrudService<Producto> productService;

    @Autowired
    private CrudRepository productoRepository;

    @Test
    public void testCreateProduct() {
        Producto nuevoProducto = new Producto();
        productService.save(nuevoProducto);

        List<Producto> productos = productService.findAll();
        assertEquals(1, productos.size());
        assertEquals(nuevoProducto, productos.get(0));
    }

    @Test
    public void testFindById_Existente() {
        Producto productoExistente = new Producto();
        productoRepository.createProduct(productoExistente);

        Long productId = productoExistente.getId();
        Producto productoEncontrado = productService.findById(productId).orElse(null);

        assertNotNull(productoEncontrado);
        assertEquals(productoExistente, productoEncontrado);
    }

    @Test
    public void testFindById_NoExistente() {
        Long productId = 100L;
        Producto productoEncontrado = productService.findById(productId).orElse(null);

        assertNull(productoEncontrado);
    }

    @Test
    public void testFindAll() {
        Producto producto1 = new Producto();
        Producto producto2 = new Producto();
        productoRepository.createProduct(producto1);
        productoRepository.createProduct(producto2);

        List<Producto> productos = productService.findAll();

        assertEquals(2, productos.size());
        assertTrue(productos.contains(producto1));
        assertTrue(productos.contains(producto2));
    }

    @Test
    public void testSave_Actualizar() {
        Producto productoExistente = new Producto();
        productoRepository.createProduct(productoExistente);

        productoExistente.setNomPlato("Producto Actualizado");
        productService.save(productoExistente);

        Producto productoActualizado = productService.findById(productoExistente.getId()).orElse(null);
        assertNotNull(productoActualizado);
        assertEquals("Producto Actualizado", productoActualizado.getNomPlato());
    }

    @Test
    public void testDeleteById() {
        Producto productoExistente = new Producto();
        productoRepository.createProduct(productoExistente);

        Long productId = productoExistente.getId();
        productService.deleteById(productId);

        Producto productoEliminado = productService.findById(productId).orElse(null);
        assertNull(productoEliminado);
    }

    @Test
    public void testDeleteAll() {
        Producto producto1 = new Producto();
        Producto producto2 = new Producto();
        productoRepository.createProduct(producto1);
        productoRepository.createProduct(producto2);

        productService.deleteAll();

        List<Producto> productos = productService.findAll();
        assertEquals(0, productos.size());
    }


}
