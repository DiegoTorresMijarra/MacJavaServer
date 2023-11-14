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
import com.example.macjava.clients.webSocket.dto.ClientNotificationResponse;
import com.example.macjava.clients.webSocket.mapper.ClientNotificationMapper;
import com.example.macjava.clients.webSocket.models.Notification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
@CacheConfig(cacheNames = {"clientes"})
@Service
public class ClientServiceImp implements ClientService{
    ClientsRepository repository;
    storageService storageService;
    ClientMapper map=new ClientMapper();
    private final WebSocketConfig webSocketConfig;
    private final ObjectMapper mapper;
    private final ClientNotificationMapper clientNotificationMapper;
    private WebSocketHandler webSocketService;
    @Autowired
    public ClientServiceImp(ClientsRepository repository,storageService storageService ,WebSocketConfig webSocketConfig, ClientNotificationMapper clientNotificationMapper){
        this.repository = repository;
        this.storageService = storageService;
        mapper= new ObjectMapper();
        this.webSocketConfig = webSocketConfig;
        this.clientNotificationMapper = clientNotificationMapper;
        webSocketService = webSocketConfig.webSocketClientsHandler();
    }
    @Override
    public Page<Client> findAll(Optional<String> dni,Optional<String> name, Optional<String> last_name, Optional<Integer> age, Optional<Integer> ageMax, Optional<Integer> ageMin, Optional<String> phone, Optional<Boolean> deleted, Pageable pageable) {
        // Criterio de búsqueda por dni
        Specification<Client> specDniClient = (root, query, criteriaBuilder) ->
                dni.map(m -> criteriaBuilder.equal(root.get("dni"), m)) // Buscamos por dni
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))); // Si no hay dni, no filtramos
        // Criterio de búsqueda por nombre
        Specification<Client> specNameClient = (root, query, criteriaBuilder) ->
                name.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + m.toLowerCase() + "%")) // Buscamos por nombre
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))); // Si no hay nombre, no filtramos
        // Criterio de búsqueda por apellido
        Specification<Client> speclastNameClient = (root, query, criteriaBuilder) ->
                last_name.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("last_name")), "%" + m.toLowerCase() + "%")) // Buscamos por apellido
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))); // Si no hay apellido, no filtramos
        // Criterio de búsqueda por edad
        Specification<Client> specAgeClient = (root, query, criteriaBuilder) ->
                age.map(m -> criteriaBuilder.equal(root.get("age"), m)) // Buscamos por edad
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))); // Si no hay edad, no filtramos
        // Criterio de búsqueda por telefono
        Specification<Client> specPhoneClient = (root, query, criteriaBuilder) ->
                phone.map(m -> criteriaBuilder.equal(root.get("phone"), m)) // Buscamos por telefono
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))); // Si no hay telefono, no filtramos
        // Criterio de búsqueda por deleted
        Specification<Client> specdeleted = (root, query, criteriaBuilder) ->
                deleted.map(d -> criteriaBuilder.equal(root.get("deleted"), d)) // Buscamos por deleted
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))); // Si no hay deleted, no filtramos
        // Criterio de búsqueda por año maximo
        Specification<Client> specAgeMax = (root, query, criteriaBuilder) ->
                ageMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("age"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        // Criterio de búsqueda por año minimo
        Specification<Client> specAgeMin = (root, query, criteriaBuilder) ->
                ageMin.map(p -> criteriaBuilder.greaterThanOrEqualTo(root.get("age"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        // Union de todas las especificaciones en una
        Specification<Client> criterio = Specification.where(specNameClient)
                .and(speclastNameClient)
                .and(specAgeClient)
                .and(specPhoneClient)
                .and(specdeleted)
                .and(specDniClient)
                .and(specAgeMax)
                .and(specAgeMin);
        return repository.findAll(criterio, pageable);
    }

    @Override
    @Cacheable
    public Client findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ClientNotFound(id));
    }

    @Override
    @CachePut
    public Client save(ClientdtoNew client) {
        Client ClientSave = map.toClientNew(client);
        onChange(Notification.Tipo.CREATE, ClientSave);
        return repository.save(ClientSave);
    }
    @Override
    @CachePut
    @Transactional
    public Client update(UUID id, ClientdtoUpdated client) {
        Client OptionalClient = findById(id);
        Client ClientUpdate = map.toClientUpdate(client, OptionalClient);
        onChange(Notification.Tipo.UPDATE, ClientUpdate);
        return repository.save(ClientUpdate);
    }

    @Override
    @CacheEvict
    @Transactional
    public void deleteById(UUID id) {
        Client OptionalClient = findById(id);
        if (OptionalClient.getImage() != null && !OptionalClient.getImage().equals("https://via.placeholder.com/150")) {
            storageService.delete(OptionalClient.getImage());
        }
        onChange(Notification.Tipo.DELETE, OptionalClient);
        repository.updateIsDeletedToTrueById(id);
    }

    @Override
    @CachePut
    @Transactional
    public Client updateImage(UUID id, MultipartFile image, Boolean withUrl) {
        var clientActual = repository.findById(id).orElseThrow(() -> new ClientNotFound(id));

        if (clientActual.getImage() != null && !clientActual.getImage().equals("https://via.placeholder.com/150")) {
            storageService.delete(clientActual.getImage());
        }

        String imageStored = storageService.store(image);
        String imageUrl = !withUrl ? imageStored : storageService.getUrl(imageStored);
        var clientActualized = Client.builder()
                .id(clientActual.getId())
                .dni(clientActual.getDni())
                .name(clientActual.getName())
                .last_name(clientActual.getLast_name())
                .age(clientActual.getAge())
                .phone(clientActual.getPhone())
                .image(imageUrl)
                .deleted(clientActual.isDeleted())
                .fecha_cre(clientActual.getFecha_cre())
                .fecha_act(LocalDate.now())
                .build();

        var clientUpdated = repository.save(clientActualized);
        onChange(Notification.Tipo.UPDATE, clientUpdated);
        return clientUpdated;
    }

    void onChange(Notification.Tipo tipo, Client data) {

        if (webSocketService == null) {
            webSocketService = this.webSocketConfig.webSocketClientsHandler();
        }

        try {
            Notification<ClientNotificationResponse> notification = new Notification<>(
                    "CLIENTES",
                    tipo,
                    ClientNotificationMapper.toResponse(data),
                    LocalDateTime.now().toString()
            );

            String json = mapper.writeValueAsString((notification));

            Thread senderThread = new Thread(() -> {
                try {
                    webSocketService.sendMessage(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Para los test
    public void setWebSocketService(WebSocketHandler webSocketHandlerMock) {
        this.webSocketService = webSocketHandlerMock;
    }
}