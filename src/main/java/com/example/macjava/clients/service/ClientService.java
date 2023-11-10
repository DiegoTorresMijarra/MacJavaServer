package com.example.macjava.clients.service;

import com.example.macjava.clients.dto.ClientdtoNew;
import com.example.macjava.clients.dto.ClientdtoUpdated;
import com.example.macjava.clients.models.Client;

import java.util.List;
import java.util.UUID;

public interface ClientService {
List<Client> findAll();
Client findById(UUID id);
Client save(ClientdtoNew client);
Client update(UUID id, ClientdtoUpdated client);
void deleteById(UUID id);
}
