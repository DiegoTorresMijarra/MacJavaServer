package com.example.macjava.categories.services;

import com.example.macjava.categories.dto.PositionSaveDto;
import com.example.macjava.categories.dto.PositionUpdateDto;
import com.example.macjava.categories.models.Position;

import java.util.List;

public interface PositionService {
    List<Position> findAll();
    Position findById(Long id);
    Position save(PositionSaveDto position);
    Position update(Long id, PositionUpdateDto position);
    void deleteById(Long id);
}
