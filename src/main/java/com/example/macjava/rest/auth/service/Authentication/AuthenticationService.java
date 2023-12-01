package com.example.macjava.rest.auth.service.Authentication;

import com.example.macjava.rest.auth.dto.JwtAuthResponse;
import com.example.macjava.rest.auth.dto.UserSignInRequest;
import com.example.macjava.rest.auth.dto.UserSignUpRequest;

public interface AuthenticationService {
    JwtAuthResponse signUp(UserSignUpRequest request);

    JwtAuthResponse signIn(UserSignInRequest request);
}
