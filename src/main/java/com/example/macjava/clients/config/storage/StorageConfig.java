package com.example.macjava.clients.config.storage;

import com.example.macjava.clients.storage.service.storageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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

    @PostConstruct
    public void init() {
        if (deleteAll.equals("true")) {
            log.info("Borrando ficheros de almacenamiento...");
            service.deleteAll();
        }

        service.init();
    }
}
