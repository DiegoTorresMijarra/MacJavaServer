package com.example.macjava.rest.user.service;

import com.example.macjava.rest.orders.models.Order;
import com.example.macjava.rest.orders.repositories.OrdersCrudRepository;
import com.example.macjava.rest.user.dto.UserInfoResponse;
import com.example.macjava.rest.user.dto.UserRequest;
import com.example.macjava.rest.user.dto.UserResponse;
import com.example.macjava.rest.user.exception.UserNameOrEmailExists;
import com.example.macjava.rest.user.exception.UserNotFound;
import com.example.macjava.rest.user.mapper.UsersMapper;
import com.example.macjava.rest.user.models.User;
import com.example.macjava.rest.user.repository.UsersRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementación de la interfaz UsersService
 * Anotada con @Service para indicar que es un servicio
 */
@Service
@CacheConfig(cacheNames = {"users"})
public class UsersServiceImp implements UsersService{
    UsersRepository usersRepository;
    PasswordEncoder passwordEncoder;
    OrdersCrudRepository ordersCrudRepository;
    private final UsersMapper usersMapper;

    /**
     * Constructor de la clase
     * @param usersRepository Repositorio de usuarios
     * @param pedidosRepository Repositorio de pedidos
     * @param usersMapper Mapper de usuarios
     * @param passwordEncoder Codificador de contraseñas
     */
    public UsersServiceImp(UsersRepository usersRepository, OrdersCrudRepository pedidosRepository, UsersMapper usersMapper, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.ordersCrudRepository = pedidosRepository;
        this.usersMapper = usersMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Devuelve una lista de usuarios paginada y filtrada
     * @param username opcional: nombre de usuario
     * @param email opcional: email
     * @param isDeleted opcional: si el usuario está eliminado
     * @param pageable objeto pageable
     * @return Lista de usuarios paginada y filtrada
     */
    @Override
    public Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable) {
        // Criterio de búsqueda por nombre
        Specification<User> specUsernameUser = (root, query, criteriaBuilder) ->
                username.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de búsqueda por email
        Specification<User> specEmailUser = (root, query, criteriaBuilder) ->
                email.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de búsqueda por borrado
        Specification<User> specIsDeleted = (root, query, criteriaBuilder) ->
                isDeleted.map(m -> criteriaBuilder.equal(root.get("isDeleted"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Combinamos las especificaciones
        Specification<User> criterio = Specification.where(specUsernameUser)
                .and(specEmailUser)
                .and(specIsDeleted);

        // Debe devolver un Page, por eso usamos el findAll de JPA
        return usersRepository.findAll(criterio, pageable).map(usersMapper::toUserResponse);
    }

    /**
     * Devuelve un usuario por su ID
     * @param id ID del usuario a buscar
     * @return Usuario
     */
    @Override
    @Cacheable(key = "#id")
    public UserInfoResponse findById(UUID id) {
        //Creamos un pageable base, cambiar mas adelante
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        // Buscamos el usuario
        var user = usersRepository.findById(id).orElseThrow(() -> new UserNotFound(id));
        // Buscamos sus pedidos
        var pedidos = ordersCrudRepository.findByWorkerUUID(id,pageable).stream().map(Order::toString).toList();//todo
        return usersMapper.toUserInfoResponse(user, pedidos);
    }

    /**
     * Guarda un usuario
     * @param userRequest  UserRequest con los datos del usuario a guardar
     * @return UserResponse con los datos del usuario guardado
     */
    @Override
    @CachePut(key = "#result.id")
    public UserResponse save(UserRequest userRequest) {
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        // No debe existir otro con el mismo username o email
        usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(), userRequest.getEmail())
                .ifPresent(u -> {
                    throw new UserNameOrEmailExists("Ya existe un usuario con ese username o email");
                });
        return usersMapper.toUserResponse(usersRepository.save(usersMapper.toUser(userRequest)));
    }

    /**
     * Actualiza un usuario
     * @param id ID del usuario a actualizar
     * @param userRequest UserRequest con los datos del usuario a actualizar
     * @return UserResponse con los datos del usuario actualizado
     */
    @Override
    @CachePut(key = "#result.id")
    public UserResponse update(UUID id, UserRequest userRequest) {
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        usersRepository.findById(id).orElseThrow(() -> new UserNotFound(id));
        // No debe existir otro con el mismo username o email, y si existe soy yo mismo
        usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(), userRequest.getEmail())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        System.out.println("usuario encontrado: " + u.getId() + " Mi id: " + id);
                        throw new UserNameOrEmailExists("Ya existe un usuario con ese username o email");
                    }
                });
        return usersMapper.toUserResponse(usersRepository.save(usersMapper.toUser(userRequest, id)));
    }

    /**
     * Borra un usuario por su ID (borrado lógico)
     * @param id ID del usuario a borrar
     */
    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(UUID id) {
        User user = usersRepository.findById(id).orElseThrow(() -> new UserNotFound(id));
        //Hacemos el borrado fisico si no hay pedidos
        if (ordersCrudRepository.existsByWorkerUUID(id)) {

            // Si no, lo marcamos como borrado lógico
            usersRepository.updateIsDeletedToTrueById(id);
        } else {
            // Si hay pedidos, lo borramos físicamente
            usersRepository.delete(user);
        }
    }
}
