package com.example.macjava.rest.user.service;

import com.example.macjava.rest.user.dto.UserInfoResponse;
import com.example.macjava.rest.user.dto.UserRequest;
import com.example.macjava.rest.user.dto.UserResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UsersService {

    Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable);

    UserInfoResponse findById(UUID id);

    UserResponse save(UserRequest userRequest);

    UserResponse update(UUID id, UserRequest userRequest);

    void deleteById(UUID id);

}
