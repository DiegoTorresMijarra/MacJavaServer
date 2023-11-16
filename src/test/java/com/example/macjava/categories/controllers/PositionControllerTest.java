package com.example.macjava.categories.controllers;

import com.example.macjava.categories.dto.PositionResponseDto;
import com.example.macjava.categories.dto.PositionSaveDto;
import com.example.macjava.categories.dto.PositionUpdateDto;
import com.example.macjava.categories.exceptions.PositionNotFound;
import com.example.macjava.categories.mappers.PositionMapper;
import com.example.macjava.categories.models.Position;
import com.example.macjava.categories.services.PositionServiceImpl;
import com.example.macjava.utils.pagination.PageResponse;
import com.example.macjava.workers.dto.WorkersResponseDto;
import com.example.macjava.workers.dto.WorkersSaveDto;
import com.example.macjava.workers.exceptions.WorkersNotFound;
import com.example.macjava.workers.mappers.WorkersMapper;
import com.example.macjava.workers.models.Workers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class PositionControllerTest {
    private Position position1=Position.builder()
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
    private final String myEndpoint = "http://localhost:8080/positions";
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private final PositionServiceImpl positionService;

    @Autowired
    public PositionControllerTest(PositionServiceImpl positionService) {
        this.positionService = positionService;
    }

    @Test
    void findById_getCorrect_WhenValidPassed() throws Exception {
        long id=1L;
        String localEndPoint=myEndpoint+"/"+id;
        when(positionService.findById(1L)).thenReturn(position1);
        MockHttpServletResponse response=mockMvc.perform(
                        get(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PositionResponseDto res = mapper.readValue(response.getContentAsString(), PositionResponseDto.class);
        assertAll("findByDni_getCorrect_WhenValidPassed",
                ()->assertEquals(200, response.getStatus()),
                ()->assertEquals(id, res.getId())
        );
    }

    @Test
    void findByDni_ThrowNotFound_WhenInvalidPassed() throws Exception {
        long id=1L;
        String localEndPoint=myEndpoint+"/"+id;
        doThrow(new PositionNotFound(id)).when(positionService).findById(id);
        MockHttpServletResponse response=mockMvc.perform(
                        get(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findByDni_getCorrect_WhenValidPassed",
                ()->assertEquals(404, response.getStatus())
        );
    }
    @Test
    void getAllPositions_ShouldReturnAllPositions_WhenNoParametersProvided() throws Exception {
        String localEndPoint=myEndpoint+"/positions";
        List<Position>  positionList = List.of(position1, position2, position3);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(positionList);
        // Arrange
        when(positionService.findAll(Optional.empty(),Optional.empty(),Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<PositionResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});

        // Assert
        assertAll("getAllPositions_ShouldReturnAllPositions_WhenNoParametersProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(positionList.size(), res.content().size())
        );
    }
    @Test
    void getAllPositions_ShouldReturnCorrect_WhenNameParameterProvided() throws Exception {
        String name="MANAGER";
        String localEndPoint = myEndpoint + "/positions?name="+name;
        List<Position> positionList = List.of(position1);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(positionList);
        // Arrange
        when(positionService.findAll(Optional.of("MANAGER"), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<PositionResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("getAllPositions_ShouldReturnCorrect_WhenNameParameterProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(positionList.size(), res.content().size()),
                () -> assertEquals(position1.getName(), res.content().get(0).getName())
        );
    }
    @Test
    void getAllPositions_ShouldReturnCorrect_WhensalaryMinParameterProvided() throws Exception {
        Integer salaryMin=1200;
        String localEndPoint = myEndpoint + "/positions?salaryMin="+salaryMin;
        List<Position> positionList = List.of(position1,position3);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(positionList);
        // Arrange
        when(positionService.findAll( Optional.empty(),Optional.of(salaryMin), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<PositionResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("getAllPositions_ShouldReturnCorrect_WhensalaryMinParameterProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(positionList.size(), res.content().size()),
                () -> assertTrue(res.content().get(0).getSalary()>=salaryMin)
        );
    }
    @Test
    void getAllPositions_ShouldReturnCorrect_WhensalaryMaxParameterProvided() throws Exception {
        Integer salaryMax=1200;
        String localEndPoint = myEndpoint + "/positions?salaryMax="+salaryMax;
        List<Position> positionList = List.of(position2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(positionList);
        // Arrange
        when(positionService.findAll( Optional.empty(),Optional.empty(),Optional.of(salaryMax),  Optional.empty(), pageable)).thenReturn(page);
        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<PositionResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("getAllPositions_ShouldReturnCorrect_WhensalaryMaxParameterProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(positionList.size(), res.content().size()),
                () -> assertTrue(res.content().get(0).getSalary()<=salaryMax)
        );
    }
    @Test
    void findAllPositions_ShouldReturnCorrect_WhenIsDeletedProvided() throws Exception {
        boolean isDeleted =false;
        String localEndPoint=myEndpoint+"/positions?isDeleted="+isDeleted;
        List<Position>  positionList = List.of(position1, position2, position3);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(positionList);
        // Arrange
        when(positionService.findAll(Optional.empty(),Optional.empty(),Optional.empty(), Optional.of(isDeleted), pageable)).thenReturn(page);
        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<PositionResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});

        // Assert
        assertAll("getAllPositions_ShouldReturnAllPositions_WhenNoParametersProvided",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(positionList.size(), res.content().size()),
                ()-> assertFalse(res.content().get(0).getIsDeleted())
        );
    }
    @Test
    void createPosition_ShouldReturnCorrect_WhenValidParametersProvided() throws Exception {
        String localEndPoint=myEndpoint+"/position";
        PositionSaveDto positionSaveDto = PositionSaveDto.builder()
                .name("CLEANER")
                .salary(1000.0)
                .build();
        Position position = PositionMapper.toModel(positionSaveDto);
        position.setId(4L);
        when(positionService.save(positionSaveDto)).thenReturn(position);
        MockHttpServletResponse response=mockMvc.perform(
                        post(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                //pasamos el body con el mapper
                                .content(mapper.writeValueAsString(positionSaveDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PositionResponseDto res = mapper.readValue(response.getContentAsString(), PositionResponseDto.class);

        assertAll("createPosition_ShouldReturnCorrect_WhenValidParametersProvided",
                ()->assertEquals(200, response.getStatus()),
                ()->assertEquals(position.getId(),res.getId())
        );
    }
    @Test
    void createPosition_ThrowsBadRequest_WhenInvalidPassed() throws Exception{
        String localEndPoint=myEndpoint+"/position";
        PositionSaveDto positionSaveDto = PositionSaveDto.builder()
                .name("CLEANER")
                //.salary(1000.0)
                .build();

        Position position = PositionMapper.toModel(positionSaveDto);
        position.setId(4L);
        when(positionService.save(positionSaveDto)).thenReturn(position);
        MockHttpServletResponse response=mockMvc.perform(
                        post(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                //pasamos el body con el mapper
                                .content(mapper.writeValueAsString(positionSaveDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertAll("createPosition_ThrowsBadRequest_WhenInvalidPassed",
                ()->assertEquals(400, response.getStatus()),
                ()->assertEquals("{\"salary\":\"must not be null\"}",response.getContentAsString())
        );
    }

    @Test
    void updatePosition_GetCorrect_WhenValidParametersProvided() throws Exception {
        long id=1L;
        String localEndPoint = myEndpoint + "/position/"+id;
        PositionUpdateDto positionUpdateDto = PositionUpdateDto.builder()
                .salary(9999.9)
                .build();
        Position position = PositionMapper.toModel(position1, positionUpdateDto);
        position.setId(id);
        when(positionService.update(id, positionUpdateDto)).thenReturn(position);
        MockHttpServletResponse response=mockMvc.perform(
                        MockMvcRequestBuilders.put(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                //pasamos el body con el mapper
                                .content(mapper.writeValueAsString(positionUpdateDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PositionResponseDto res=mapper.readValue(response.getContentAsString(), PositionResponseDto.class);
        assertAll("updatePosition_ShouldThrow_WhenValidParametersProvided",
                ()->assertEquals(200, response.getStatus()),
                ()->assertEquals(id,res.getId())
        );
    }
    @Test
    void deletedById_ShouldReturnCorrect_WhenValidParametersProvided() throws Exception {
        long id=1L;
        String localEndPoint = myEndpoint + "/position/"+id;
        doNothing().when(positionService).deleteById(id);
        MockHttpServletResponse response=mockMvc.perform(
                        delete(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        verify(positionService, times(1)).deleteById(id);
    }

    @Test
    void deletedById_ThrowsPositionNotFoundException() throws Exception {
        long id=99L;
        String localEndPoint = myEndpoint + "/position/"+id;
        doThrow(new PositionNotFound(id)).when(positionService).deleteById(id);
        MockHttpServletResponse response=mockMvc.perform(
                        delete(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("deletedById_ThrowsPositionNotFoundException",
                ()->assertEquals(404, response.getStatus())
        );
    }
    @Test
    void updateIsDeleted_ShouldReturnCorrect_WhenValidParametersProvided() throws Exception {
        long id=1L;
        String localEndPoint = myEndpoint + "/position/isDeleted/"+id;
        doNothing().when(positionService).updateIsDeletedToTrueById(id);
        MockHttpServletResponse response=mockMvc.perform(
                        MockMvcRequestBuilders.put(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        verify(positionService, times(1)).updateIsDeletedToTrueById(id);
    }
    @Test
    void updateIsDeleted_ShouldThrowPositionNotFound_WhenValidParametersProvided() throws Exception {
        long id=99L;
        String localEndPoint = myEndpoint + "/position/isDeleted/"+id;
        doThrow(new PositionNotFound(id)).when(positionService).updateIsDeletedToTrueById(id);
        MockHttpServletResponse response=mockMvc.perform(
                        MockMvcRequestBuilders.put(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("updateIsDeleted_ShouldThrowPositionNotFound_WhenValidParametersProvided",
                ()->assertEquals(404, response.getStatus())
        );
    }
}