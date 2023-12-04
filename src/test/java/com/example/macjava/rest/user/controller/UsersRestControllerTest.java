package com.example.macjava.rest.user.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.macjava.rest.user.dto.UserInfoResponse;
import com.example.macjava.rest.user.dto.UserResponse;
import com.example.macjava.rest.user.exception.UserNotFound;
import com.example.macjava.rest.user.service.UsersService;
import com.example.macjava.utils.pagination.PageResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.example.macjava.rest.user.dto.UserRequest;
import com.example.macjava.rest.user.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class) // Extensión de Mockito para usarlo
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"}) // Usuario de prueba (admin, tiene de rol usaurio y admin)
class UsersRestControllerTest {

    private final UserRequest userRequest = UserRequest.builder()
            .nombre("test")
            .apellidos("test")
            .password("test1234")
            .username("test")
            .email("test@test.com")
            .build();
    private final User user = User.builder()
            .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
            .nombre("test")
            .apellidos("test")
            .password("test1234")
            .username("test")
            .email("test@test.com")
            .build();
    private final UserResponse userResponse = UserResponse.builder()
            .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
            .nombre("test")
            .apellidos("test")
            .username("test")
            .email("test@test.com")
            .build();
    private final UserInfoResponse userInfoResponse = UserInfoResponse.builder()
            .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
            .nombre("test")
            .apellidos("test")
            .username("test")
            .email("test@test.com")
            .build();

    private final String myEndpoint = "https://localhost:3000/v1/users";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc; // Cliente MVC

    @MockBean
    private UsersService usersService;


    @Autowired
    public UsersRestControllerTest(UsersService UsersService) {
        this.usersService = UsersService;
        mapper.registerModule(new JavaTimeModule()); // Necesario para que funcione LocalDateTime
    }

    @Test
    @WithAnonymousUser
    void NotAuthenticated() throws Exception {
        // Localpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(403, response.getStatus());
    }

    @Test
    void findAll() throws Exception {
        var list = List.of(userResponse);
        Page<UserResponse> page = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        // Arrange
        when(usersService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // System.out.println(response.getContentAsString());
        PageResponse<UserResponse> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll("findallUsers",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        // Verify
        verify(usersService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void findById() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint + "/00000000-0000-0000-0000-000000000000";

        // Arrange
        when(usersService.findById(any(UUID.class))).thenReturn(userInfoResponse);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), UserInfoResponse.class);

        // Assert
        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(userInfoResponse, res)
        );

        // Verify
        verify(usersService, times(1)).findById(any(UUID.class));
    }

    @Test
    void findByIdNotFound() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint + "/00000000-0000-0000-0000-000000000000";

        // Arrange
        when(usersService.findById(any(UUID.class))).thenThrow(new UserNotFound("No existe el usuario"));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(404, response.getStatus());

        // Verify
        verify(usersService, times(1)).findById(any(UUID.class));
    }

    @Test
    void createUser() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint;

        // Arrange
        when(usersService.save(any(UserRequest.class))).thenReturn(userResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), UserResponse.class);

        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(userResponse, res)
        );

        // Verify
        verify(usersService, times(1)).save(any(UserRequest.class));

    }

    // Hay que comprobar cada una de las validaciones

    @Test
    void createUserBadRequestPasswordMenosDe5Caracteres() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint;

        // Arrange
        var userRequest = UserRequest.builder()
                .nombre("test")
                .apellidos("test")
                .password("test")
                .username("test")
                .email("test@test.com")
                .build();
        when(usersService.save(any(UserRequest.class))).thenReturn(userResponse);
        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        // Assert
        assertEquals(400, response.getStatus());

        // Verify
        verify(usersService, times(0)).save(any(UserRequest.class));
    }

    // Lo normal es hacer uno para cada validación
    @Test
    void createUserBadRequestNombreApellidosYTodoEnBlanco() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint;

        // Arrange
        var userRequest = UserRequest.builder()
                .nombre("")
                .apellidos("")
                .password("test1234")
                .username("test")
                .email("")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        // Assert
        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("Nombre no puede estar")),
                () -> assertTrue(response.getContentAsString().contains("Apellidos no puede estar"))
        );
    }


    @Test
    void updateUser() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint + "/00000000-0000-0000-0000-000000000000";

        // Arrange
        when(usersService.update(any(UUID.class), any(UserRequest.class))).thenReturn(userResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), UserResponse.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(userResponse, res)
        );

        // Verify
        verify(usersService, times(1)).update(any(UUID.class), any(UserRequest.class));
    }

    @Test
    void updateUserNotFound() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint + "/00000000-0000-0000-0000-000000000000";

        // Arrange
        when(usersService.update(any(UUID.class), any(UserRequest.class))).thenThrow(new UserNotFound("No existe el usuario"));

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        // Assert
        assertEquals(404, response.getStatus());

        // Verify
        verify(usersService, times(1)).update(any(UUID.class), any(UserRequest.class));
    }

    // Hacer un test para cada una de las validaciones del update

    @Test
    void deleteUser() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint + "/00000000-0000-0000-0000-000000000000";

        // Arrange
        doNothing().when(usersService).deleteById(any(UUID.class));

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertEquals(204, response.getStatus());

        // Verify
        verify(usersService, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void deleteUserNotFound() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint + "/00000000-0000-0000-0000-000000000000";

        // Arrange
        doThrow(new UserNotFound("No existe el usuario")).when(usersService).deleteById(any(UUID.class));

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(404, response.getStatus());

        // Verify
        verify(usersService, times(1)).deleteById(any(UUID.class));
    }

    @Test
    // Este lo puede hacer cualquiera que esté autenticado
    // Pero autentication principal necesita uno de vverdad, por ueso usamos admin o user
    // que está en la base de datos data.sql que lo buscará con el userDetailsService
    @WithUserDetails("admin")
    void me() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint + "/me/profile";

        // Arrange
        when(usersService.findById(any(UUID.class))).thenReturn(userInfoResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    @WithAnonymousUser
    void me_AnonymousUser() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint + "/me/profile";
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(403, response.getStatus());
    }
}