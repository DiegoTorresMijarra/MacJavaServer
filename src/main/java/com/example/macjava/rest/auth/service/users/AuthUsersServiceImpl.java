package com.example.macjava.rest.auth.service.users;

import com.example.macjava.rest.auth.repository.AuthUsersRepository;
import com.example.macjava.rest.user.exception.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementación de la interfaz AuthUsersService
 */
@Service("userDetailsService")
public class AuthUsersServiceImpl implements AuthUsersService{
    private final AuthUsersRepository authUsersRepository;

    @Autowired
    public AuthUsersServiceImpl(AuthUsersRepository authUsersRepository) {
        this.authUsersRepository = authUsersRepository;
    }

    /**
     * Carga los datos de un usuario a partir de su username
     * @param username nombre de usuario
     * @return usuario
     * @throws UserNotFound si no se encuentra el usuario
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFound {
        return authUsersRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("Usuario con username " + username + " no encontrado"));
    }
}
