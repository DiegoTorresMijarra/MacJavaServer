package com.example.macjava.rest.clients.config.storage;

import com.example.macjava.rest.clients.storage.service.storageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración para el almacenamiento de ficheros
 */
@Configuration
@Slf4j
public class StorageConfig {
    private final storageService service;

    @Value("${upload.delete}")
    private String deleteAll;

    @Autowired
    public StorageConfig(storageService service) {
        this.service = service;
    }

    /**
     * Inicializa el almacenamiento
     *  - Borra todos los ficheros si la variable deleteAll es true
     */
    @PostConstruct
    public void init() {
        if (deleteAll.equals("true")) {
            log.info("Borrando ficheros de almacenamiento...");
            service.deleteAll();
        }

        service.init();
    }
}
