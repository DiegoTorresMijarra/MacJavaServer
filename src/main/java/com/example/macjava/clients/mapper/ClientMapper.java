package com.example.macjava.clients.mapper;

import com.example.macjava.clients.dto.ClientdtoNew;
import com.example.macjava.clients.dto.ClientdtoUpdated;
import com.example.macjava.clients.models.Client;

import java.time.LocalDate;
import java.util.UUID;

public class ClientMapper {
public Client toClientNew(ClientdtoNew client) {
    return Client.builder()
            .id(UUID.randomUUID())
            .dni(client.getDni())
            .name(client.getName())
            .last_name(client.getLast_name())
            .age(client.getAge())
            .phone(client.getPhone())
            .image(client.getImage())
            .deleted(false)
            .fecha_cre(LocalDate.now())
            .fecha_act(LocalDate.now())
    .build();
}
public Client toClientUpdate(ClientdtoUpdated clientdto,Client client) {
    return Client.builder()
            .id(client.getId())
            .dni(clientdto.getDni())
            .name(clientdto.getName())
            .last_name(clientdto.getLast_name())
            .age(clientdto.getAge())
            .phone(clientdto.getPhone())
            .image(clientdto.getImage())
            .deleted(clientdto.isDeleted())
            .fecha_act(LocalDate.now())
            .fecha_cre(client.getFecha_cre())
            .build();
}
}
