package com.example.macjava.products.controller;

import com.example.macjava.products.dto.ProductdtoNew;
import com.example.macjava.products.exceptions.ProductNotFound;
import com.fasterxml.jackson.core.type.TypeReference;
import com.example.macjava.products.dto.ProductdtoUpdate;
import com.example.macjava.products.models.Product;
import com.example.macjava.products.services.ProductService;
import com.example.macjava.products.utils.PageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
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
            .is_deleted(true)
            .fecha_cre(LocalDate.now())
            .fecha_act(LocalDate.now())
            .build();
    private final String myEndpoint = "http://localhost:8080/productos";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ProductService service;
    @Autowired
    private JacksonTester<ProductdtoNew> jsonProductCreateDto;
    @Autowired
    private JacksonTester<ProductdtoUpdate> jsonProductUpdateDto;
    @Autowired
    public ProductControllerTest(ProductService service) {
        this.service = service;
        mapper.registerModule(new JavaTimeModule());
    }
    @Test
    void getAllProducts() throws Exception {
        List<Product> clientsList = List.of(product1, product2);
        Page<Product> page = new PageImpl<>(clientsList);

        when(service.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class),any(Optional.class) , any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Product> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2,res.content().size())
        );
    }

    @Test
    void getAllProducts_Name() throws Exception {
        List<Product> clientsList = List.of(product1);
        Page<Product> page = new PageImpl<>(clientsList);
        Optional<String> nombre = Optional.of("test");

        when(service.findAll(eq(nombre), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class),any(Optional.class) , any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?nombre=test")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Product> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }

    @Test
    void getAllProducts_StockMax() throws Exception {
        List<Product> clientsList = List.of(product1);
        Page<Product> page = new PageImpl<>(clientsList);
        Optional<Integer> stockMax = Optional.of(10);

        when(service.findAll(any(Optional.class), eq(stockMax), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class),any(Optional.class) , any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?stockMax=10")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Product> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }

    @Test
    void getAllProducts_StockMin() throws Exception {
        List<Product> clientsList = List.of(product1);
        Page<Product> page = new PageImpl<>(clientsList);
        Optional<Integer> stockMin = Optional.of(10);

        when(service.findAll(any(Optional.class), any(Optional.class), eq(stockMin), any(Optional.class), any(Optional.class), any(Optional.class),any(Optional.class) , any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?stockMin=10")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Product> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }

    @Test
    void getAllProducts_PrecioMax() throws Exception {
        List<Product> clientsList = List.of(product1);
        Page<Product> page = new PageImpl<>(clientsList);
        Optional<Double> precioMax = Optional.of(5.99);

        when(service.findAll(any(Optional.class), any(Optional.class), any(Optional.class), eq(precioMax), any(Optional.class), any(Optional.class),any(Optional.class) , any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?precioMax=5.99")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Product> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }
    @Test
    void getAllProducts_PrecioMin() throws Exception {
        List<Product> clientsList = List.of(product1);
        Page<Product> page = new PageImpl<>(clientsList);
        Optional<Double> precioMin = Optional.of(5.99);

        when(service.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), eq(precioMin), any(Optional.class),any(Optional.class) , any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?precioMin=5.99")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Product> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }
    @Test
    void getAllProducts_Gluten() throws Exception {
        List<Product> clientsList = List.of(product2);
        Page<Product> page = new PageImpl<>(clientsList);
        Optional<Boolean> gluten = Optional.of(false);

        when(service.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), eq(gluten),any(Optional.class) , any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?gluten=false")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Product> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }

    @Test
    void getAllProducts_deleted() throws Exception {
        List<Product> clientsList = List.of(product2);
        Page<Product> page = new PageImpl<>(clientsList);
        Optional<Boolean> is_deleted = Optional.of(true);

        when(service.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class),eq(is_deleted) , any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?is_deleted=true")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Product> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }
    @Test
    void getProduct() throws Exception {
        when(service.findById(product1.getId())).thenReturn(product1);
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint + "/" + product1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).findById(product1.getId())
        );
    }

    @Test
    void getProduct_NotFound() throws Exception {
        when(service.findById(product1.getId()))
                .thenThrow(new ProductNotFound(product1.getId()));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint + "/" + product1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(service, times(1)).findById(product1.getId());
    }
    @Test
    void createProduct() throws Exception {
        ProductdtoNew productdtoNew = ProductdtoNew.builder()
                .nombre("test")
                .stock(1)
                .precio(1.0)
                .gluten(true)
                .build();
        when(service.save(productdtoNew)).thenReturn(product1);
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductCreateDto.write(productdtoNew).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
        verify(service, times(1)).save(productdtoNew);
    }

    @Test
    void createProduct_NoName() throws Exception {
        ProductdtoNew productdtoNew = ProductdtoNew.builder()
                .nombre("")
                .stock(1)
                .precio(1.0)
                .gluten(true)
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductCreateDto.write(productdtoNew).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }
    @Test
    void createProduct_NoStock() throws Exception {
        ProductdtoNew productdtoNew = ProductdtoNew.builder()
                .nombre("test")
                .stock(null)
                .precio(1.0)
                .gluten(true)
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductCreateDto.write(productdtoNew).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createProduct_NoPrecio() throws Exception {
        ProductdtoNew productdtoNew = ProductdtoNew.builder()
                .nombre("test")
                .stock(1)
                .precio(0)
                .gluten(true)
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductCreateDto.write(productdtoNew).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }
    @Test
    void updateProduct() throws Exception {
        ProductdtoUpdate productdtoUpdate = ProductdtoUpdate.builder()
                .nombre("test")
                .stock(1)
                .precio(1.0)
                .gluten(true)
                .build();
        when(service.findById(product1.getId())).thenReturn(product1);
        when(service.update(product1.getId(), productdtoUpdate)).thenReturn(product1);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + product1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductUpdateDto.write(productdtoUpdate).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        verify(service, times(1)).update(product1.getId(), productdtoUpdate);
    }
    @Test
    void updateProduct_NoName() throws Exception {
        ProductdtoUpdate productdtoUpdate = ProductdtoUpdate.builder()
                .nombre("")
                .stock(1)
                .precio(1.0)
                .gluten(true)
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + product1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductUpdateDto.write(productdtoUpdate).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void updateProduct_NoStock() throws Exception {
        ProductdtoUpdate productdtoUpdate = ProductdtoUpdate.builder()
                .nombre("test")
                .stock(null)
                .precio(1.0)
                .gluten(true)
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + product1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductUpdateDto.write(productdtoUpdate).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void updateProduct_NoPrecio() throws Exception {
        ProductdtoUpdate productdtoUpdate = ProductdtoUpdate.builder()
                .nombre("test")
                .stock(1)
                .precio(0)
                .gluten(true)
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + product1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductUpdateDto.write(productdtoUpdate).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void updateProduct_NotFound() throws Exception {
        ProductdtoUpdate productdtoUpdate = ProductdtoUpdate.builder()
                .nombre("test")
                .stock(1)
                .precio(1.0)
                .gluten(true)
                .build();
        when(service.update(product1.getId(), productdtoUpdate)).thenThrow(new ProductNotFound(product1.getId()));
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + product1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductUpdateDto.write(productdtoUpdate).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(404, response.getStatus());
    }
    @Test
    void deleteProduct() throws Exception {
        doNothing().when(service).deleteById(product1.getId());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndpoint + "/" + product1.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(204, response.getStatus())
        );
        verify(service, times(1)).deleteById(product1.getId());
    }

    @Test
    void deleteProduct_NotFound() throws Exception {
        doThrow(new ProductNotFound(product1.getId())).when(service).deleteById(product1.getId());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndpoint + "/" + product1.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(service, times(1)).deleteById(product1.getId());
    }
}