package com.example.macjava.categories.services;

import com.example.macjava.categories.dto.PositionSaveDto;
import com.example.macjava.categories.dto.PositionUpdateDto;
import com.example.macjava.categories.exceptions.PositionBadRequest;
import com.example.macjava.categories.exceptions.PositionNotFound;
import com.example.macjava.categories.mappers.PositionMapper;
import com.example.macjava.categories.models.Position;
import com.example.macjava.categories.repositories.PositionCrudRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Page<Position> findAll(Optional<String> name, Optional<Integer> salaryMin,Optional<Integer> salaryMax,Optional<Boolean> isDeleted, Pageable pageable) {
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

    @Override
    @Cacheable(key="#id")
    public Position findById(Long id) {
        log.info("Buscando la Posición con el id: " + id);
        return positionCrudRepository.findById(id).orElseThrow(() -> new PositionNotFound(id));
    }

    @Override
    @Cacheable(key="#result.id")
    public Position save(PositionSaveDto position) {
        log.info("Guardando la position con el nombre: " + position.getName());
        return positionCrudRepository.save(PositionMapper.toModel(position));
    }

    @Override
    @Transactional
    @Cacheable(key="#result.id")
    public Position update(Long id, PositionUpdateDto position) {
        log.info("Actualizando la Posición con el id: " + id);
        Position original =findById(id);
        return positionCrudRepository.save(PositionMapper.toModel(original,position));
    }

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

    @Override
    @Transactional
    public void updateIsDeletedToTrueById(Long id) {
        log.info("Actualizando a TRUE el isDeleted de la Posición con el id: " + id);
        findById(id);
        positionCrudRepository.updateIsDeletedToTrueById(id);
    }
}
