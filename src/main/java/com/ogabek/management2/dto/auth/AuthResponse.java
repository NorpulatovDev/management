package com.ogabek.management2.dto.auth;


import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private long expiresInMs;
    private UserDto user;
}
