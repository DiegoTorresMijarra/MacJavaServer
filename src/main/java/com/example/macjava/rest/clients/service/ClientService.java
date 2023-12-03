package com.example.macjava.rest.clients.service;

import com.example.macjava.rest.clients.dto.ClientdtoNew;
import com.example.macjava.rest.clients.dto.ClientdtoUpdated;
import com.example.macjava.rest.clients.models.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz para el servicio de Clientes
 */
public interface ClientService {

    /**
     * Obtiene todos los clientes con los filtros opcionales
     * @param dni opcional dni del cliente
     * @param name opcional nombre del cliente
     * @param last_name opcional apellidos del cliente
     * @param age opcional edad del cliente
     * @param ageMax opcional edad maxima del cliente
     * @param ageMin opcional edad minima del cliente
     * @param phone opcional telefono del cliente
     * @param deleted  opcional eliminado del cliente
     * @param pageable Paginacion
     * @return pagina de clientes encontrados
     */
    Page<Client> findAll(Optional<String> dni, Optional<String> name, Optional<String> last_name, Optional<Integer> age, Optional<Integer> ageMax, Optional<Integer> ageMin, Optional<String> phone, Optional<Boolean> deleted, Pageable pageable);

    /**
     * Obtiene un cliente por su id
     * @param id id del cliente a buscar
     * @return cliente encontrado
     */
    Client findById(UUID id);

    /**
     * Guarda un cliente
     * @param client cliente a guardar
     * @return cliente guardado
     */
    Client save(ClientdtoNew client);

    /**
     * Actualiza un cliente
     * @param id id del cliente a actualizar
     * @param client cliente con los datos a actualizar
     * @return cliente actualizado
     */
    Client update(UUID id, ClientdtoUpdated client);

    /**
     * Elimina un cliente por su id
      * @param id id del cliente a eliminar
     */
    void deleteById(UUID id);

    /**
     * Actualiza la imagen de un cliente
     * @param id id del cliente a actualizar
     * @param image imagen a actualizar
     * @param withUrl si se quiere obtener la url de la imagen
     */
    Client updateImage (UUID id, MultipartFile image, Boolean withUrl);
}
