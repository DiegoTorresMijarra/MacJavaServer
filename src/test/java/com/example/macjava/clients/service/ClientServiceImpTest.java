package com.example.macjava.clients.service;

import com.example.macjava.clients.config.webSocket.WebSocketConfig;
import com.example.macjava.clients.config.webSocket.WebSocketHandler;
import com.example.macjava.clients.dto.ClientdtoNew;
import com.example.macjava.clients.dto.ClientdtoUpdated;
import com.example.macjava.clients.exceptions.ClientNotFound;
import com.example.macjava.clients.mapper.ClientMapper;
import com.example.macjava.clients.models.Client;
import com.example.macjava.clients.repository.ClientsRepository;
import com.example.macjava.clients.storage.service.storageService;
import com.example.macjava.clients.webSocket.mapper.ClientNotificationMapper;
import com.example.macjava.clients.webSocket.models.Notification;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImpTest {
    Client client1 = Client.builder()
            .id(UUID.randomUUID())
            .dni("12345678A")
            .name("John")
            .last_name("Doe")
            .age(30)
            .phone("123456789")
            .image("https://via.placeholder.com/150")
            .deleted(false)
            .fecha_act(LocalDate.now())
            .fecha_cre(LocalDate.now())
            .build();

    Client client2 = Client.builder()
            .id(UUID.randomUUID())
            .dni("98765432B")
            .name("Jane")
            .last_name("Smith")
            .age(25)
            .phone("987654321")
            .image("https://via.placeholder.com/150")
            .deleted(false)
            .fecha_act(LocalDate.now())
            .fecha_cre(LocalDate.now())
            .build();
    Client client3 = Client.builder()
            .id(UUID.randomUUID())
            .dni("63957327T")
            .name("Alberto")
            .last_name("zarza")
            .age(23)
            .phone("123456789")
            .image("https://via.placeholder.com/150")
            .deleted(true)
            .fecha_act(LocalDate.now())
            .fecha_cre(LocalDate.now())
            .build();
    ClientdtoNew clientdtoNew = ClientdtoNew.builder()
            .dni("48546678A")
            .name("jacobo")
            .last_name("hernandez")
            .age(30)
            .phone("123456789")
            .image("https://via.placeholder.com/150")
            .build();
    ClientdtoUpdated clientdtoUpdated = ClientdtoUpdated.builder()
            .dni("58439078A")
            .name("juan")
            .last_name("alias")
            .age(30)
            .phone("123456789")
            .image("https://via.placeholder.com/150")
            .build();
    WebSocketHandler webSocketHandlerMock = mock(WebSocketHandler.class);
    @Mock
    private ClientsRepository repository;
    private final ClientMapper clientMapper=new ClientMapper();
    @Mock
    private WebSocketConfig webSocketConfig;
    @Mock
    storageService storageService;
    @Mock
    private ClientNotificationMapper clientNotificationMapper;
    @InjectMocks
    ClientServiceImp service;
    @Captor
    private ArgumentCaptor<Client> clientCaptor;
    @BeforeEach
    void setUp() {
        service.setWebSocketService(webSocketHandlerMock);
    }
    @Test
    void findAll_ShouldReturnAllClients_WhenNoParametersProvided() {
        List<Client> expectedClients = Arrays.asList(client1, client2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Client> actualPage = service.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(), Optional.empty(), pageable);
        assertAll("findAll",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnClientsByDni_WhenDniParameterProvided() {
        Optional<String> dni = Optional.of("12345678A");
        List<Client> expectedClients = List.of(client1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Client> actualPage = service.findAll(dni, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(), Optional.empty(), pageable);
        assertAll("findAllWithDni",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnClientsByName_WhenNameParameterProvided() {
        Optional<String> name = Optional.of("John");
        List<Client> expectedClients = List.of(client1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Client> actualPage = service.findAll(Optional.empty(), name, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(), pageable);
        assertAll("findAllWithName",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnClientsByLast_name_WhenLast_nameParameterProvided() {
        Optional<String> last_name = Optional.of("Doe");
        List<Client> expectedClients = List.of(client1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Client> actualPage = service.findAll(Optional.empty(), Optional.empty(), last_name, Optional.empty(),Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll("findAllWithLast_name",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnClientsByAge_WhenAgeParameterProvided() {
        Optional<Integer> age = Optional.of(30);
        List<Client> expectedClients = List.of(client1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Client> actualPage = service.findAll(Optional.empty(), Optional.empty(),Optional.empty(), age,Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll("findAllWithAge",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnClientsByPhone_WhenPhoneParameterProvided() {
        Optional<String> phone = Optional.of("123456789");
        List<Client> expectedClients = List.of(client1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Client> actualPage = service.findAll(Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), phone, Optional.empty(), pageable);
        assertAll("findAllWithPhone",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnClientsByDeleted_WhenDeletedParameterProvided() {
        Optional<Boolean> deleted = Optional.of(true);
        List<Client> expectedClients = List.of(client3);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Client> actualPage = service.findAll(Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), deleted, pageable);
        assertAll("findAllWithDeleted",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnClientsByAgeAndPhone_WhenAgeAndPhoneParameterProvided() {
        Optional<Integer> age = Optional.of(30);
        Optional<String> phone = Optional.of("123456789");
        List<Client> expectedClients = List.of(client1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Client> actualPage = service.findAll(Optional.empty(), Optional.empty(),Optional.empty(), age,Optional.empty(), Optional.empty(), phone,Optional.empty(), pageable);
        assertAll("findAllWithAgeAndPhone",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ShouldReturnClientsByAgeMax_WhenAgeMaxParameterProvided() {
        Optional<Integer> AgeMax = Optional.of(35);
        List<Client> expectedClients = List.of(client1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Client> actualPage = service.findAll(Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(), AgeMax, Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll("findAllWithDeleted",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ShouldReturnClientsByAgeMin_WhenAgeMinParameterProvided() {
        Optional<Integer> AgeMin = Optional.of(25);
        List<Client> expectedClients = List.of(client1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(expectedClients);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Client> actualPage = service.findAll(Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(), Optional.empty(), AgeMin, Optional.empty(), Optional.empty(), pageable);
        assertAll("findAllWithDeleted",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findById() {
        when(repository.findById(client1.getId())).thenReturn(Optional.of(client1));

        Client result=service.findById(client1.getId());

        assertAll("FindById",
                () -> assertNotNull(result),
                () -> assertEquals(client1, result),
                () -> verify(repository, times(1)).findById(client1.getId())
        );
    }

    @Test
    void findByIdthrow(){
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ClientNotFound.class, () -> {
            service.findById(id);
        });

        verify(repository, times(1)).findById(id);

    }
    @Test
    void save() {
        Client clientnew = clientMapper.toClientNew(clientdtoNew);
        when(repository.save(any(Client.class))).thenReturn(clientnew);

        Client result = service.save(clientdtoNew);

        assertAll("Save",
                () -> assertNotNull(result),
                () -> assertEquals(clientnew.getDni(), result.getDni()),
                () -> verify(repository, times(1)).save(any(Client.class))
        );
    }

    @Test
    void update() {
        when(repository.findById(client1.getId())).thenReturn(Optional.of(client1));
        Client updatedClient = clientMapper.toClientUpdate(clientdtoUpdated, client1);
        when(repository.save(updatedClient)).thenReturn(updatedClient);
        Client result = service.update(client1.getId(), clientdtoUpdated);
        assertAll("Update",
                () -> assertNotNull(result),
                () -> assertEquals(client1.getId(), result.getId()),
                () -> assertEquals(updatedClient.getDni(), result.getDni()),
                () -> verify(repository, times(1)).findById(client1.getId()),
                () -> verify(repository, times(1)).save(updatedClient)
        );
    }

    @Test
    void updateClientNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ClientNotFound.class, () -> service.update(id, clientdtoUpdated));
        verify(repository, times(1)).findById(id);
    }
    @Test
    void deleteById() {
        when(repository.findById(client1.getId())).thenReturn(Optional.of(client1));
        service.deleteById(client1.getId());
        assertAll("DeleteById",
                () -> verify(repository, times(1)).findById(client1.getId()),
                () -> verify(repository, times(1)).updateIsDeletedToTrueById(client1.getId())
        );
    }
    @Test
    void deleteByIdNotFound() {
        when(repository.findById(client1.getId())).thenReturn(Optional.empty());
        assertThrows(ClientNotFound.class, () -> service.deleteById(client1.getId()));
        verify(repository, times(1)).findById(client1.getId());
    }

    @Test
    void updateImage_ShouldUpdateImageAndReturnProduct_WhenValidIdAndImageProvided() throws IOException {
        Client clientupdated = Client.builder()
                .id(client1.getId())
                .dni(client1.getDni())
                .name(client1.getName())
                .last_name(client1.getLast_name())
                .age(client1.getAge())
                .image("https://example.com/images/image.jpg")
                .phone(client1.getPhone())
                .deleted(client1.isDeleted())
                .fecha_act(LocalDate.now())
                .fecha_cre(client1.getFecha_cre())
                .build();

        when(repository.findById(client1.getId())).thenReturn(Optional.of(client1));
        when(storageService.store(any(MultipartFile.class))).thenReturn("image-stored.jpg");
        when(storageService.getUrl("image-stored.jpg")).thenReturn("https://example.com/images/image-stored.jpg");
        doReturn(clientupdated).when(repository).save(any(Client.class));

        Client updatedClient = service.updateImage(client1.getId(), mock(MultipartFile.class), true);

        assertEquals(clientupdated.getImage(), updatedClient.getImage());

        verify(repository, times(1)).findById(client1.getId());
        verify(storageService, times(1)).store(any(MultipartFile.class));
        verify(storageService, times(1)).getUrl("image-stored.jpg");
        verify(repository, times(1)).save(any(Client.class));

    }

    @Test
    void onChange() throws IOException {
        doNothing().when(webSocketHandlerMock).sendMessage(any(String.class));
        service.onChange(Notification.Tipo.CREATE, client1);
    }
}