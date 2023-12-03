package com.example.macjava.rest.categories.services;

import com.example.macjava.rest.categories.dto.PositionSaveDto;
import com.example.macjava.rest.categories.dto.PositionUpdateDto;
import com.example.macjava.rest.categories.exceptions.PositionBadRequest;
import com.example.macjava.rest.categories.exceptions.PositionNotFound;
import com.example.macjava.rest.categories.mappers.PositionMapper;
import com.example.macjava.rest.categories.models.Position;
import com.example.macjava.rest.categories.repositories.PositionCrudRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementación del servicio de Posiciones
 */
@Service
@Slf4j
@Cacheable(cacheNames = "positions")
public class PositionServiceImpl implements PositionService{
    private final PositionCrudRepository positionCrudRepository;

    @Autowired
    public PositionServiceImpl(PositionCrudRepository positionCrudRepository) {
        log.info("Iniciando el Servicio de Posiciones");
        this.positionCrudRepository = positionCrudRepository;
    }

    /**
     * Obtiene todas las posiciones que coincidan con los parámetros
     * @param name opcional: nombre de la posición
     * @param salaryMin opcional: salario mínimo de la posición
     * @param salaryMax opcional: salario máximo de la posición
     * @param isDeleted opcional: si la posición está eliminada
     * @param pageable paginación
     * @return Page de las posiciones que coinciden con los parámetros
     */
    @Override
    public Page<Position> findAll(Optional<String> name, Optional<Integer> salaryMin, Optional<Integer> salaryMax, Optional<Boolean> isDeleted, Pageable pageable) {
        log.info("Buscando todas las Posiciones");
       //Criterio por nombre
        Specification<Position> specName=(root, query, criteriaBuilder)->
                name.map(d->criteriaBuilder.equal(root.get("name"),d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        // Criterio por salario minimo
        Specification<Position> specSalaryMin=(root, query, criteriaBuilder)->
                salaryMin.map(d->criteriaBuilder.greaterThanOrEqualTo(root.get("salary"),d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        // Criterio por salario maximo
        Specification<Position> specSalaryMax=(root, query, criteriaBuilder)->
                salaryMax.map(d->criteriaBuilder.lessThanOrEqualTo(root.get("salary"),d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Criterio por isDeleted
        Specification<Position> specIsDeleted=(root, query, criteriaBuilder)->
                isDeleted.map(d->criteriaBuilder.equal(root.get("isDeleted"),d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Position> specification=Specification.where(specName)
                .and(specSalaryMin)
                .and(specSalaryMax)
                .and(specIsDeleted);

        return positionCrudRepository.findAll(specification,pageable);
    }

    /**
     * Obtiene la posición con el id dado
     * @param id id de la posición a obtener
     * @return Posicion con ese id
     * @throws PositionNotFound Excepción que se lanza si no se encuentra la posición
     */
    @Override
    @Cacheable(key="#id")
    public Position findById(Long id) {
        log.info("Buscando la Posición con el id: " + id);
        return positionCrudRepository.findById(id).orElseThrow(() -> new PositionNotFound(id));
    }

    /**
     * Guarda una posición
     * @param position posición a guardar
     * @return posición guardada
     */
    @Override
    @Cacheable(key="#result.id")
    public Position save(PositionSaveDto position) {
        log.info("Guardando la position con el nombre: " + position.getName());
        return positionCrudRepository.save(PositionMapper.toModel(position));
    }

    /**
     * Actualiza una posición
     * @param id id de la posición a actualizar
     * @param position posición con los datos a actualizar
     * @return posición actualizada
     */
    @Override
    @Transactional
    @Cacheable(key="#result.id")
    public Position update(Long id, PositionUpdateDto position) {
        log.info("Actualizando la Posición con el id: " + id);
        Position original =findById(id);
        return positionCrudRepository.save(PositionMapper.toModel(original,position));
    }

    /**
     * Elimina una posición si no tiene empleados asignados
     * @param id id de la posición a eliminar
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Eliminando la Posición con el id: " + id);
        Position position=findById(id);
        if(!position.getWorkers().isEmpty()){
            throw new PositionBadRequest("La posición no puede ser eliminada porque tiene empleados asignados"); //tal vez deberia ser un tipo de excepcion distinto
        }
        positionCrudRepository.deleteById(id);
    }

    /**
     * Actualiza el campo isDeleted a true (elimina lógicamente)
     * @param id id de la posición a actualizar
     */
    @Override
    @Transactional
    public void updateIsDeletedToTrueById(Long id) {
        log.info("Actualizando a TRUE el isDeleted de la Posición con el id: " + id);
        findById(id);
        positionCrudRepository.updateIsDeletedToTrueById(id);
    }
}
