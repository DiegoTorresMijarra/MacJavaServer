package com.example.macjava.clients.service;

import com.example.macjava.clients.dto.ClientdtoNew;
import com.example.macjava.clients.dto.ClientdtoUpdated;
import com.example.macjava.clients.models.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface ClientService {
Page<Client> findAll(Optional<String> dni,Optional<String> name, Optional<String> last_name, Optional<Integer> age, Optional<Integer> ageMax, Optional<Integer> ageMin, Optional<String> phone, Optional<Boolean> deleted, Pageable pageable);
Client findById(UUID id);
Client save(ClientdtoNew client);
Client update(UUID id, ClientdtoUpdated client);
void deleteById(UUID id);
Client updateImage (UUID id, MultipartFile image, Boolean withUrl);
}
