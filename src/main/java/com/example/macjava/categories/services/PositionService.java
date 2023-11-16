package com.example.macjava.categories.services;

import com.example.macjava.categories.dto.PositionSaveDto;
import com.example.macjava.categories.dto.PositionUpdateDto;
import com.example.macjava.categories.models.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PositionService {
    Page<Position> findAll(Optional<String> name, Optional<Integer> salaryMin, Optional<Integer> salaryMax, Optional<Boolean> isDeleted, Pageable pageable);
    Position findById(Long id);
    Position save(PositionSaveDto position);
    Position update(Long id, PositionUpdateDto position);
    void deleteById(Long id);
    void updateIsDeletedToTrueById(Long id);
}
