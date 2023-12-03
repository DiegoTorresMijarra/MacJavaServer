package com.example.macjava.rest.workers.services;

import com.example.macjava.rest.workers.dto.WorkersSaveDto;
import com.example.macjava.rest.workers.dto.WorkersUpdateDto;
import com.example.macjava.rest.workers.models.Workers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para la entidad Workers
 */
public interface WorkersService {
    /**
     * Busca todos los trabajadores con los criterios de búsqueda
      * @param name opcional: nombre del trabajador
     * @param surname opcional: apellido del trabajador
     * @param age opcional: edad del trabajador
     * @param phone opcional: telefono del trabajador
     * @param isDeleted opcional: indica si el trabajador esta borrado o no
     * @param antiquierityMin opcional: antiguedad minima del trabajador
     * @param antiquierityMax opcional: antiguedad maxima del trabajador
     * @param positionId opcional: id de la posición del trabajador
     * @param pageable paginación
     * @return  lista de trabajadores encontrados
     */
    Page<Workers> findAll(Optional<String> name, Optional<String> surname, Optional<Integer> age, Optional<String> phone,
                          Optional<Boolean> isDeleted, Optional<Integer> antiquierityMin, Optional<Integer> antiquierityMax,Optional<Integer> positionId, Pageable pageable);

    /**
     * Busca un trabajador por su id
     * @param uuid id del trabajador a buscar
     * @return trabajador encontrado
     */
    Workers findByUUID(UUID uuid);

    /**
     * Crea un trabajador
     * @param workers dto con los datos del trabajador
     * @return trabajador creado
     */
    Workers save(WorkersSaveDto workers);

    /**
     * Actualiza un trabajador
     * @param uuid id del trabajador a actualizar
     * @param workers dto con los datos a actualizar
     * @return
     */
    Workers update(UUID uuid, WorkersUpdateDto workers);

    /**
     * Borra un trabajador
     * @param uuid  id del trabajador a borrar
     */
    void deleteByUUID(UUID uuid);

    /**
     * Busca un trabajador por su dni
     * @param dni   dni del trabajador a buscar
     * @return trabajador encontrado
     */
    Workers findByDni(String dni);

    /**
     * Busca trabajadores borrados o no (borrado logico)
     * @param isDeleted indica si los trabajadores buscados estan borrados o no
     * @return Lista de trabajadores encontrados
     */
    List<Workers> findByIsDeleted(Boolean isDeleted);

    /**
     * Actualiza el campo isDeleted a true
     * @param uuid id del trabajador a actualizar
     */
    void updateIsDeletedToTrueById(UUID uuid); //podria ser workers
}
