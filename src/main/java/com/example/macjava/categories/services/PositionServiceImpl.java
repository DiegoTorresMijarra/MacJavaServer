package com.example.macjava.categories.services;

import com.example.macjava.categories.dto.PositionSaveDto;
import com.example.macjava.categories.dto.PositionUpdateDto;
import com.example.macjava.categories.exceptions.PositionNotFound;
import com.example.macjava.categories.mappers.PositionMapper;
import com.example.macjava.categories.models.Position;
import com.example.macjava.categories.repositories.PositionCrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PositionServiceImpl implements PositionService{
    private final PositionCrudRepository positionCrudRepository;

    @Autowired
    public PositionServiceImpl(PositionCrudRepository positionCrudRepository) {
        log.info("Iniciando el Servicio de Posiciones");
        this.positionCrudRepository = positionCrudRepository;
    }

    @Override
    public List<Position> findAll() {
        log.info("Buscando todas las Posiciones");
        return positionCrudRepository.findAll();
    }

    @Override
    public Position findById(Long id) {
        log.info("Buscando la Posición con el id: " + id);
        return positionCrudRepository.findById(id).orElseThrow(() -> new PositionNotFound(id));
    }

    @Override
    public Position save(PositionSaveDto position) {
        log.info("Going to save position con el nombre: " + position.getName());
        return positionCrudRepository.save(PositionMapper.toModel(position));
    }

    @Override
    public Position update(Long id, PositionUpdateDto position) {
        log.info("Actualizando la Posición con el id: " + id);
        Position original =findById(id);
        return positionCrudRepository.save(PositionMapper.toModel(original,position));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Eliminando la Posición con el id: " + id);
        positionCrudRepository.deleteById(id);
    }
}
