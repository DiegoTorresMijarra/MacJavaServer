package com.example.macjava.rest.clients.webSocket.dto;

public record ClientNotificationResponse(
        String id,
        String dni,
        String name,
        String last_name,
        int age,
        String phone,
        String image,
        boolean deleted,
        String fecha_cre,
        String fecha_act
) {

}
