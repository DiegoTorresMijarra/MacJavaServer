package com.example.macjava.restaurantes.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import com.example.macjava.restaurantes.dto.NewRestaurantDTO;
import com.example.macjava.restaurantes.dto.UpdatedRestaurantDTO;
import com.example.macjava.restaurantes.exceptions.RestaurantNotFound;
import com.example.macjava.restaurantes.mapper.RestaurantMapper;
import com.example.macjava.restaurantes.modelos.Restaurante;
import com.example.macjava.restaurantes.repositories.RestaurantRepository;
import com.example.macjava.restaurantes.servicios.RestaurantServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {

    private Restaurante res1 = Restaurante.builder()
            .id(1L)
            .name("Restaurante 1")
            .number(String.valueOf(123456789))
            .isDeleted(false)
            .creationD(LocalDate.now())
            .modificationD(LocalDate.now())
            .build();

    private Restaurante res2 = Restaurante.builder()
            .id(2L)
            .name("Restaurante 2")
            .number(String.valueOf(987654321))
            .isDeleted(false)
            .creationD(LocalDate.now())
            .modificationD(LocalDate.now())
            .build();
    private Restaurante res3= Restaurante.builder()
            .id(3L)
            .name("Restaurante 3")
            .number(String.valueOf(123455789))
            .isDeleted(true)
            .creationD(LocalDate.now())
            .modificationD(LocalDate.now())
            .build();

    private NewRestaurantDTO newDto = NewRestaurantDTO.builder()
            .name("newDto")
            .number(String.valueOf(123456719))
            .build();

    private UpdatedRestaurantDTO updatedDto = UpdatedRestaurantDTO.builder()
            .name("updatedDto")
            .number(String.valueOf(123456719))
            .isDeleted(false)
            .build();
    @Mock
    private RestaurantRepository repository;
    private final RestaurantMapper mapper = new RestaurantMapper();
    @InjectMocks
    RestaurantServiceImpl service;
    @Test
    void findAll_ShouldReturnAllRestaurantWithoutParameters() {
        List<Restaurante> expectedRestau = Arrays.asList(res1, res2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("cod").ascending());
        Page<Restaurante> expectedPage = new PageImpl<>(expectedRestau);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Restaurante> actualPage = service.findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll("findAll",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ShouldReturnRestaurantsByName_WhenNameParameterProvided() {
        Optional<String> name = Optional.of("Restaurante 1");
        List<Restaurante> expectedRestau = List.of(res1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("cod").ascending());
        Page<Restaurante> expectedPage = new PageImpl<>(expectedRestau);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Restaurante> actualPage = service.findAll(name, Optional.empty(), Optional.empty(),  pageable);
        assertAll("findAllWithName",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findAll_ShouldReturnRestaurantByNumber_WhenNumberParameterProvided() {
        Optional<String> number = Optional.of((String.valueOf(123456789)));
        List<Restaurante> expectedRestau = List.of(res1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("cod").ascending());
        Page<Restaurante> expectedPage = new PageImpl<>(expectedRestau);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Restaurante> actualPage = service.findAll(Optional.empty(), number,Optional.empty(), pageable);
        assertAll("findAllWithNumber",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ShouldReturnRestaurantsByDeleted_WhenDeletedParameterProvided() {
        Optional<Boolean> isDeleted = Optional.of(true);
        List<Restaurante> expectedRestaurants = List.of(res3);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("cod").ascending());
        Page<Restaurante> expectedPage = new PageImpl<>(expectedRestaurants);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Restaurante> actualPage = service.findAll(Optional.empty(), Optional.empty(),isDeleted, pageable);
        assertAll("findAllWithDeleted",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void findById() {
        when(repository.findById(res1.getId())).thenReturn(Optional.of(res1));

        Restaurante result=service.findById(res1.getId());

        assertAll("FindById",
                () -> assertNotNull(result),
                () -> assertEquals(res1, result),
                () -> verify(repository, times(1)).findById(res1.getId())
        );
    }
    @Test
    void findByIdthrowNotFound(){
        Long cod = 5L;
        when(repository.findById(cod)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFound.class, () -> {
            service.findById(cod);
        });

        verify(repository, times(1)).findById(cod);

    }

    @Test
    void save() {
        Restaurante newRes = mapper.toRestaurantNew(newDto);
        when(repository.save(any(Restaurante.class))).thenReturn(newRes);

        Restaurante result = service.save(newDto);

        assertAll("Save",
                () -> assertNotNull(result),
                () -> assertEquals(newRes.getId(), result.getId()),
                () -> verify(repository, times(1)).save(any(Restaurante.class))
        );
    }

    @Test
    void update() {
        when(repository.findById(res1.getId())).thenReturn(Optional.of(res1));
        Restaurante updatedRestau = mapper.toRestaurantUpdate(updatedDto, res1);
        when(repository.save(updatedRestau)).thenReturn(updatedRestau);
        Restaurante result = service.update(res1.getId(), updatedDto);
        assertAll("Update",
                () -> assertNotNull(result),
                () -> assertEquals(res1.getId(), result.getId()),
                () -> verify(repository, times(1)).findById(res1.getId()),
                () -> verify(repository, times(1)).save(updatedRestau)
        );
    }

    @Test
    void updateClientNotFound() {
        Long cod = 5L;
        when(repository.findById(cod)).thenReturn(Optional.empty());
        assertThrows(RestaurantNotFound.class, () -> service.update(cod, updatedDto));
        verify(repository, times(1)).findById(cod);
    }

    @Test
    void deleteById() {
        when(repository.findById(res1.getId())).thenReturn(Optional.of(res1));
        service.deleteById(res1.getId());
        assertAll("DeleteById",
                () -> verify(repository, times(1)).findById(res1.getId()),
                () -> verify(repository, times(1)).updateIsDeletedToTrueById(res1.getId())
        );
    }
    @Test
    void deleteByIdNotFound() {
        when(repository.findById(res1.getId())).thenReturn(Optional.empty());
        assertThrows(RestaurantNotFound.class, () -> service.deleteById(res1.getId()));
        verify(repository, times(1)).findById(res1.getId());
    }

}
