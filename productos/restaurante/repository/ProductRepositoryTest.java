package productos.restaurante.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import productos.models.Producto;
import productos.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testCreateProduct() {
        Producto nuevoProducto = new Producto();
        productRepository.createProduct(nuevoProducto);

        List<Producto> productos = productRepository.findAll();
        assertTrue(productos.contains(nuevoProducto));
    }

    @Test
    public void testFindById_Existente() {
        Long productId = 1L;
        Optional<Producto> productoOptional = productRepository.findById(productId);

        assertTrue(productoOptional.isPresent());
        assertEquals(productId, productoOptional.get().getId());
    }

    @Test
    public void testFindById_NoExistente() {
        Long productId = 100L;
        Optional<Producto> productoOptional = productRepository.findById(productId);

        assertTrue(productoOptional.isEmpty());
    }

    @Test
    public void testFindAll() {
        List<Producto> productos = productRepository.findAll();

        assertEquals(8, productos.size()); // Ajusta este número según la cantidad inicial de productos
    }

    @Test
    public void testSave() {
        Long productId = 1L;
        Producto productoActualizado = new Producto();
        productRepository.save(productId, productoActualizado);

        Optional<Producto> productoOptional = productRepository.findById(productId);
        assertTrue(productoOptional.isPresent());
        assertEquals(productoActualizado, productoOptional.get());
    }

    @Test
    public void testDeleteById() {
        Long productId = 1L;
        productRepository.deleteById(productId);

        List<Producto> productos = productRepository.findAll();
        assertTrue(productos.stream().noneMatch(producto -> producto.getId().equals(productId)));
    }

    @Test
    public void testDeleteAll() {
        productRepository.deleteAll();

        List<Producto> productos = productRepository.findAll();
        assertEquals(0, productos.size());
    }
}
