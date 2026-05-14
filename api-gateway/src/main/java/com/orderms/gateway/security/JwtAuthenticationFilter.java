package com.orderms.gateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Skip authentication for public auth endpoints
        String path = request.getRequestURI();
        if (path.equals("/api/auth/register") || path.equals("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the Authorization header
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorizedResponse(response, "Missing or invalid Authorization header");
            return;
        }

        final String token = authHeader.substring(7);

        // Validate the token
        if (!jwtService.isTokenValid(token)) {
            sendUnauthorizedResponse(response, "Invalid or expired token");
            return;
        }

        // Extract user info from the token
        String userEmail = jwtService.extractUsername(token);
        String roles = jwtService.extractRoles(token);

        // Set SecurityContext so Spring Security allows the request through
        List<SimpleGrantedAuthority> authorities = Arrays.stream(roles.split(","))
                .filter(role -> !role.isBlank())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userEmail, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Wrap request to inject user headers for downstream services
        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                if ("X-User-Email".equalsIgnoreCase(name)) return userEmail;
                if ("X-User-Roles".equalsIgnoreCase(name)) return roles;
                return super.getHeader(name);
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if ("X-User-Email".equalsIgnoreCase(name))
                    return Collections.enumeration(List.of(userEmail));
                if ("X-User-Roles".equalsIgnoreCase(name))
                    return Collections.enumeration(List.of(roles));
                return super.getHeaders(name);
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                List<String> names = new ArrayList<>(Collections.list(super.getHeaderNames()));
                names.add("X-User-Email");
                names.add("X-User-Roles");
                return Collections.enumeration(names);
            }
        };

        filterChain.doFilter(wrappedRequest, response);
    }

    // Return a structured 401 JSON response
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"" + message + "\"}"
        );
    }
}