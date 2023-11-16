package com.example.macjava.categories.services;

import com.example.macjava.categories.dto.PositionSaveDto;
import com.example.macjava.categories.dto.PositionUpdateDto;
import com.example.macjava.categories.exceptions.PositionBadRequest;
import com.example.macjava.categories.exceptions.PositionNotFound;
import com.example.macjava.categories.mappers.PositionMapper;
import com.example.macjava.categories.models.Position;
import com.example.macjava.categories.repositories.PositionCrudRepository;
import com.example.macjava.workers.models.Workers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionServiceImplTest {
    private  Position position1=Position.builder()
            .id(1L)
            .name("MANAGER")
            .salary(10000.0)
            .build();
    private final Position position2=Position.builder()
            .id(2L)
            .name("CLEANER")
            .salary(1000.0)
            .build();

    private Position position3 = Position.builder()
            .id(3L)
            .name("COOKER")
            .salary(1500.0)
            .build();

    @Mock
    private PositionCrudRepository positionCrudRepository;
    @InjectMocks
    private PositionServiceImpl positionService;


    @Test
    void findAll_ShouldReturnAllClients_WhenNoParametersProvided() {
        List<Position> expectedClients = Arrays.asList(position1, position2,position3);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Position> expectedPage = new PageImpl<>(expectedClients);

        when(positionCrudRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Position> actualPage = positionService.findAll(Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll("findAll_ShouldReturnAllClients_WhenNoParametersProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size())
        );
        verify(positionCrudRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenNameParameterProvided() {
        List<Position> expectedClients = Arrays.asList(position1);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Position> expectedPage = new PageImpl<>(expectedClients);

        when(positionCrudRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Position> actualPage = positionService.findAll(Optional.of("MANAGER"),Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenNameParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size()),
                ()-> assertEquals("MANAGER", actualPage.stream().findFirst().get().getName())
        );
        verify(positionCrudRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenMinSalaryParameterProvided() {
        List<Position> expectedClients = Arrays.asList(position1, position3);
        int minSalary = 1200;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Position> expectedPage = new PageImpl<>(expectedClients);

        when(positionCrudRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Position> actualPage = positionService.findAll(Optional.empty(),Optional.of(minSalary), Optional.empty(), Optional.empty(), pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenMinSalaryParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size()),
                ()-> assertTrue(minSalary < actualPage.stream().findFirst().get().getSalary())
        );
        verify(positionCrudRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenMaxSalaryParameterProvided() {
        List<Position> expectedClients = Arrays.asList(position2);
        int maxSalary = 1200;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Position> expectedPage = new PageImpl<>(expectedClients);

        when(positionCrudRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Position> actualPage = positionService.findAll(Optional.empty(),Optional.empty(), Optional.of(maxSalary),  Optional.empty(), pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenMinSalaryParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size()),
                ()-> assertFalse(maxSalary < actualPage.stream().findFirst().get().getSalary())
        );
        verify(positionCrudRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenIsDeletedParameterProvided() {
        List<Position> expectedClients = Arrays.asList(position1, position2,position3);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Position> expectedPage = new PageImpl<>(expectedClients);

        when(positionCrudRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Position> actualPage = positionService.findAll(Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenIsDeletedParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size())
        );
        verify(positionCrudRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenMaxSalaryAndMinSalaryParameterProvided() {
        List<Position> expectedClients = Arrays.asList(position3);
        int minSalary = 1200;
        int maxSalary = 1600;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Position> expectedPage = new PageImpl<>(expectedClients);

        when(positionCrudRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Position> actualPage = positionService.findAll(Optional.empty(),Optional.of(minSalary), Optional.empty(), Optional.empty(), pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenMaxSalaryAndMinSalaryParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size()),
                ()-> assertTrue(minSalary < actualPage.stream().findFirst().get().getSalary()),
                ()-> assertFalse(maxSalary < actualPage.stream().findFirst().get().getSalary())
        );
        verify(positionCrudRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findById_ShouldReturnCorrect() {
        when(positionCrudRepository.findById(1L)).thenReturn(Optional.of(position1));
        Position result=positionService.findById(1L);

        assertAll("FindByUUID",
                () -> assertNotNull(result),
                () -> assertEquals(position1, result)
        );
        verify(positionCrudRepository, times(1)).findById(1L);
    }
    @Test
    void findById_ThrowNotFoundException() {
        when(positionCrudRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PositionNotFound.class, () -> positionService.findById(1L));
        verify(positionCrudRepository, times(1)).findById(1L);
    }
    @Test
    void save_ShouldReturnCorrect() {
        PositionSaveDto positionSaveDto= PositionSaveDto.builder()
                .name("WAITER")
                .salary(1550.0)
                .build();
        Position position4= PositionMapper.toModel(positionSaveDto);
        position4.setId(4L);
        when(positionCrudRepository.save(any(Position.class))).thenReturn(position4);

        Position result = positionService.save(positionSaveDto);
        assertAll("save_ShouldReturnCorrect",
                () -> assertNotNull(result),
                () -> assertEquals(position4, result),
                () -> assertEquals(position4.getId(), result.getId())
        );
    }

    @Test
    void update_ShouldReturnCorrect() {
        PositionUpdateDto positionUpdateDto= PositionUpdateDto.builder()
                .salary(9999.0)
                .build();
        Position actualPosition= PositionMapper.toModel(position1,positionUpdateDto);
        when(positionCrudRepository.save(any(Position.class))).thenReturn(actualPosition);
        when(positionCrudRepository.findById(1L)).thenReturn(Optional.of(position1));

        Position result = positionService.update(1L, positionUpdateDto);
        assertAll("update_ShouldReturnCorrect",
                () -> assertNotNull(result),
                () -> assertEquals(actualPosition, result),
                () -> assertEquals(actualPosition.getId(), result.getId())
        );
    }
    @Test
    void update_ThrowsWorkersNotFoundException() {
        PositionUpdateDto positionUpdateDto= PositionUpdateDto.builder().build();
        when(positionCrudRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PositionNotFound.class,() -> positionService.update(1L, positionUpdateDto));
    }

    @Test
    void deleteById() {
        Optional <Position> position = Optional.of(position1);
        position.get().setWorkers(List.of());
        when(positionCrudRepository.findById(1L)).thenReturn(Optional.of(position1));
        positionService.deleteById(1L);
        verify(positionCrudRepository, times(1)).deleteById(1L);
    }
    @Test
    void deleteById_ThrowsPositionNotFoundException() {
        when(positionCrudRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PositionNotFound.class, () -> positionService.deleteById(1L));
    }
    @Test
    void deleteById_throwPositionBadRequest(){
        Optional <Position> position = Optional.of(position1);
        position.get().setWorkers(List.of(Workers.builder().build()));
        when(positionCrudRepository.findById(1L)).thenReturn(Optional.of(position1));
        assertThrows(PositionBadRequest.class, () -> positionService.deleteById(1L));
    }

    @Test
    void updateIsDeletedToTrueById() {
        when(positionCrudRepository.findById(1L)).thenReturn(Optional.of(position1));
        positionService.updateIsDeletedToTrueById(1L);
        verify(positionCrudRepository, times(1)).updateIsDeletedToTrueById(1L);
    }
    @Test
    void updateIsDeletedToTrueById_ThrowsPositionNotFoundException() {
        when(positionCrudRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PositionNotFound.class, () -> positionService.updateIsDeletedToTrueById(1L));
    }


}