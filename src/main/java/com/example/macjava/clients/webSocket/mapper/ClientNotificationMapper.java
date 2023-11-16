package com.example.macjava.clients.webSocket.mapper;

import com.example.macjava.clients.models.Client;
import com.example.macjava.clients.webSocket.dto.ClientNotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class ClientNotificationMapper {
    public static ClientNotificationResponse toResponse(Client client){
        return new ClientNotificationResponse(
                client.getId().toString(),
                client.getDni(),
                client.getName(),
                client.getLast_name(),
                client.getAge(),
                client.getPhone(),
                client.getImage(),
                client.isDeleted(),
                client.getFecha_cre().toString(),
                client.getFecha_act().toString()
        );
    }
}
