package com.example.macjava.restaurantes.controllers;

import com.example.macjava.restaurantes.dto.NewRestaurantDTO;
import com.example.macjava.restaurantes.dto.UpdatedRestaurantDTO;
import com.example.macjava.restaurantes.exceptions.RestaurantNotFound;
import com.example.macjava.restaurantes.modelos.Restaurante;
import com.example.macjava.restaurantes.servicios.RestaurantServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class RestaurantRestControllerTest {

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

    private NewRestaurantDTO newDto = NewRestaurantDTO.builder()
            .name("newDto")
            .number(String.valueOf(123456719L))

            .build();

    private UpdatedRestaurantDTO updatedDto = UpdatedRestaurantDTO.builder()
            .name("updatedDto")
            .number(String.valueOf(123456719L))
            .isDeleted(false)
            .build();

    private final String myEndPoint = "http://localhost:8080/restaurantes";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private RestaurantServiceImpl service;
    @Autowired
    private JacksonTester<NewRestaurantDTO> jsonClientCreateDto;
    @Autowired
    private JacksonTester<UpdatedRestaurantDTO> jsonClientUpdateDto;
    @Autowired
    public RestaurantRestControllerTest(RestaurantServiceImpl service) {
        this.service = service;
        mapper.registerModule(new JavaTimeModule());
    }

   /* @Test
    void getAllRestaurants() throws Exception {
        List<Restaurant> listaRes = List.of(res1, res2);
        Page<Restaurant> page = new PageImpl<>(listaRes);

        when(service.findAll(any(Optional.class), any(Optional.class),  any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                                 get(myEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                                .andReturn().getResponse();
        PageResponse<Restaurant> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );
    }*/

    /*
    @Test
    void getAllRestaurants_Name() throws Exception {
        List<Restaurant> clientsList = List.of(res1);
        Page<Restaurant> page = new PageImpl<>(clientsList);
        Optional<String> name = Optional.of("John");
        when(service.findAll(name, Optional.empty(), Optional.empty(), Optional.empty(), PageRequest.of(0, 10))).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "?name=Restaurante 1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Restaurant> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findall by name",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );
    }*/

    @Test
    void getRestaurant() throws Exception {
        when(service.findById(res1.getId())).thenReturn(res1);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/" + res1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).findById(res1.getId())
        );
    }

    @Test
    void updateProduct() throws Exception {
        when(service.findById(res1.getId())).thenReturn(res1);
        when(service.update(res1.getId(), updatedDto)).thenReturn(res1);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndPoint + "/" + res1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientUpdateDto.write(updatedDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        verify(service, times(1)).update(res1.getId(), updatedDto);
    }

    @Test
    void getProduct_NotFound() throws Exception {
        when(service.findById(res1.getId()))
                .thenThrow(new RestaurantNotFound(res1.getId()));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/" + res1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(service, times(1)).findById(res1.getId());
    }

    @Test
    void createRestaurant() throws Exception {

        when(service.save(newDto)).thenReturn(res1);
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClientCreateDto.write(newDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
        verify(service, times(1)).save(newDto);
    }

    @Test
    void deleteRestaurant() throws Exception {
        doNothing().when(service).deleteById(res1.getId());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/"+res1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("delete",
                () -> assertEquals(204, response.getStatus())
        );
        verify(service,times(1)).deleteById(res1.getId());
    }

    @Test
    void deleteProduct_NotFound() throws Exception {
        doThrow(new RestaurantNotFound(res1.getId())).when(service).deleteById(res1.getId());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/"+ res1.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(service, times(1)).deleteById(res1.getId());
    }
}

