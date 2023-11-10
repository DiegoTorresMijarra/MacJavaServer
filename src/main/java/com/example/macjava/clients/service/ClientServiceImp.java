package com.example.macjava.clients.service;

import com.example.macjava.clients.config.WebSocketConfig;
import com.example.macjava.clients.config.WebSocketHandler;
import com.example.macjava.clients.dto.ClientdtoNew;
import com.example.macjava.clients.dto.ClientdtoUpdated;
import com.example.macjava.clients.exceptions.ClientNotFound;
import com.example.macjava.clients.mapper.ClientMapper;
import com.example.macjava.clients.models.Client;
import com.example.macjava.clients.repository.ClientsRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@CacheConfig(cacheNames = {"clientes"})
@Service
public class ClientServiceImp implements ClientService{
    ClientsRepository repository;
    ClientMapper map=new ClientMapper();

    private final WebSocketConfig webSocketConfig;
    private final ObjectMapper mapper;
    private final ClientNotificationMapper clientNotificationMapper;
    private WebSocketHandler webSocketService;
    @Autowired
    public ClientServiceImp(ClientsRepository repository, WebSocketConfig webSocketConfig, ClientNotificationMapper clientNotificationMapper){
        this.repository = repository;
        mapper= new ObjectMapper();
        this.webSocketConfig = webSocketConfig;
        this.clientNotificationMapper = clientNotificationMapper;
        webSocketService = webSocketConfig.webSocketClientsHandler();
    }
    @Override
    public List<Client> findAll() {
        return repository.findAll();
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
        onChange(Notification.Tipo.DELETE, OptionalClient);
        repository.updateIsDeletedToTrueById(id);
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


            // Enviamos el mensaje a los clientes ws con un hilo, si hay muchos clientes, puede tardar
            // no bloqueamos el hilo principal que atiende las peticiones http
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