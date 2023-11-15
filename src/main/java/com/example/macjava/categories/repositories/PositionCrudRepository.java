package com.example.macjava.categories.repositories;

import com.example.macjava.categories.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionCrudRepository extends JpaRepository<Position, Long>, JpaSpecificationExecutor<Position> {
    //todo
}
