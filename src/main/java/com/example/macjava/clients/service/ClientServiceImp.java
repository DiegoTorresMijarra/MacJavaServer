package com.example.macjava.clients.service;

import com.example.macjava.clients.dto.ClientdtoNew;
import com.example.macjava.clients.dto.ClientdtoUpdated;
import com.example.macjava.clients.exceptions.ClientNotFound;
import com.example.macjava.clients.mapper.ClientMapper;
import com.example.macjava.clients.models.Client;
import com.example.macjava.clients.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class ClientServiceImp implements ClientService{
    ClientsRepository repository;
    ClientMapper map=new ClientMapper();
    @Autowired
    public ClientServiceImp(ClientsRepository repository){
        this.repository = repository;
    }
    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public Client findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ClientNotFound(id));
    }

    @Override
    public Client save(ClientdtoNew client) {
        Client ClientSave = map.toClientNew(client);
        return repository.save(ClientSave);
    }

    @Override
    public Client update(UUID id, ClientdtoUpdated client) {
        Client OptionalClient = findById(id);
        Client ClientUpdate = map.toClientUpdate(client, OptionalClient);
        return repository.save(ClientUpdate);
    }

    @Override
    public void deleteById(UUID id) {
        Client OptionalClient = findById(id);
        repository.updateIsDeletedToTrueById(id);
    }
}