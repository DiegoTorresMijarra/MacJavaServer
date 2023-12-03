package com.example.macjava.rest.orders.services;

import com.example.macjava.rest.clients.models.Client;
import com.example.macjava.rest.clients.repository.ClientsRepository;
import com.example.macjava.rest.orders.dto.OrderSaveDto;
import com.example.macjava.rest.orders.dto.OrderUpdateDto;
import com.example.macjava.rest.orders.exceptions.OrderBadRequest;
import com.example.macjava.rest.orders.exceptions.OrderNotFound;
import com.example.macjava.rest.orders.models.Order;
import com.example.macjava.rest.orders.models.OrderedProduct;
import com.example.macjava.rest.orders.repositories.OrdersCrudRepository;
import com.example.macjava.rest.products.models.Product;
import com.example.macjava.rest.products.repository.ProductRepository;
import com.example.macjava.rest.restaurants.modelos.Restaurant;
import com.example.macjava.rest.restaurants.repositories.RestaurantRepository;
import com.example.macjava.rest.workers.models.Workers;
import com.example.macjava.rest.workers.repositories.WorkersCrudRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdersServiceImplTest {

    @Mock
    private OrdersCrudRepository ordersCrudRepository;

    @Mock
    private ClientsRepository clientsRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private WorkersCrudRepository workersCrudRepository;

    @InjectMocks
    private OrdersServiceImpl ordersService;

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

    private  Order order1= Order.builder()
            .id(new ObjectId())
            .clientUUID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"))
            .workerUUID(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
            .restaurantId(1L)
            .orderedProducts(validList)
            .build();
    private Order order2= Order.builder()
            .id(new ObjectId())
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

    @BeforeEach
    void setUp(){
        order1.setOrderedProducts(validList);
        orderList = List.of(order1,order2);
    }
    @Test
    void testFindAll() {
        // Mock data
        when(ordersCrudRepository.findAll()).thenReturn(orderList);

        // Test the method
        List<Order> result = ordersService.findAll();

        // Verify the result
        assertAll("testFindAll",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size())
        );

        // Verify that the repository findAll method is called
        verify(ordersCrudRepository).findAll();
    }

    @Test
    void testFindAllPageable_ShouldReturnAll_whenNoParamsPassed() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Order> expectedPage = new PageImpl<>(orderList);
        when(ordersCrudRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Order> result = ordersService.findAll(pageable);

        verify(ordersCrudRepository).findAll(pageable);

        assertAll("testFindAllPageable_ShouldReturnAll_whenNoParamsPassed",
                () -> assertNotNull(result),
                ()-> assertFalse(result.isEmpty()),
                () -> assertEquals(2, result.getTotalElements())
        );
    }

    @Test
    void testFindAllPageable_ShouldReturnCorrect_whenParamsPassed() {
        // Mock data
        Pageable pageable = PageRequest.of(0, 1, Sort.by("id").ascending());
        Page<Order> expectedPage = new PageImpl<>(orderList.subList(0, 1));

        when(ordersCrudRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Order> result = ordersService.findAll(pageable);

        assertAll("testFindAllPageable_ShouldReturnCorrect_whenParamsPassed",
        ()-> assertNotNull(result),
        ()-> assertFalse(result.isEmpty()),
        () -> assertEquals(1, result.getTotalElements())
        );
        verify(ordersCrudRepository).findAll(pageable);
    }



    @Test
    void testFindById_ShouldReturnCorrect_whenParamsPassed() {
        ObjectId orderId = new ObjectId(order1.getId());
        when(ordersCrudRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(order1));

        Order result = ordersService.findById(orderId);

        assertAll("testFindById_ShouldReturnCorrect_whenParamsPassed",
                () -> assertNotNull(result),
                () -> assertEquals(order1, result)
        );
        verify(ordersCrudRepository).findById(orderId);
    }
    @Test
    void testFindById_ShouldThrowException_whenIncorrectParamsPassed() {
        ObjectId orderId = new ObjectId();
        when(ordersCrudRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());

        assertThrows(OrderNotFound.class, () -> ordersService.findById(orderId));
        verify(ordersCrudRepository).findById(orderId);
    }

    @Test
    void testSave_ShouldReturnCorrect() {

        when(workersCrudRepository.findById(any(UUID.class))).thenReturn(Optional.of(new Workers()));
        when(clientsRepository.findById(any(UUID.class))).thenReturn(Optional.of(new Client()));
        when(restaurantRepository.findById(any(Long.class))).thenReturn(Optional.of(new Restaurant()));
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));

        when(ordersCrudRepository.save(any(Order.class))).thenReturn(order1);

        Order result = ordersService.save(orderSaveDto);

        assertAll("testSave_ShouldReturnCorrect",
                () -> assertNotNull(result),
                () -> assertEquals(order1, result),
                () -> assertEquals(product.getPrecio(),result.getOrderedProducts().get(0).getProductPrice()),
                ()-> assertTrue(product.getStock()>= result.getOrderedProducts().get(0).getQuantity())
        );
        verify(ordersCrudRepository).save(any(Order.class));
    }

    @Test
    void testSave_ShouldThrowException_whenIncorrectParamsPassed() {

        when(workersCrudRepository.findById(any(UUID.class))).thenReturn(Optional.of(new Workers()));
        when(clientsRepository.findById(any(UUID.class))).thenReturn(Optional.of(new Client()));
        when(restaurantRepository.findById(any(Long.class))).thenReturn(Optional.of(new Restaurant()));

        product.setStock(-1);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));

       assertThrows(OrderBadRequest.class, () -> ordersService.save(orderSaveDto));
    }

    @Test
    void testDeleteById() {
        ObjectId orderId = new ObjectId();
        when(ordersCrudRepository.findById(orderId)).thenReturn(Optional.of(new Order()));
        doNothing().when(ordersCrudRepository).deleteById(orderId);

        ordersService.deleteById(orderId);

        verify(ordersCrudRepository).findById(orderId);
        verify(ordersCrudRepository).deleteById(orderId);
    }

    @Test
    void testDeleteById_ShouldThrowException_whenIncorrectParamsPassed() {
        ObjectId orderId = new ObjectId();
        when(ordersCrudRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFound.class, () -> ordersService.deleteById(orderId));
        verify(ordersCrudRepository).findById(orderId);
    }
    @Test
    void testUpdateOrder_ShouldReturnCorrect() {
        ObjectId orderId = new ObjectId(order1.getId());
        OrderUpdateDto orderUpdateDto = OrderUpdateDto.builder().build();
        when(ordersCrudRepository.findById(orderId)).thenReturn(Optional.of(order1));
        when(ordersCrudRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = ordersService.updateOrder(orderId, orderUpdateDto);

        verify(ordersCrudRepository).findById(orderId);
        verify(ordersCrudRepository).save(any(Order.class));

        assertNotNull(result);
    }

    @Test
    void testUpdateIsPaidById() {
        ObjectId orderId = new ObjectId(order1.getId());
        Order updatedIsPaid= Order.builder()
                .id(orderId)
                .isPaid(true)
                .build();
        when(ordersCrudRepository.findById(orderId)).thenReturn(Optional.of(order1));
        when(ordersCrudRepository.save(any(Order.class))).thenReturn(updatedIsPaid);

        Order result = ordersService.updateIsPaidById(orderId, true);

        assertAll("testUpdateIsPaidById",
                () -> assertNotNull(result),
                () -> assertTrue(result.getIsPaid()),
                ()-> assertEquals(updatedIsPaid.getId(), result.getId())
        );
        verify(ordersCrudRepository).findById(orderId);
        verify(ordersCrudRepository).save(any(Order.class));
    }
    @Test
    void testUpdateIsDeletedById() {
        ObjectId orderId = new ObjectId(order1.getId());
        Order updatedIsDeleted= Order.builder()
                .id(orderId)
                .isDeleted(true)
                .build();
        when(ordersCrudRepository.findById(orderId)).thenReturn(Optional.of(order1));
        when(ordersCrudRepository.save(any(Order.class))).thenReturn(updatedIsDeleted);

        Order result = ordersService.updateIsDeletedById(orderId, true);

        assertAll("testUpdateIsPaidById",
                () -> assertNotNull(result),
                () -> assertTrue(result.getIsDeleted()),
                ()-> assertEquals(updatedIsDeleted.getId(), result.getId())
        );
        verify(ordersCrudRepository).findById(orderId);
        verify(ordersCrudRepository).save(any(Order.class));
    }

    @Test
    void findByClientUUID_ShouldReturnAll() {
        UUID uuid=UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Order> expectedPage = new PageImpl<>(orderList.subList(0,1));
        when(ordersCrudRepository.findByClientUUID(uuid,pageable)).thenReturn(expectedPage);

        Page<Order> result = ordersService.findByClientUUID(uuid,pageable);

        assertAll("findByClientUUID_ShouldReturnAll",
                () -> assertNotNull(result),
                ()-> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                ()->assertEquals(uuid, result.stream().findFirst().get().getClientUUID())
        );
        verify(ordersCrudRepository).findByClientUUID(uuid,pageable);
    }

    @Test
    void existsByClientUUID_ShouldReturnTrue() {
        UUID uuid=UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        when(ordersCrudRepository.existsByClientUUID(uuid)).thenReturn(true);

        Boolean result = ordersService.existsByClientUUID(uuid);

        assertAll("existsByClientUUID_ShouldReturnTrue",
                () -> assertNotNull(result),
                ()-> assertTrue(result)
        );
        verify(ordersCrudRepository).existsByClientUUID(uuid);
    }

    @Test
    void findByRestaurantId_ShouldReturnAll() {
        Long id=1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Order> expectedPage = new PageImpl<>(orderList.subList(0,1));
        when(ordersCrudRepository.findByRestaurantId(id,pageable)).thenReturn(expectedPage);

        Page<Order> result = ordersService.findByRestaurantId(id,pageable);

        assertAll("findByRestaurantId_ShouldReturnAll",
                () -> assertNotNull(result),
                ()-> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                ()->assertEquals(id, result.stream().findFirst().get().getRestaurantId())
        );
        verify(ordersCrudRepository).findByRestaurantId(id,pageable);
    }

    @Test
    void existsByRestaurantId_ShouldReturnTrue() {
        Long id=1L;
        when(ordersCrudRepository.existsByRestaurantId(id)).thenReturn(true);

        Boolean result = ordersService.existsByRestaurantId(id);

        assertAll("existsByRestaurantId_ShouldReturnTrue",
                () -> assertNotNull(result),
                ()-> assertTrue(result)
        );
        verify(ordersCrudRepository).existsByRestaurantId(id);
    }
}
       
