package com.example.macjava.clients.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.example.macjava.clients.dto.ClientdtoNew;
import com.example.macjava.clients.dto.ClientdtoUpdated;
import com.example.macjava.clients.exceptions.ClientNotFound;
import com.example.macjava.clients.models.Client;
import com.example.macjava.clients.service.ClientService;
import com.example.macjava.clients.utils.PageResponse;
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
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class ClientControllerTest {
    private Client client1 = Client.builder()
            .id(UUID.randomUUID())
            .dni("12345678A")
            .name("John")
            .last_name("Doe")
            .age(30)
            .phone("123456789")
            .image("http://")
            .deleted(false)
            .fecha_act(LocalDate.now())
            .fecha_cre(LocalDate.now())
            .build();
    private Client client2 = Client.builder()
            .id(UUID.randomUUID())
            .dni("98765432B")
            .name("Jane")
            .last_name("Smith")
            .age(25)
            .phone("987654321")
            .image("http://")
            .deleted(false)
            .fecha_act(LocalDate.now())
            .fecha_cre(LocalDate.now())
            .build();
    private Client client3 = Client.builder()
            .id(UUID.randomUUID())
            .dni("63957327T")
            .name("Alberto")
            .last_name("zarza")
            .age(23)
            .phone("123456789")
            .image("http://")
            .deleted(true)
            .fecha_act(LocalDate.now())
            .fecha_cre(LocalDate.now())
            .build();
    private final String myEndpoint = "http://localhost:8080/clientes";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ClientService service;
    @Autowired
    private JacksonTester<ClientdtoNew> jsonClientCreateDto;
    @Autowired
    private JacksonTester<ClientdtoUpdated> jsonClientUpdateDto;
    @Autowired
    public ClientControllerTest(ClientService service) {
        this.service = service;
        mapper.registerModule(new JavaTimeModule());
    }
    @Test
    void getAllProducts() throws Exception {
        List<Client> clientsList = List.of(client1, client2, client3);
        Page<Client> page = new PageImpl<>(clientsList);

        when(service.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class),any(Optional.class) , any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(myEndpoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Client> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(3,res.content().size())
        );
    }

    @Test
    void getAllProducts_Dni() throws Exception {
        List<Client> clientsList = List.of(client1);
        Page<Client> page = new PageImpl<>(clientsList);
        Optional<String> dni = Optional.of("12345678A");
        when(service.findAll(eq(dni), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class),any(Optional.class) , any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?dni=12345678A")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Client> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }
    @Test
    void getAllProducts_Name() throws Exception {
        List<Client> clientsList = List.of(client1);
        Page<Client> page = new PageImpl<>(clientsList);
        Optional<String> name = Optional.of("John");
        when(service.findAll(any(Optional.class), eq(name), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class),any(Optional.class) , any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?name=John")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Client> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }
    @Test
    void getAllProducts_LastName() throws Exception {
        List<Client> clientsList = List.of(client1);
        Page<Client> page = new PageImpl<>(clientsList);
        Optional<String> last_name = Optional.of("Doe");
        when(service.findAll(any(Optional.class), any(Optional.class), eq(last_name), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class),any(Optional.class) , any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?last_name=Doe")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Client> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }

    @Test
    void getAllProducts_Age() throws Exception {
        List<Client> clientsList = List.of(client1);
        Page<Client> page = new PageImpl<>(clientsList);
        Optional<Integer> age = Optional.of(30);
        when(service.findAll(any(Optional.class), any(Optional.class), any(Optional.class), eq(age), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?age=30")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Client> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }

    @Test
    void getAllProducts_AgeMax() throws Exception {
        List<Client> clientsList = List.of(client1);
        Page<Client> page = new PageImpl<>(clientsList);
        Optional<Integer> ageMax = Optional.of(30);
        when(service.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), eq(ageMax), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?ageMax=30")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Client> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }

    @Test
    void getAllProducts_AgeMin() throws Exception {
        List<Client> clientsList = List.of(client1);
        Page<Client> page = new PageImpl<>(clientsList);
        Optional<Integer> ageMin = Optional.of(30);
        when(service.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), eq(ageMin), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?ageMin=30")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Client> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }

    @Test
    void getAllProducts_Phone() throws Exception {
        List<Client> clientsList = List.of(client1);
        Page<Client> page = new PageImpl<>(clientsList);
        Optional<String> phone = Optional.of("123456789");
        when(service.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), eq(phone), any(Optional.class), any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?phone=123456789")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Client> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }
    @Test
    void getAllProducts_Deleted() throws Exception {
        List<Client> clientsList = List.of(client3);
        Page<Client> page = new PageImpl<>(clientsList);
        Optional<Boolean> deleted = Optional.of(true);
        when(service.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), eq(deleted), any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?deleted=true")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Client> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }
    @Test
    void getAllProducts_AllParams() throws Exception {
        List<Client> clientsList = List.of(client1);
        Page<Client> page = new PageImpl<>(clientsList);
        Optional<Boolean> deleted = Optional.of(false);
        Optional<Integer> age = Optional.of(30);
        Optional<Integer> ageMax = Optional.of(30);
        Optional<Integer> ageMin = Optional.of(30);
        Optional<String> dni = Optional.of("12345678A");
        Optional<String> name = Optional.of("John");
        Optional<String> last_name = Optional.of("Doe");
        Optional<String> phone = Optional.of("123456789");
        when(service.findAll(eq(dni), eq(name), eq(last_name), eq(age), eq(ageMax), eq(ageMin), eq(phone), eq(deleted), any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"?dni=12345678A&name=John&last_name=Doe&age=30&ageMax=30&ageMin=30&phone=123456789&deleted=false")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Client> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1,res.content().size())
        );
    }
    @Test
    void getProduct() throws Exception {
        when(service.findById(client1.getId())).thenReturn(client1);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint + "/" + client1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).findById(client1.getId())
        );
    }
    @Test
    void getProduct_NotFound() throws Exception {
        when(service.findById(client1.getId()))
                .thenThrow(new ClientNotFound(client1.getId()));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint + "/" + client1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(service, times(1)).findById(client1.getId());
    }

    @Test
    void createProduct() throws Exception {
        ClientdtoNew clientdtoNew = ClientdtoNew.builder()
                .dni("48546678A")
                .name("jacobo")
                .last_name("hernandez")
                .age(30)
                .image("http://")
                .phone("123456789")
                .build();
        when(service.save(clientdtoNew)).thenReturn(client1);
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientCreateDto.write(clientdtoNew).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
        verify(service, times(1)).save(clientdtoNew);
    }
    @Test
    void createProduct_InvalidDni() throws Exception {
        ClientdtoNew clientdtoNew = ClientdtoNew.builder()
                .dni("48678A")
                .name("jacobo")
                .last_name("hernandez")
                .age(30)
                .phone("123456789")
                .image("http://")
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientCreateDto.write(clientdtoNew).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createProduct_InvalidName() throws Exception {
        ClientdtoNew clientdtoNew = ClientdtoNew.builder()
                .dni("48546678A")
                .name("")
                .last_name("hernandez")
                .age(30)
                .phone("123456789")
                .image("http://")
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientCreateDto.write(clientdtoNew).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createProduct_InvalidLastName() throws Exception {
        ClientdtoNew clientdtoNew = ClientdtoNew.builder()
                .dni("48546678A")
                .name("jacobo")
                .last_name("")
                .age(30)
                .phone("123456789")
                .image("http://")
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientCreateDto.write(clientdtoNew).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createProduct_InvalidAge() throws Exception {
        ClientdtoNew clientdtoNew = ClientdtoNew.builder()
                .dni("48546678A")
                .name("jacobo")
                .last_name("hernandez")
                .age(0)
                .image("http://")
                .phone("123456789")
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientCreateDto.write(clientdtoNew).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createProduct_InvalidPhone() throws Exception {
        ClientdtoNew clientdtoNew = ClientdtoNew.builder()
                .dni("48546678A")
                .name("jacobo")
                .last_name("hernandez")
                .age(30)
                .image("http://")
                .phone("126789")
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientCreateDto.write(clientdtoNew).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void createProduct_InvalidImage() throws Exception {
        ClientdtoNew clientdtoNew = ClientdtoNew.builder()
                .dni("48546678A")
                .name("jacobo")
                .last_name("hernandez")
                .age(30)
                .image("")
                .phone("126789543")
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientCreateDto.write(clientdtoNew).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }
    @Test
    void updateProduct() throws Exception {
        ClientdtoUpdated clientdtoUpdated = ClientdtoUpdated.builder()
                .dni("58439078A")
                .name("juan")
                .last_name("alias")
                .age(30)
                .phone("123456789")
                .image("http://")
                .build();
        when(service.findById(client1.getId())).thenReturn(client1);
        when(service.update(client1.getId(), clientdtoUpdated)).thenReturn(client1);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + client1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientUpdateDto.write(clientdtoUpdated).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        verify(service, times(1)).update(client1.getId(), clientdtoUpdated);
    }

    @Test
    void updateProduct_InvalidDni() throws Exception {
        ClientdtoUpdated clientdtoUpdated = ClientdtoUpdated.builder()
                .dni("5878A")
                .name("juan")
                .last_name("alias")
                .age(30)
                .phone("123456789")
                .image("http://")
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + client1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientUpdateDto.write(clientdtoUpdated).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(400, response.getStatus());
    }

    @Test
    void updateProduct_InvalidName() throws Exception {
        ClientdtoUpdated clientdtoUpdated = ClientdtoUpdated.builder()
                .dni("58535785A")
                .name("")
                .last_name("alias")
                .age(30)
                .phone("123456789")
                .image("http://")
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + client1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientUpdateDto.write(clientdtoUpdated).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(400, response.getStatus());
    }

    @Test
    void updateProduct_InvalidLastName() throws Exception {
        ClientdtoUpdated clientdtoUpdated = ClientdtoUpdated.builder()
                .dni("58535785A")
                .name("juan")
                .last_name("")
                .age(30)
                .phone("123456789")
                .image("http://")
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + client1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientUpdateDto.write(clientdtoUpdated).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(400, response.getStatus());
    }

    @Test
    void updateProduct_InvalidAge() throws Exception {
        ClientdtoUpdated clientdtoUpdated = ClientdtoUpdated.builder()
                .dni("58535785A")
                .name("juan")
                .last_name("alias")
                .age(0)
                .phone("123456789")
                .image("http://")
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + client1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientUpdateDto.write(clientdtoUpdated).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(400, response.getStatus());
    }

    @Test
    void updateProduct_InvalidPhone() throws Exception {
        ClientdtoUpdated clientdtoUpdated = ClientdtoUpdated.builder()
                .dni("58535785A")
                .name("juan")
                .last_name("alias")
                .age(30)
                .phone("123789")
                .image("http://")
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + client1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientUpdateDto.write(clientdtoUpdated).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(400, response.getStatus());
    }

    @Test
    void updateProduct_InvalidImage() throws Exception {
        ClientdtoUpdated clientdtoUpdated = ClientdtoUpdated.builder()
                .dni("58535785A")
                .name("juan")
                .last_name("alias")
                .age(30)
                .phone("123789543")
                .image("")
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + client1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientUpdateDto.write(clientdtoUpdated).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(400, response.getStatus());
    }

    @Test
    void updateProduct_NotFound() throws Exception {
        ClientdtoUpdated clientdtoUpdated = ClientdtoUpdated.builder()
                .dni("58439078A")
                .name("juan")
                .last_name("alias")
                .age(30)
                .image("http://")
                .phone("123456789")
                .build();
        when(service.update(client1.getId(), clientdtoUpdated)).thenThrow(new ClientNotFound(client1.getId()));
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + client1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientUpdateDto.write(clientdtoUpdated).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(404, response.getStatus());

    }
    @Test
    void deleteProduct() throws Exception {
        doNothing().when(service).deleteById(client1.getId());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndpoint + "/" + client1.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(204, response.getStatus())
        );
        verify(service, times(1)).deleteById(client1.getId());
    }

    @Test
    void deleteProduct_NotFound() throws Exception {
        doThrow(new ClientNotFound(client1.getId())).when(service).deleteById(client1.getId());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndpoint + "/" + client1.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(service, times(1)).deleteById(client1.getId());
    }
    @Test
    void actualizarImagen() throws Exception{
        String localEndpoint = "http://localhost:8080/image";
        UUID id = UUID.randomUUID();

        when(service.updateImage(eq(id), any(MultipartFile.class), anyBoolean())).thenReturn(client1);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "filename.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido del archivo".getBytes()
        );

        MockHttpServletResponse response = mockMvc.perform(
                multipart(localEndpoint+"/"+ id)
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
        ).andReturn().getResponse();

        Client res = mapper.readValue(response.getContentAsString(), Client.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(client1, res)
        );
        verify(service, times(1)).updateImage(eq(id), any(MultipartFile.class), anyBoolean());
    }
    @Test
    void actualizarImagenErrorArchivoNoValido() throws Exception {
        String localEndpoint = "http://localhost:8080/image";
        UUID id = UUID.randomUUID();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "filename.txt", // Extensión de archivo no válida
                MediaType.TEXT_PLAIN_VALUE, // Tipo de contenido no válido
                "contenido del archivo".getBytes()
        );

        MockHttpServletResponse response = mockMvc.perform(
                multipart(localEndpoint + "/" + id)
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        verify(service, times(0)).updateImage(any(UUID.class), any(MultipartFile.class), anyBoolean()); // No se debe llamar al servicio
    }

    @Test
    void actualizarImagenErrorArchivoVacio() throws Exception {
        String localEndpoint = "http://localhost:8080/image";
        UUID id = UUID.randomUUID();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                new byte[0] // Archivo vacío
        );

        MockHttpServletResponse response = mockMvc.perform(
                multipart(localEndpoint + "/" + id)
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );

        verify(service, times(0)).updateImage(any(UUID.class), any(MultipartFile.class), anyBoolean()); // No se debe llamar al servicio
    }
}
