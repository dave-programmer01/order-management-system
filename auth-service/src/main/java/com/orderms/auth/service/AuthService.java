package com.orderms.auth.service;

import com.orderms.auth.dto.AuthResponse;
import com.orderms.auth.dto.LoginRequest;
import com.orderms.auth.dto.RegisterRequest;
import com.orderms.auth.entity.Role;
import com.orderms.auth.entity.User;
import com.orderms.auth.exception.UserAlreadyExistsException;
import com.orderms.auth.repository.UserRepository;
import com.orderms.auth.security.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Register a new user and return a JWT
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(Role.USER))
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    // Authenticate and return a JWT
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    // Validate a token and return its claims
    public Map<String, Object> validateToken(String token) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", jwtService.extractUsername(token));
        claims.put("valid", jwtService.isTokenValid(token));
        Claims allClaims = jwtService.extractClaim(token, c -> c);
        claims.put("roles", allClaims.get("roles"));
        return claims;
    }
}