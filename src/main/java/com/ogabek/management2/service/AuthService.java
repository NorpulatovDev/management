package com.ogabek.management2.service;

import com.ogabek.management2.dto.AuthResponse;
import com.ogabek.management2.dto.LoginRequest;
import com.ogabek.management2.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshToken(String refreshToken);
    void logout(String username);
}
