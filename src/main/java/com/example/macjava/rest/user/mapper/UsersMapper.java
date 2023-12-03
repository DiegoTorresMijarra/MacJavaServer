package com.example.macjava.rest.user.mapper;

import com.example.macjava.rest.user.dto.UserInfoResponse;
import com.example.macjava.rest.user.dto.UserRequest;
import com.example.macjava.rest.user.dto.UserResponse;
import com.example.macjava.rest.user.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
/**
 * Clase que se encarga de mapear los objetos de usuario.
 */
@Component
public class UsersMapper {
    /**
     * Método que mapea un objeto UserRequest a un objeto User.
     * @param request Objeto UserRequest a mapear
     * @return Objeto User mapeado
     */
    public User toUser(UserRequest request) {
        return User.builder()
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .isDeleted(request.getIsDeleted())
                .build();
    }

    /**
     * Método que mapea un objeto UserRequest a un objeto User.
     * @param request Objeto UserRequest a mapear
     * @param id ID del usuario
     * @return Objeto User mapeado
     */
    public User toUser(UserRequest request, UUID id) {
        return User.builder()
                .id(id)
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .isDeleted(request.getIsDeleted())
                .build();
    }

    /**
     * Método que mapea un objeto User a un objeto UserResponse.
     * @param user  Objeto User a mapear
     * @return Objeto UserResponse mapeado
     */
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isDeleted(user.getIsDeleted())
                .build();
    }

    /**
     * Método que mapea un objeto User a un objeto UserInfoResponse.
     * @param user Objeto User a mapear
     * @param pedidos Lista de pedidos del usuario
     * @return Objeto UserInfoResponse mapeado
     */
    public UserInfoResponse toUserInfoResponse(User user, List<String> pedidos) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isDeleted(user.getIsDeleted())
                .pedidos(pedidos)
                .build();
    }
}
