package com.example.macjava.rest.user.service;

import com.example.macjava.rest.user.dto.UserInfoResponse;
import com.example.macjava.rest.user.dto.UserRequest;
import com.example.macjava.rest.user.dto.UserResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;
/**
 * Interfaz que define los métodos que implementará el servicio de usuarios
 */
public interface UsersService {
    /**
     * Devuelve una lista de usuarios
     * @param username opcional: nombre de usuario
     * @param email opcional: email
     * @param isDeleted opcional: si el usuario está eliminado
     * @param pageable objeto pageable
     * @return Lista de usuarios paginada y filtrada
     */
    Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable);

    /**
     * Devuelve un usuario por su ID
     * @param id ID del usuario a buscar
     * @return Usuario
     */
    UserInfoResponse findById(UUID id);

    /**
     * Devuelve un usuario
     * @param userRequest  UserRequest con los datos del usuario a guardar
     * @return UserResponse con los datos del usuario guardado
     */
    UserResponse save(UserRequest userRequest);

    /**
     * Actualiza un usuario
     * @param id ID del usuario a actualizar
     * @param userRequest UserRequest con los datos del usuario a actualizar
     * @return UserResponse con los datos del usuario actualizado
     */
    UserResponse update(UUID id, UserRequest userRequest);

    /**
     * Borra un usuario por su ID
     * @param id ID del usuario a borrar
     */
    void deleteById(UUID id);

}
