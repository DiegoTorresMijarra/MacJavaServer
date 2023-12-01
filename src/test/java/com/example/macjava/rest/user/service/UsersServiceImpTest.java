package com.example.macjava.rest.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.macjava.rest.orders.repositories.OrdersCrudRepository;
import com.example.macjava.rest.user.dto.UserInfoResponse;
import com.example.macjava.rest.user.dto.UserRequest;
import com.example.macjava.rest.user.dto.UserResponse;
import com.example.macjava.rest.user.exception.UserNameOrEmailExists;
import com.example.macjava.rest.user.exception.UserNotFound;
import com.example.macjava.rest.user.mapper.UsersMapper;
import com.example.macjava.rest.user.models.User;
import com.example.macjava.rest.user.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceImpTest {
    private final UserRequest userRequest = UserRequest.builder().username("test").email("test@test.com").build();
    private final User user = User.builder().id(UUID.fromString("12345678-1234-1234-1234-123456789012")).username("test").email("test@test.com").build();
    private final UserResponse userResponse = UserResponse.builder().username("test").email("test@test.com").build();
    private final UserInfoResponse userIResponse = UserInfoResponse.builder().username("test").email("test@test.com").build();
    @Mock
    private UsersRepository usersRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    private OrdersCrudRepository pedidosRepository;
    @Mock
    private UsersMapper usersMapper;
    @InjectMocks
    private UsersServiceImp usersService;

    @Test
    public void testFindAll_NoFilters_ReturnsPageOfUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        Page<User> page = new PageImpl<>(users);
        when(usersRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(usersMapper.toUserResponse(any(User.class))).thenReturn(new UserResponse());

        // Act
        Page<UserResponse> result = usersService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Pageable.unpaged());

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.getTotalElements())
        );

        // Verify
        verify(usersRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testFindById() {
        // Arrange

        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789012");
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidosRepository.findByWorkerUUID(userId,any(Pageable.class))).thenReturn(Page.empty());
        when(usersMapper.toUserInfoResponse(any(User.class), anyList())).thenReturn(userIResponse);
        // Act
        UserInfoResponse result = usersService.findById(userId);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userResponse.getUsername(), result.getUsername()),
                () -> assertEquals(userResponse.getEmail(), result.getEmail())
        );

        // Verify
        verify(usersRepository, times(1)).findById(userId);
        verify(pedidosRepository, times(1)).findPedidosIdsByIdUsuario(userId);
        verify(usersMapper, times(1)).toUserInfoResponse(user, List.of());

    }

    @Test
    public void testFindById_UserNotFound_ThrowsUserNotFound() {
        // Arrange
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFound.class, () -> usersService.findById(userId));

        // Verify
        verify(usersRepository, times(1)).findById(userId);
    }

    @Test
    public void testSave_ValidUserRequest_ReturnsUserResponse() {
        // Arrange


        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(usersMapper.toUser(userRequest)).thenReturn(user);
        when(usersMapper.toUserResponse(user)).thenReturn(userResponse);
        when(usersRepository.save(user)).thenReturn(user);

        // Act
        UserResponse result = usersService.save(userRequest);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userRequest.getUsername(), result.getUsername()),
                () -> assertEquals(userRequest.getEmail(), result.getEmail())
        );

        // Verify
        verify(usersRepository, times(1)).findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString());
        verify(usersMapper, times(1)).toUser(userRequest);
        verify(usersMapper, times(1)).toUserResponse(user);
        verify(usersRepository, times(1)).save(user);

    }

    @Test
    public void testSave_DuplicateUsernameOrEmail_ThrowsUserNameOrEmailExists() {
        // Arrange
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(new User()));

        // Act and Assert
        assertThrows(UserNameOrEmailExists.class, () -> usersService.save(userRequest));
    }

    @Test
    public void testUpdate_ValidUserRequest_ReturnsUserResponse() {
        // Arrange
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(usersMapper.toUser(userRequest, userId)).thenReturn(user);
        when(usersMapper.toUserResponse(user)).thenReturn(userResponse);
        when(usersRepository.save(user)).thenReturn(user);

        // Act
        UserResponse result = usersService.update(userId, userRequest);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userRequest.getUsername(), result.getUsername()),
                () -> assertEquals(userRequest.getEmail(), result.getEmail())
        );

        // Verify
        verify(usersRepository, times(1)).findById(userId);
        verify(usersRepository, times(1)).findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString());
        verify(usersMapper, times(1)).toUser(userRequest, userId);
        verify(usersMapper, times(1)).toUserResponse(user);
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    public void testUpdate_DuplicateUsernameOrEmail_ThrowsUserNameOrEmailExists() {
        // Arrange
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(user));

        // Act and Assert
        assertThrows(UserNameOrEmailExists.class, () -> usersService.update(userId, userRequest));
    }

    @Test
    public void testUpdate_UserNotFound_ThrowsUserNotFound() {
        // Arrange
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFound.class, () -> usersService.update(userId, userRequest));
    }

    @Test
    public void testDeleteById_PhisicalDelete() {
        // Arrange
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidosRepository.existsByIdUsuario(userId)).thenReturn(false);

        // Act
        usersService.deleteById(userId);

        // Verify
        verify(usersRepository, times(1)).delete(user);
        verify(pedidosRepository, times(1)).existsByIdUsuario(userId);
    }

    @Test
    public void testDeleteById_LogicalDelete() {
        // Arrange
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidosRepository.existsByIdUsuario(userId)).thenReturn(true);
        doNothing().when(usersRepository).updateIsDeletedToTrueById(userId);

        // Act
        usersService.deleteById(userId);

        // Assert

        // Verify
        verify(usersRepository, times(1)).updateIsDeletedToTrueById(userId);
        verify(pedidosRepository, times(1)).existsByIdUsuario(userId);
    }

    @Test
    public void testDeleteByIdNotExists() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidosRepository.existsByIdUsuario(userId)).thenReturn(true);

        // Act
        usersService.deleteById(userId);

        // Verify
        verify(usersRepository, times(1)).updateIsDeletedToTrueById(userId);
        verify(pedidosRepository, times(1)).existsByIdUsuario(userId);
    }
}