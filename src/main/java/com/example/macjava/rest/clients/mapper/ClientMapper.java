package com.example.macjava.rest.clients.mapper;

import com.example.macjava.rest.clients.dto.ClientdtoNew;
import com.example.macjava.rest.clients.dto.ClientdtoUpdated;
import com.example.macjava.rest.clients.models.Client;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Mapeador de clientes
 */
public class ClientMapper {
    /**
     * Convierte un objeto de transferencia de datos de cliente nuevo a un modelo de cliente
     * @param client Objeto de transferencia de datos de cliente nuevo
     * @return Modelo de cliente
     */
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
     /**
     * Convierte un objeto de transferencia de datos de cliente actualizado a un modelo de cliente
     * @param clientdto Objeto de transferencia de datos de cliente actualizado
     * @param client Modelo de cliente
     * @return Modelo de cliente
     */
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
