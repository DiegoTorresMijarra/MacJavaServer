package com.example.macjava.workers.controllers;

import com.example.macjava.categories.models.Position;
import com.example.macjava.categories.services.PositionServiceImpl;
import com.example.macjava.utils.pagination.PageResponse;
import com.example.macjava.workers.dto.WorkersResponseDto;
import com.example.macjava.workers.dto.WorkersSaveDto;
import com.example.macjava.workers.dto.WorkersUpdateDto;
import com.example.macjava.workers.exceptions.WorkersNotFound;
import com.example.macjava.workers.mappers.WorkersMapper;
import com.example.macjava.workers.models.Workers;
import com.example.macjava.workers.services.WorkersServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class WorkersControllerTest {
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
            .name("Maria")
            .surname("Torres")
            .age(30)
            .phone("555111222")
            .position(position2)
            .build();

    private Workers worker4 = Workers.builder()
            .uuid(UUID.fromString("f487f8f9-7e35-4b79-bab7-ef9a1b5a821e"))
            .dni("65432187B")
            .name("Pablo")
            .surname("Lopez")
            .age(25)
            .phone("333444555")
            .position(position2)
            .build();

    private Workers worker5 = Workers.builder()
            .uuid(UUID.fromString("ba2f764e-e31e-4871-964c-13e4f62a5ed2"))
            .dni("98765432C")
            .name("Diego")
            .surname("Gomez")
            .age(22)
            .phone("777888999")
            .position(position1)
            .build();

    private final String myEndpoint = "http://localhost:8080/workers";
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkersServiceImpl workersService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public WorkersControllerTest(WorkersServiceImpl workersService){
        this.workersService = workersService;
    }

    @Test
    void getAllWorkers_ShouldReturnAllProducts_WhenNoParametersProvided() throws Exception {
        String localEndPoint=myEndpoint+"/workers";
        List<Workers> workersList = List.of(worker1, worker2, worker3, worker4,worker5);
        var pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        var page = new PageImpl<>(workersList);

        // Arrange
        when(workersService.findAll(Optional.empty(),Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<WorkersResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});

        // Assert
        assertAll("getAllProducts_ShouldReturnAllProducts_WhenNoParametersProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(workersList.size(), res.content().size())
        );

        // Verify
        verify(workersService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

    }
    @Test
    void getAllWorkers_ShouldReturnCorrect_WhenNameParameterProvided() throws Exception {
        String localEndPoint=myEndpoint+"/workers";
        List<Workers> workersList = List.of(worker1, worker5);
        var pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        var page = new PageImpl<>(workersList);

        // Arrange
        when(workersService.findAll(Optional.of("Diego"),Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);


        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint+"?name=Diego")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        System.out.println(response.getContentAsString());
        PageResponse<WorkersResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});

        // Assert
        assertAll("getAllWorkers_ShouldReturnCorrect_WhenNameParameterProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(workersList.size(), res.content().size()),
                () -> assertEquals("Diego", res.content().get(0).getName())
        );

        // Verify
        verify(workersService, times(1)).findAll(Optional.of("Diego"), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllWorkers_ShouldReturnCorrect_WhenSurnameParameterProvided() throws Exception {
        String localEndPoint=myEndpoint+"/workers";
        List<Workers> workersList = List.of(worker1, worker2,worker3);
        var pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        var page = new PageImpl<>(workersList);

        // Arrange
        when(workersService.findAll(Optional.empty(),Optional.of("Torres"),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);


        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint+"?surname=Torres")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        System.out.println(response.getContentAsString());
        PageResponse<WorkersResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});

        // Assert
        assertAll("getAllWorkers_ShouldReturnCorrect_WhenSurnameParameterProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(workersList.size(), res.content().size()),
                () -> assertEquals("Torres", res.content().get(0).getSurname())
        );

        // Verify
        verify(workersService, times(1)).findAll(Optional.empty(),Optional.of("Torres"),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }
    @Test
    void getAllWorkers_ShouldReturnCorrect_WhenAgeParameterProvided () throws Exception {
        String localEndPoint=myEndpoint+"/workers";
        List<Workers> workersList = List.of( worker2,worker3);
        var pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        var page = new PageImpl<>(workersList);

        // Arrange
        when(workersService.findAll(Optional.empty(),Optional.empty(),Optional.of(30), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);


        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint+"?age=30")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        System.out.println(response.getContentAsString());
        PageResponse<WorkersResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});

        // Assert
        assertAll("getAllWorkers_ShouldReturnCorrect_WhenSurnameParameterProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(workersList.size(), res.content().size()),
                () -> assertEquals(30, res.content().get(0).getAge())
        );

        // Verify
        verify(workersService, times(1)).findAll(Optional.empty(),Optional.empty(),Optional.of(30), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }
    @Test
    void getAllWorkers_ShouldReturnCorrect_WhenPhoneParameterProvided() throws Exception {
        String localEndPoint=myEndpoint+"/workers";
        List<Workers> workersList = List.of( worker1);
        var pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        var page = new PageImpl<>(workersList);

        // Arrange
        when(workersService.findAll(Optional.empty(),Optional.empty(), Optional.empty(),Optional.of("123456789"), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);


        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint+"?phone=123456789")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        System.out.println(response.getContentAsString());
        PageResponse<WorkersResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});

        // Assert
        assertAll("getAllWorkers_ShouldReturnCorrect_WhenPhoneParameterProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(workersList.size(), res.content().size()),
                () -> assertEquals("123456789", res.content().get(0).getPhone())
        );

        // Verify
        verify(workersService, times(1)).findAll(Optional.empty(),Optional.empty(), Optional.empty(),Optional.of("123456789"), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllWorkers_ShouldReturnCorrect_WhenIsDeletedParameterProvided () throws Exception {
        String localEndPoint=myEndpoint+"/workers";
        List<Workers> workersList = List.of(worker1,worker2,worker3,worker4,worker5);
        var pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        var page = new PageImpl<>(workersList);

        // Arrange
        when(workersService.findAll(Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(),Optional.of(false), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);


        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint+"?isDeleted=false")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        System.out.println(response.getContentAsString());
        PageResponse<WorkersResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});

        // Assert
        assertAll("getAllWorkers_ShouldReturnCorrect_WhenIsDeletedParameterProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(workersList.size(), res.content().size()),
                () -> assertFalse(res.content().get(0).getIsDeleted())
        );

        // Verify
        verify(workersService, times(1)).findAll(Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(),Optional.of(false), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }
    @Test
    void getAllWorkers_ShouldReturnCorrect_WhenAntiquityMinParameterProvided () throws Exception {
        String localEndPoint=myEndpoint+"/workers";
        List<Workers> workersList = List.of(worker1,worker2,worker3,worker4,worker5);
        var pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        var page = new PageImpl<>(workersList);

        // Arrange
        when(workersService.findAll(Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.of(0), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);


        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint+"?antiquierityMin=0")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        System.out.println(response.getContentAsString());
        PageResponse<WorkersResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});

        // Assert
        assertAll("getAllWorkers_ShouldReturnCorrect_WhenAntiquityMinParameterProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(workersList.size(), res.content().size()),
                () -> assertTrue(LocalDateTime.now().minusYears(0).isAfter(res.content().get(0).getCreatedAt()))
        );

        // Verify
        verify(workersService, times(1)).findAll(Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.of(0), Optional.empty(), Optional.empty(), pageable);
    }
    @Test
    void getAllWorkers_ShouldReturnCorrect_WhenAntiquityMaxParameterProvided () throws Exception {
        String localEndPoint=myEndpoint+"/workers";
        List<Workers> workersList = List.of(worker1,worker2,worker3,worker4,worker5);
        var pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        var page = new PageImpl<>(workersList);

        // Arrange
        when(workersService.findAll(Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.of(1), Optional.empty(), pageable)).thenReturn(page);


        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint+"?antiquierityMax=1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        System.out.println(response.getContentAsString());
        PageResponse<WorkersResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});

        // Assert
        assertAll("getAllWorkers_ShouldReturnCorrect_WhenAntiquityMaxParameterProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(workersList.size(), res.content().size()),
                () -> assertTrue(LocalDateTime.now().minusYears(1).isBefore(res.content().get(0).getCreatedAt()))
        );

        // Verify
        verify(workersService, times(1)).findAll(Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.of(1), Optional.empty(), pageable);
    }
    @Test
    void getAllWorkers_ShouldReturnCorrect_WhenPositionParameterProvided () throws Exception {
        String localEndPoint=myEndpoint+"/workers";
        List<Workers> workersList = List.of( worker3,worker4);
        var pageable = PageRequest.of(0, 10, Sort.by("uuid").ascending());
        var page = new PageImpl<>(workersList);

        // Arrange
        when(workersService.findAll(Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.of(2), pageable)).thenReturn(page);


        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint+"?positionId=2")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        System.out.println(response.getContentAsString());
        PageResponse<WorkersResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});

        // Assert
        assertAll("getAllWorkers_ShouldReturnCorrect_WhenPositionParameterProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(workersList.size(), res.content().size()),
                () -> assertEquals(position2, res.content().get(0).getPosition())
        );

        // Verify
        verify(workersService, times(1)).findAll(Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),Optional.of(2), pageable);
    }


    @Test
    void findByUUID_getCorrect_WhenValidPassed ()throws Exception {
        UUID uuid=worker1.getUuid();
        String localEndPoint=myEndpoint+"/"+uuid;
        when(workersService.findByUUID(uuid)).thenReturn(worker1);
        MockHttpServletResponse response=mockMvc.perform(
                        get(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        WorkersResponseDto res = mapper.readValue(response.getContentAsString(), WorkersResponseDto.class);
        assertAll("findByDni_getCorrect_WhenValidPassed",
                ()->assertEquals(200, response.getStatus()),
                ()->assertEquals(uuid, res.getUuid())
        );
    }
    @Test
    void findByUUID_ThrowsWorkerNotFoundException () throws Exception {
        UUID uuid = UUID.randomUUID();
        String localEndPoint = myEndpoint +"/"+ uuid;
        doThrow(new WorkersNotFound("UUID: " + uuid)).when(workersService).findByUUID(uuid);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll("findByUUID_ThrowsWorkerNotFoundException",
                () -> assertEquals(404, response.getStatus()));
    }


    @Test
    void createWorkers_getCorrect_WhenValidPassed() throws Exception{
        String localEndPoint=myEndpoint+"/worker";
        WorkersSaveDto workersSaveDto = WorkersSaveDto.builder()
                .name("Nuevo")
                .surname("NuevoApellido")
                .age(20)
                .dni("12345678Q")
                .phone("123456789")
                .positionId(2L)
                .build();

        UUID uuid=UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        Workers newWorkers=WorkersMapper.toModel(workersSaveDto,position2);
        newWorkers.setUuid(uuid);
        when(workersService.save(workersSaveDto)).thenReturn(newWorkers);

        MockHttpServletResponse response=mockMvc.perform(
                        post(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                //pasamos el body con el mapper
                                .content(mapper.writeValueAsString(workersSaveDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        WorkersResponseDto res = mapper.readValue(response.getContentAsString(), WorkersResponseDto.class);

        assertAll("createWorkers_getCorrect_WhenValidPassed",
                ()->assertEquals(200, response.getStatus()),
                ()->assertEquals(uuid,res.getUuid())
        );
    }
    @Test
    void createWorkers_ThrowsBadRequest_WhenInvalidPassed() throws Exception{
        String localEndPoint=myEndpoint+"/worker";
        WorkersSaveDto workersSaveDto = WorkersSaveDto.builder()
                .name("Nuevo")
                .surname("NuevoApellido")
                .age(20)
                .dni("12345678Q")
                //.phone("123456789") omitimos este valor
                .positionId(2L)
                .build();

        UUID uuid=UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        Workers newWorkers=WorkersMapper.toModel(workersSaveDto,position2);
        newWorkers.setUuid(uuid);
        when(workersService.save(workersSaveDto)).thenReturn(newWorkers);

        MockHttpServletResponse response=mockMvc.perform(
                        post(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                //pasamos el body con el mapper
                                .content(mapper.writeValueAsString(workersSaveDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertAll("createWorkers_ThrowsBadRequest_WhenInvalidPassed",
                ()->assertEquals(400, response.getStatus()),
                ()->assertEquals("{\"phone\":\"El telefono no puede estar vacÃ\u00ADo\"}",response.getContentAsString())
        );
    }


    @Test
    void updateWorker_getCorrect_WhenValidPassed() throws Exception {
        UUID uuid=worker1.getUuid();
        String localEndPoint=myEndpoint+"/worker/"+uuid;
        WorkersUpdateDto workersUpdateDto = WorkersUpdateDto.builder()
                .name("Juan")
                .positionId(2L)
                .build();

        Workers newWorkers=WorkersMapper.toModel(worker1,workersUpdateDto,position2);
        newWorkers.setUuid(uuid);
        when(workersService.update(uuid,workersUpdateDto)).thenReturn(newWorkers);

        MockHttpServletResponse response=mockMvc.perform(
                        put(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                //pasamos el body con el mapper
                                .content(mapper.writeValueAsString(workersUpdateDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        WorkersResponseDto res = mapper.readValue(response.getContentAsString(), WorkersResponseDto.class);
        assertAll("updateWorker_getCorrect_WhenValidPassed",
                ()->assertEquals(200, response.getStatus()),
                ()->assertEquals(uuid,res.getUuid())
        );
    }
    /* no me va bien, revisar porq me permite hacerlo pero si le meto constraints peta el q va bien xd
    @Test
    void updateWorker_ThrowsBadRequest_WhenInvalidPassed() throws Exception {
        UUID uuid=worker1.getUuid();
        String localEndPoint=myEndpoint+"/worker/"+uuid;
        WorkersUpdateDto workersUpdateDto = WorkersUpdateDto.builder()
                .name("Juan")
                .age(-1)
                .build();

        Workers newWorkers=WorkersMapper.toModel(worker1,workersUpdateDto,position2);
        newWorkers.setUuid(uuid);
        when(workersService.update(uuid,workersUpdateDto)).thenReturn(newWorkers);

        MockHttpServletResponse response=mockMvc.perform(
                        put(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                //pasamos el body con el mapper
                                .content(mapper.writeValueAsString(workersUpdateDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("updateWorker_getCorrect_WhenValidPassed",
                ()->assertEquals(400, response.getStatus()),
                ()->assertEquals("{\"phone\":\"El telefono no puede estar vacÃ\u00ADo\"}",response.getContentAsString())
        );
    }

     */

    @Test
    void deleteByUUID_getCorrect_WhenValidPassed() throws Exception {
        UUID uuid=worker1.getUuid();
        String localEndPoint=myEndpoint+"/worker/"+uuid;
        doNothing().when(workersService).deleteByUUID(uuid);
        MockHttpServletResponse response=mockMvc.perform(
                        delete(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        verify(workersService, times(1)).deleteByUUID(uuid);
    }
    @Test
    void deleteByUUID_ThrowsWorkerNotFoundException() throws Exception {
        UUID uuid=UUID.randomUUID();
        String localEndPoint=myEndpoint+"/worker/"+uuid;
        doThrow(new WorkersNotFound("UUID: "+uuid)).when(workersService).deleteByUUID(uuid);
        MockHttpServletResponse response=mockMvc.perform(
                        delete(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("deleteByUUID_ThrowsWorkerNotFoundException",
                ()->assertEquals(404, response.getStatus())
        );
    }

    @Test
    void findByDni_getCorrect_WhenValidPassed()throws Exception {
        String dni=worker1.getDni();
        String localEndPoint=myEndpoint+"/dni/"+dni;
        when(workersService.findByDni(dni)).thenReturn(worker1);
        MockHttpServletResponse response=mockMvc.perform(
                        get(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        WorkersResponseDto res = mapper.readValue(response.getContentAsString(), WorkersResponseDto.class);
        assertAll("findByDni_getCorrect_WhenValidPassed",
                ()->assertEquals(200, response.getStatus()),
                ()->assertEquals(dni, res.getDni())
        );
    }
    @Test
    void findByDni_ThrowsWorkerNotFoundException() throws Exception {
        String dni="99999999Z";
        String localEndPoint=myEndpoint+"/dni/"+dni;
        doThrow(new WorkersNotFound("DNI: "+dni)).when(workersService).findByDni(dni);
        MockHttpServletResponse response=mockMvc.perform(
                        get(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll("findByDni_ThrowsWorkerNotFoundException",
                ()->assertEquals(404, response.getStatus())
        );
    }

    @Test
    void updateIsDeletedToTrueById_getCorrect_WhenValidPassed()throws Exception {
        UUID uuid=worker1.getUuid();
        String localEndPoint=myEndpoint+"/worker/isDeleted/"+uuid;
        doNothing().when(workersService).updateIsDeletedToTrueById(uuid);
        MockHttpServletResponse response=mockMvc.perform(
                        put(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        verify(workersService, times(1)).updateIsDeletedToTrueById(uuid);
    }
    @Test
    void updateIsDeletedToTrueById_ThrowsWorkerNotFoundException() throws Exception {
        UUID uuid=UUID.randomUUID();
        String localEndPoint=myEndpoint+"/worker/isDeleted/"+uuid;
        doThrow(new WorkersNotFound("UUID: "+uuid)).when(workersService).updateIsDeletedToTrueById(uuid);
        MockHttpServletResponse response=mockMvc.perform(
                        put(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll("updateIsDeletedToTrueById_ThrowsWorkerNotFoundException",
                ()->assertEquals(404, response.getStatus())
        );
    }

    @Test
    void findByIsDeleted_GetCorrect_WhenValidPassed() throws Exception {
        String localEndPoint=myEndpoint+"/worker/isDeleted/"+false;
        List<Workers> expectedWorkers = List.of(worker1, worker2, worker3, worker4, worker5);
        when(workersService.findByIsDeleted(false)).thenReturn(expectedWorkers);
        MockHttpServletResponse response=mockMvc.perform(
                        get(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        List<WorkersResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("findByIsDeleted_GetCorrect_WhenValidPassed",
                () -> assertEquals(200, response.getStatus()),
                () -> assertNotNull(res),
                () -> assertFalse(res.isEmpty()),
                () -> assertEquals(res.size(), expectedWorkers.size())
        );
    }

    /*
    @Test
    void handleValidationExceptions() {
    }

     */
}