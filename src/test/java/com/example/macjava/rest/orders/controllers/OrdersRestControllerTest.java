package com.example.macjava.rest.orders.controllers;

import com.example.macjava.config.auth.SecurityConfig;
import com.example.macjava.rest.auth.service.jwt.JwtServiceImpl;
import com.example.macjava.rest.orders.dto.OrderSaveDto;
import com.example.macjava.rest.orders.dto.OrderUpdateDto;
import com.example.macjava.rest.orders.exceptions.OrderBadRequest;
import com.example.macjava.rest.orders.exceptions.OrderNotFound;
import com.example.macjava.rest.orders.models.Order;
import com.example.macjava.rest.orders.models.OrderedProduct;
import com.example.macjava.rest.orders.services.OrdersServiceImpl;
import com.example.macjava.rest.products.models.Product;
import com.example.macjava.rest.user.models.Role;
import com.example.macjava.rest.user.models.User;
import com.example.macjava.rest.user.service.UsersServiceImp;
import com.example.macjava.utils.pagination.PageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "user", password = "u", roles = "USER")
@Import(SecurityConfig.class)
class OrdersRestControllerTest {
    private final String myEndpoint="http://localhost:8080/v1/orders";
    private final User mockUser=User.builder()
            .username("User")
            .roles(Set.of(Role.USER))
            .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
            .build();
    private final Product product= Product.builder()
            .id(1L)
            .stock(100)
            .precio(10.00)
            .build();

    private final List<OrderedProduct> validList=List.of(
            OrderedProduct.builder()
                    .quantity(10)
                    .productId(1L)
                    .productPrice(10.00)
                    .totalPrice(100.00)
                    .build(),
            OrderedProduct.builder()
                    .quantity(50)
                    .productId(1L)
                    .productPrice(10.00)
                    .totalPrice(500.00)
                    .build()
    );
    private final List<OrderedProduct> invalidList= List.of(
            OrderedProduct.builder()
                    .quantity(10)
                    .productId(2L)
                    .productPrice(0.00)
                    .totalPrice(100.00)
                    .build(),
            OrderedProduct.builder()
                    .quantity(500)
                    .productId(1L)
                    .productPrice(10.00)
                    .totalPrice(500.00)
                    .build()
    );

    private Order order1= Order.builder()
            .id(new ObjectId())
            .clientUUID(UUID.fromString("f47ac10b-58cc-4372-a567-446655440000"))
            .workerUUID(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
            .restaurantId(1L)
            .orderedProducts(validList)
            .build();
    private Order order2= Order.builder()
            .id(new ObjectId())
            .clientUUID(UUID.fromString("f47ac10b-58cc-4372-a567-446655440000"))
            .workerUUID(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
            .restaurantId(2L)
            .orderedProducts(validList.subList(0,1))
            .isPaid(true)
            .isDeleted(true)
            .build();

    private OrderSaveDto orderSaveDto= OrderSaveDto.builder()
            .clientUUID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"))
            .workerUUID(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
            .restaurantId(1L)
            .orderedProducts(validList)
            .build();
    private OrderUpdateDto orderUpdateDto= OrderUpdateDto.builder()
            .clientUUID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"))
            .workerUUID(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
            .restaurantId(1L)
            .orderedProducts(validList)
            .build();
    private List<Order> orderList;

    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc; // Cliente MVC

    @MockBean
    private OrdersServiceImpl ordersService;
    @MockBean
    private final JwtServiceImpl jwtService;
    @Autowired
    public OrdersRestControllerTest(OrdersServiceImpl ordersService,JwtServiceImpl jwtService) {
        this.ordersService = ordersService;
        this.jwtService = jwtService;
        mapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setUp() {
        order1.setOrderedProducts(validList);
        orderList = List.of(order1,order2);
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
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testFindAll() throws Exception {
        String localEndPoint=myEndpoint+"/listAll";
        when(ordersService.findAll()).thenReturn(orderList);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        List<Order> result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("findAll",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertEquals(orderList,result)
        );
    }
    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testFindAllPaged() throws Exception {
        String localEndPoint=myEndpoint;
        var res= new PageImpl<>(orderList);
        when(ordersService.findAll(any(Pageable.class))).thenReturn(res);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Order> result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("findAllPaged",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertEquals(orderList,result.content())
        );
    }
    @Test
    void testFindById() throws Exception {
        String localEndPoint=myEndpoint+"/6569a29d62996427018dc774";
        when(ordersService.findById(any(ObjectId.class))).thenReturn(order1);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Order result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertEquals(order1,result)
        );
    }
    @Test
    void testFindById_ThrowsNotFoundException() throws Exception {
        String localEndPoint=myEndpoint+"/6569a29d62996427018dc774";
        when(ordersService.findById(any(ObjectId.class))).thenThrow(OrderNotFound.class);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll("findById_ThrowsNotFoundException",
                () -> assertEquals(404, response.getStatus())
        );
    }
    @Test
    void testSaveOrder() throws Exception {
        String localEndPoint=myEndpoint+"/orders";
        when(ordersService.save(any(OrderSaveDto.class))).thenReturn(order1);

        MockHttpServletResponse response = mockMvc.perform(
                        post(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(orderSaveDto))
                                .header("Authorization",mockUser)
                                .with(
                                        requestSpec ->{
                                            try {
                                                requestSpec.setRemoteUser(mapper.writeValueAsString(mockUser));
                                            } catch (JsonProcessingException e) {
                                                throw new RuntimeException(e);
                                            }
                                            return requestSpec;
                                        }
                                )
                )
                .andReturn().getResponse();

        Order result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("saveOrder",
                () -> assertEquals(201, response.getStatus()),
                ()-> assertEquals(order1,result)
        );
    }
    @Test
    void testSaveOrder_ShouldThrowBadRequestException() throws Exception {
        String localEndPoint=myEndpoint+"/orders";
        when(ordersService.save(any(OrderSaveDto.class))).thenThrow(OrderBadRequest.class);
        MockHttpServletResponse response = mockMvc.perform(
                        post(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(orderSaveDto)))
                .andReturn().getResponse();
        assertAll("saveOrder_ShouldThrowBadRequestException",
                () -> assertEquals(400, response.getStatus())
        );
    }
    @Test
    void testSaveOrder_ShouldThrowForbiddenException() throws Exception {
        String localEndPoint=myEndpoint+"/orders";
        MockHttpServletResponse response = mockMvc.perform(
                        post(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(orderSaveDto)))
                .andReturn().getResponse();
        assertAll("saveOrder_ShouldThrowForbiddenException",
                () -> assertEquals(403, response.getStatus())
        );
    }
    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testSaveOrderAdmin() throws Exception {
        String localEndPoint=myEndpoint+"/ordersAdmin";
        when(ordersService.save(any(OrderSaveDto.class))).thenReturn(order1);
        MockHttpServletResponse response = mockMvc.perform(
                        post(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(orderSaveDto)))
                .andReturn().getResponse();

        Order result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("saveOrderAdmin",
                () -> assertEquals(201, response.getStatus()),
                ()-> assertEquals(order1,result)
        );
    }
    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testSaveOrderAdmin_ShouldThrowBadRequestException() throws Exception {
        String localEndPoint=myEndpoint+"/ordersAdmin";
        when(ordersService.save(any(OrderSaveDto.class))).thenThrow(OrderBadRequest.class);
        MockHttpServletResponse response = mockMvc.perform(
                        post(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(orderSaveDto)))
                .andReturn().getResponse();
        assertAll("saveOrderAdmin_ShouldThrowBadRequestException",
                () -> assertEquals(400, response.getStatus())
        );
    }

    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testDeleteOrder() throws Exception {
        ObjectId orderId = new ObjectId("6569a29d62996427018dc774");
        String localEndPoint=myEndpoint+"/6569a29d62996427018dc774";
        doNothing().when(ordersService).deleteById(orderId);
        MockHttpServletResponse response = mockMvc.perform(
                        delete(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        verify(ordersService).deleteById(orderId);
    }
    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testDeleteOrder_ShouldThrowNotFoundException() throws Exception {
        String localEndPoint=myEndpoint+"/6569a29d62996427018dc774";
        doThrow(new OrderNotFound("")).when(ordersService).deleteById(any(ObjectId.class));
        MockHttpServletResponse response = mockMvc.perform(
                        delete(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll("deleteOrder_ShouldThrowNotFoundException",
                () -> assertEquals(404, response.getStatus())
        );
    }
    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testUpdateOrder() throws Exception {
        String localEndPoint = myEndpoint + "/6569a29d62996427018dc774";
        when(ordersService.updateOrder(any(ObjectId.class), any(OrderUpdateDto.class))).thenReturn(order1);
        MockHttpServletResponse response = mockMvc.perform(
                        put(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(orderSaveDto)))
                .andReturn().getResponse();

        Order result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("updateOrder",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertEquals(order1,result)
        );
    }
    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testUpdateOrder_ShouldThrowNotFoundException() throws Exception {
        String localEndPoint = myEndpoint + "/6569a29d62996427018dc774";
        when(ordersService.updateOrder(any(ObjectId.class), any(OrderUpdateDto.class))).thenThrow(OrderNotFound.class);
        MockHttpServletResponse response = mockMvc.perform(
                        put(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(orderSaveDto)))
                .andReturn().getResponse();
        assertAll("updateOrder_ShouldThrowNotFoundException",
                () -> assertEquals(404, response.getStatus())
        );
    }
    @Test
    void testExistByClientUUID() throws Exception {
        String localEndPoint = myEndpoint + "/clientExists/550e8400-e29b-41d4-a716-446655440000";
        when(ordersService.existsByClientUUID(any(UUID.class))).thenReturn(true);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("existByClientUUID",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertTrue(Boolean.parseBoolean(response.getContentAsString()))
        );
    }
    @Test
    void testFindByClientUUID() throws Exception {
        String localEndPoint = myEndpoint + "/client/550e8400-e29b-41d4-a716-446655440000";
        var res= new PageImpl<>(orderList);
        when(ordersService.findByClientUUID(any(UUID.class),any(Pageable.class))).thenReturn(res);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Order> result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("testFindByClientUUID",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertEquals(orderList,result.content())
        );
    }
    /*
    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testExistByWorkersUUID() throws Exception {
        String localEndPoint = myEndpoint + "/workerExists/550e8400-e29b-41d4-a716-446655440000";
        when(ordersService.existsByWorkerUUID(any(UUID.class))).thenReturn(true);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("testExistByWorkersUUID",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertTrue(Boolean.parseBoolean(response.getContentAsString()))
        );
    }
    @Test
    void testExistByWorkersUUID_ShouldThrowForbiddenException() throws Exception {
        String localEndPoint = myEndpoint + "/workerExists/550e8400-e29b-41d4-a716-446655440000";
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll("testExistByWorkersUUID_ShouldThrowForbiddenException",
                () -> assertEquals(403, response.getStatus())
        );
    }
    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testFindByWorkersUUID() throws Exception {
        String localEndPoint = myEndpoint + "/worker/550e8400-e29b-41d4-a716-446655440000";
        var res= new PageImpl<>(orderList);
        when(ordersService.findAll(any(Pageable.class))).thenReturn(res);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Order> result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("testFindByWorkersUUID",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertEquals(orderList,result.content())
        );
    }
    @Test
    void testFindByWorkersUUID_ShouldThrowForbiddenException() throws Exception {
        String localEndPoint = myEndpoint + "/worker/550e8400-e29b-41d4-a716-446655440000";
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll("testFindByWorkersUUID_ShouldThrowForbiddenException",
                () -> assertEquals(403, response.getStatus())
        );
    }

     */
    @Test
    void testExistByRestaurantId() throws Exception {
        String localEndPoint = myEndpoint + "/restaurantExists/1";
        when(ordersService.existsByRestaurantId(any(Long.class))).thenReturn(true);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll("testExistByRestaurantId",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertTrue(Boolean.parseBoolean(response.getContentAsString()))
        );
    }
    @Test
    void findByRestaurantId() throws Exception {
        String localEndPoint = myEndpoint + "/restaurant/1";
        var res= new PageImpl<>(orderList);
        when(ordersService.findByRestaurantId(any(Long.class),any(Pageable.class))).thenReturn(res);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Order> result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("testFindByRestaurantId",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertEquals(orderList,result.content())
        );
    }
    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testUpdateIsPaidById() throws Exception {
        String localEndPoint = myEndpoint + "/isPaid/6569a29d62996427018dc774?isPaid=true";
        var res = new PageImpl<>(orderList);
        when(ordersService.updateIsPaidById(any(ObjectId.class), any(Boolean.class))).thenReturn(order2);
        MockHttpServletResponse response = mockMvc.perform(
                        put(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn().getResponse();
        assertAll("testUpdateIsPaid",
                () -> assertEquals(200, response.getStatus())
        );
    }
    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testUpdateIsPaidById_ThrowsOrderNotFoundException() throws Exception {
        String localEndPoint = myEndpoint + "/isPaid/6569a29d62996427018dc774?isPaid=true";

        when(ordersService.updateIsPaidById(any(ObjectId.class), any(Boolean.class))).thenThrow(OrderNotFound.class);
        MockHttpServletResponse response = mockMvc.perform(
                        put(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(true)))
                .andReturn().getResponse();
        assertAll("testUpdateIsPaid",
                () -> assertEquals(404, response.getStatus())
        );
    }
    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testUpdateIsDeletedById() throws Exception {
        String localEndPoint = myEndpoint + "/isDeleted/6569a29d62996427018dc774?isDeleted=true";
        var res = new PageImpl<>(orderList);
        when(ordersService.updateIsDeletedById(any(ObjectId.class), any(Boolean.class))).thenReturn(order2);
        MockHttpServletResponse response = mockMvc.perform(
                        put(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(true)))
                .andReturn().getResponse();
        assertAll("testUpdateIsDeleted",
                () -> assertEquals(200, response.getStatus())
        );
    }
    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testUpdateIsDeletedById_ThrowsOrderNotFoundException() throws Exception {
        String localEndPoint = myEndpoint + "/isDeleted/6569a29d62996427018dc774?isDeleted=true";
        when(ordersService.updateIsDeletedById(any(ObjectId.class), any(Boolean.class))).thenThrow(OrderNotFound.class);
        MockHttpServletResponse response = mockMvc.perform(
                        put(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(true)))
                .andReturn().getResponse();
        assertAll("testUpdateIsDeleted",
                () -> assertEquals(404, response.getStatus())
        );
    }
}