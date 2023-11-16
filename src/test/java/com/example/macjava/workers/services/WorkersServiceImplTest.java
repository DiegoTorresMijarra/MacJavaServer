package com.example.macjava.workers.services;

import com.example.macjava.categories.models.Position;
import com.example.macjava.categories.services.PositionServiceImpl;
import com.example.macjava.workers.dto.WorkersSaveDto;
import com.example.macjava.workers.dto.WorkersUpdateDto;
import com.example.macjava.workers.exceptions.WorkersNotFound;
import com.example.macjava.workers.mappers.WorkersMapper;
import com.example.macjava.workers.models.Workers;
import com.example.macjava.workers.repositories.WorkersCrudRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkersServiceImplTest {
    private final Position position1=Position.builder()
            .id(1L)
            .name("MANAGER")
            .salary(10000.0)
            .build();
    private final Position position2=Position.builder()
            .id(2L)
            .name("CLEANER")
            .salary(1000.0)
            .build();
    private Workers worker1= Workers.builder()
            .uuid(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
            .dni("53718369Y")
            .name("Diego")
            .surname("Torres")
            .age(25)
            .phone("123456789")
            .position(position1)
            .build();

    private Workers worker2 = Workers.builder()
            .uuid(UUID.fromString("d08d06c3-5973-4b45-af0a-159f8b675ec3"))
            .dni("12345678X")
            .name("Juan")
            .surname("Torres")
            .age(30)
            .phone("987654321")
            .position(position1)
            .build();

    private Workers worker3 = Workers.builder()
            .uuid(UUID.fromString("76ebbd5a-080b-4aae-81d8-8bf12d9d9f92"))
            .dni("87654321A")
            .name("María")
            .surname("Torres")
            .age(30)
            .phone("555111222")
            .position(position2)
            .build();

    private Workers worker4 = Workers.builder()
            .uuid(UUID.fromString("f487f8f9-7e35-4b79-bab7-ef9a1b5a821e"))
            .dni("65432187B")
            .name("Pablo")
            .surname("López")
            .age(25)
            .phone("333444555")
            .position(position2)
            .build();

    private Workers worker5 = Workers.builder()
            .uuid(UUID.fromString("ba2f764e-e31e-4871-964c-13e4f62a5ed2"))
            .dni("98765432C")
            .name("Diego")
            .surname("Gómez")
            .age(22)
            .phone("777888999")
            .position(position1)
            .build();

    @Mock
    private WorkersCrudRepository repository;
    @Mock
    private PositionServiceImpl positionService;
    @InjectMocks
    private WorkersServiceImpl service;

    @Test
    void findAll_ShouldReturnAllClients_WhenNoParametersProvided() {
        List<Workers> expectedClients = Arrays.asList(worker1, worker2, worker3, worker4, worker5);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Workers> expectedPage = new PageImpl<>(expectedClients);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Workers> actualPage = service.findAll(Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(),  pageable);
        assertAll("findAll_ShouldReturnAllClients_WhenNoParametersProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size())
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenNameParameterProvided() {
        List<Workers> expectedClients = Arrays.asList(worker1, worker5);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Workers> expectedPage = new PageImpl<>(expectedClients);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Workers> actualPage = service.findAll(Optional.of("Diego"), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(), Optional.empty(), pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenNameParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size()),
                ()-> assertEquals("Diego", actualPage.stream().findFirst().get().getName())
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenSurnameParameterProvided() {
        List<Workers> expectedClients = Arrays.asList(worker1, worker2, worker3);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Workers> expectedPage = new PageImpl<>(expectedClients);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Workers> actualPage = service.findAll(Optional.empty(), Optional.of("Torres"), Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(), Optional.empty(), pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenSurnameParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size()),
                ()-> assertEquals("Torres", actualPage.stream().findFirst().get().getSurname())
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenAgeParameterProvided() {
        List<Workers> expectedClients = Arrays.asList(worker1, worker4);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Workers> expectedPage = new PageImpl<>(expectedClients);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Workers> actualPage = service.findAll(Optional.empty(), Optional.empty(), Optional.of(25), Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),  pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenAgeParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size()),
                ()-> assertEquals(25, actualPage.stream().findFirst().get().getAge())
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenPhoneParameterProvided() {
        List<Workers> expectedClients = Arrays.asList(worker3);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Workers> expectedPage = new PageImpl<>(expectedClients);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Workers> actualPage = service.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("555111222"), Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),  pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenPhoneParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size()),
                ()-> assertEquals("555111222", actualPage.stream().findFirst().get().getPhone())
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenIsDeletedParameterProvided() {
        List<Workers> expectedClients = Arrays.asList(worker1, worker2, worker3, worker4, worker5);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Workers> expectedPage = new PageImpl<>(expectedClients);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Workers> actualPage = service.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(false),Optional.of(1),Optional.empty(),Optional.empty(),  pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenIsDeletedParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size()),
                ()-> assertEquals(false, actualPage.stream().findFirst().get().getIsDeleted())

        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenAntiquierityMaxParameterProvided() {
        List<Workers> expectedClients = Arrays.asList(worker1, worker2, worker3, worker4, worker5);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Workers> expectedPage = new PageImpl<>(expectedClients);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Workers> actualPage = service.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(), Optional.of(0),Optional.empty(),  pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenAntiquierityMaxParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size())
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenAntiquierityMinParameterProvided() {
        List<Workers> expectedClients = Arrays.asList(worker1, worker2, worker3, worker4, worker5);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Workers> expectedPage = new PageImpl<>(expectedClients);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Workers> actualPage = service.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.of(1),Optional.empty(),Optional.empty(),  pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenAntiquierityMinParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size())
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenPositionIdParameterProvided() {
        List<Workers> expectedClients = Arrays.asList(worker1, worker2, worker5);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Workers> expectedPage = new PageImpl<>(expectedClients);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Workers> actualPage = service.findAll(Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(),  pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenPositionIdParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size()),
                ()-> assertEquals(1, actualPage.stream().findFirst().get().getPosition().getId())
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnCorrect_WhenSurnameAndAgeParameterProvided() {
        List<Workers> expectedClients = Arrays.asList(worker2, worker3);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Workers> expectedPage = new PageImpl<>(expectedClients);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Workers> actualPage = service.findAll(Optional.empty(), Optional.of("Torres"), Optional.of(20), Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),  pageable);
        assertAll("findAll_ShouldReturnCorrect_WhenSurnameAndAgeParameterProvided",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(actualPage.getTotalElements(), expectedClients.size())
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findByUUID() {
        UUID uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        when(repository.findById(uuid)).thenReturn(Optional.of(worker1));

        Workers result=service.findByUUID(uuid);

        assertAll("FindByUUID",
                () -> assertNotNull(result),
                () -> assertEquals(worker1, result)
        );
        verify(repository, times(1)).findById(uuid);
    }
    @Test
    void findByUUID_ThrowsWorkerNotFoundException() {
        UUID uuid = UUID.randomUUID();
        when(repository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(WorkersNotFound.class, () -> {
            service.findByUUID(uuid);
        });
        verify(repository, times(1)).findById(uuid);
    }


    @Test
    void save() {
        WorkersSaveDto workersSaveDto =WorkersSaveDto.builder()
                .dni("12345678X")
                .name("Juan")
                .surname("Pérez")
                .age(30)
                .phone("987654321")
                .positionId(1L)
                .build();

        Workers worker6=WorkersMapper.toModel(workersSaveDto,position1);
        worker6.setUuid(UUID.fromString("c7f0055f-3517-4ba0-b4af-65a9b5f2bc23"));

        when(positionService.findById(1L)).thenReturn(position1);
        when(repository.save(any(Workers.class))).thenReturn(worker6);


        Workers result=service.save(workersSaveDto);
        assertAll("save",
                () -> assertNotNull(result),
                () -> assertEquals(worker6, result),
                () -> assertEquals(worker6.getUuid(), result.getUuid())
        );
    }

    @Test
    void update() {
        WorkersUpdateDto workersUpdateDto =WorkersUpdateDto.builder()
                .name("Juan")
                .positionId(2L)
                .build();

        Workers worker6=WorkersMapper.toModel(worker1,workersUpdateDto,position2);

        when(positionService.findById(2L)).thenReturn(position2);
        when(repository.findById(worker1.getUuid())).thenReturn(Optional.of(worker1));
        when(repository.save(any(Workers.class))).thenReturn(worker6);


        Workers result=service.update(worker1.getUuid(),workersUpdateDto);

        assertAll("update",
                () -> assertNotNull(result),
                () -> assertEquals(worker6, result),
                () -> assertEquals(worker6.getUuid(), result.getUuid()),
                () -> assertEquals(worker6.getPosition(), result.getPosition())
        );
    }
    @Test
    void update_ThrowsWorkersNotFoundException() {
        UUID id = UUID.randomUUID();
        WorkersUpdateDto workersUpdateDto =WorkersUpdateDto.builder().build();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(WorkersNotFound.class, () -> {
            service.update(id,workersUpdateDto);
        });
    }
    @Test
    void update_getSinCategoria_WhenDefault(){
        WorkersUpdateDto workersUpdateDto =WorkersUpdateDto.builder()
                .name("Juan")
                .positionId(-1L)
                .build();

        Workers worker6=WorkersMapper.toModel(worker1,workersUpdateDto,Position.SIN_CATEGORIA);

        when(repository.findById(worker1.getUuid())).thenReturn(Optional.of(worker1));
        when(repository.save(any(Workers.class))).thenReturn(worker6);


        Workers result=service.update(worker1.getUuid(),workersUpdateDto);

        assertAll("update",
                () -> assertNotNull(result),
                () -> assertEquals(worker6, result),
                () -> assertEquals(worker6.getUuid(), result.getUuid()),
                () -> assertEquals(worker6.getPosition(), result.getPosition())
        );
    }
    @Test
    void update_GetSinCategoria_WhenNull() {
        WorkersUpdateDto workersUpdateDto =WorkersUpdateDto.builder()
                .name("Juan")
                .positionId(null)
                .build();

        Workers worker6=WorkersMapper.toModel(worker1,workersUpdateDto,Position.SIN_CATEGORIA);

        when(repository.findById(worker1.getUuid())).thenReturn(Optional.of(worker1));
        when(repository.save(any(Workers.class))).thenReturn(worker6);


        Workers result=service.update(worker1.getUuid(),workersUpdateDto);

        assertAll("update",
                () -> assertNotNull(result),
                () -> assertEquals(worker6, result),
                () -> assertEquals(worker6.getUuid(), result.getUuid()),
                () -> assertEquals(worker6.getPosition(), result.getPosition())
        );

    }


    @Test
    void deleteByUUID() {
        when (repository.findById(worker1.getUuid())).thenReturn(Optional.of(worker1));
        service.deleteByUUID(worker1.getUuid());
        verify(repository, times(1)).deleteById(worker1.getUuid());
    }
    @Test
    void deleteByUUID_ThrowsWorkersNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(WorkersNotFound.class, () -> {
            service.deleteByUUID(id);
        });
    }


    @Test
    void findByDni() {
        when(repository.findByDni(worker1.getDni())).thenReturn(Optional.of(worker1));

        Workers result=service.findByDni(worker1.getDni());

        assertAll("FindByUUID",
                () -> assertNotNull(result),
                () -> assertEquals(worker1, result)
        );
        verify(repository, times(1)).findByDni(worker1.getDni());
    }

    @Test
    void findByIsDeleted() {
        List<Workers> expectedClients = Arrays.asList(worker1, worker2, worker3, worker4, worker5);

        when(repository.findByIsDeleted(false)).thenReturn(expectedClients);

        List<Workers> workersList = service.findByIsDeleted(false);
        assertAll("findByIsDeleted",
                () -> assertNotNull(workersList),
                () -> assertFalse(workersList.isEmpty()),
                () -> assertEquals(workersList.size(), expectedClients.size())
        );
    }

    @Test
    void updateIsDeletedToTrueById() {
        when(repository.findById(worker1.getUuid())).thenReturn(Optional.of(worker1));
        service.updateIsDeletedToTrueById(worker1.getUuid());
        verify(repository, times(1)).updateIsDeletedToTrueById(worker1.getUuid());
    }

    @Test
    void updateIsDeletedToTrueById_ThrowsWorkersNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(WorkersNotFound.class, () -> {
            service.updateIsDeletedToTrueById(id);
        });
    }

}