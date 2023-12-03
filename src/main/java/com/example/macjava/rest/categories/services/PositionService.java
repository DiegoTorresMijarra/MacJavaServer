package com.example.macjava.rest.categories.services;

import com.example.macjava.rest.categories.dto.PositionSaveDto;
import com.example.macjava.rest.categories.dto.PositionUpdateDto;
import com.example.macjava.rest.categories.models.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Servicio para la entidad Position
 */
public interface PositionService {
    /**
     * Obtiene todas las posiciones que coincidan con los parámetros
     * @param name opcional: nombre de la posición
     * @param salaryMin opcional: salario mínimo de la posición
     * @param salaryMax opcional: salario máximo de la posición
     * @param isDeleted opcional: si la posición está eliminada
     * @param pageable paginación
     * @return Page de las posiciones que coinciden con los parámetros
     */
    Page<Position> findAll(Optional<String> name, Optional<Integer> salaryMin, Optional<Integer> salaryMax, Optional<Boolean> isDeleted, Pageable pageable);

    /**
     * Obtiene la posición con el id dado
     * @param id id de la posición a obtener
     * @return Posicion con ese id
     */
    Position findById(Long id);

    /**
     * Guarda una posición
     * @param position posición a guardar
     * @return posición guardada
     */
    Position save(PositionSaveDto position);

    /**
     * Actualiza una posición
     * @param id id de la posición a actualizar
     * @param position posición con los datos a actualizar
     * @return posición actualizada
     */
    Position update(Long id, PositionUpdateDto position);

    /**
     * Elimina una posición
     * @param id id de la posición a eliminar
     */
    void deleteById(Long id);

    /**
     * Actualiza el campo isDeleted a true
     * @param id id de la posición a actualizar
     */
    void updateIsDeletedToTrueById(Long id);
}
