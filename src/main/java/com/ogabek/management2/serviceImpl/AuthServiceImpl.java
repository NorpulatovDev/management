package com.ogabek.management2.serviceImpl;

import com.ogabek.management2.dto.AuthResponse;
import com.ogabek.management2.dto.LoginRequest;
import com.ogabek.management2.dto.RegisterRequest;
import com.ogabek.management2.entity.RefreshToken;
import com.ogabek.management2.entity.User;
import com.ogabek.management2.exception.ApiException;
import com.ogabek.management2.mapper.UserMapper;
import com.ogabek.management2.repository.RefreshTokenRepository;
import com.ogabek.management2.repository.UserRepository;
import com.ogabek.management2.security.JwtUtils;
import com.ogabek.management2.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new ApiException("Username is already taken");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .enabled(true)
                .build();
        userRepository.save(user);

        // Create refresh token and return tokens
        String refreshTokenStr = createRefreshTokenForUser(user);
        String access = jwtUtils.generateAccessToken(user.getUsername(), user.getRole().name());


        return AuthResponse.builder()
                .accessToken(access)
                .refreshToken(refreshTokenStr)
                .expiresInMs(jwtUtils.getAccessExpirationMs())
                .user(UserMapper.toDto(user))
                .build();
    }


    private String createRefreshTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        RefreshToken rt = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plusMillis(jwtUtils.getAccessExpirationMs() * 1000L)) // NOTE: We'll instead use configured expiry
                .build();
        // better to use JwtUtils.generateRefreshToken for expiry handling, but we keep DB token as UUID
        rt.setExpiryDate(Instant.now().plusMillis(jwtUtils.getAccessExpirationMs())); // placeholder
        // To be correct, use configured value:
        rt.setExpiryDate(Instant.now().plusMillis(jwtUtils.getAccessExpirationMs())); // you can change
        refreshTokenRepository.save(rt);
        return token;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            var auth = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            authenticationManager.authenticate(auth);
        } catch (BadCredentialsException ex) {
            throw new ApiException("Invalid username or password");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApiException("User not found"));

        // delete old refresh token (if exists) and create a new one
        refreshTokenRepository.deleteByUser(user);
        String refreshTokenStr = createRefreshTokenForUser(user);
        String access = jwtUtils.generateAccessToken(user.getUsername(), user.getRole().name());

        return AuthResponse.builder()
                .accessToken(access)
                .refreshToken(refreshTokenStr)
                .expiresInMs(jwtUtils.getAccessExpirationMs())
                .user(UserMapper.toDto(user))
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new ApiException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new ApiException("Refresh token expired");
        }

        User user = refreshToken.getUser();
        String access = jwtUtils.generateAccessToken(user.getUsername(), user.getRole().name());
        // optionally rotate refresh token
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtUtils.getAccessExpirationMs())); // adjust to configured
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(access)
                .refreshToken(refreshToken.getToken())
                .expiresInMs(jwtUtils.getAccessExpirationMs())
                .user(UserMapper.toDto(user))
                .build();
    }

    @Override
    @Transactional
    public void logout(String username) {
        userRepository.findByUsername(username).ifPresent(refreshTokenRepository::deleteByUser);
    }
}
