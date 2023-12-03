package com.example.macjava.rest.clients.config.webSocket;

import java.io.IOException;

/**
 * Interfaz para el envío de mensajes por el WebSocket
 */
public interface WebSocketSender {
    void sendMessage(String message) throws IOException;

    void sendPeriodicMessages() throws IOException;
}
