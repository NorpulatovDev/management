package com.ogabek.management2.service;

import com.ogabek.management2.dto.auth.AuthResponse;
import com.ogabek.management2.dto.auth.LoginRequest;
import com.ogabek.management2.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshToken(String refreshToken);
    void logout(String username);
}
