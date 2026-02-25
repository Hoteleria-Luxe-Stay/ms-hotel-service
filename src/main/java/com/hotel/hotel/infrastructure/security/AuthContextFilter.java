package com.hotel.hotel.infrastructure.security;

import com.hotel.hotel.internal.dto.TokenValidationResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthContextFilter extends OncePerRequestFilter {

    public static final String AUTH_CONTEXT_KEY = "AUTH_CONTEXT";

    private final JwtDecoder jwtDecoder;

    public AuthContextFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                Jwt jwt = jwtDecoder.decode(token);

                Object userId = jwt.getClaim("userId");
                String roles = jwt.getClaim("roles");

                if (userId != null || roles != null) {
                    TokenValidationResponse validation = new TokenValidationResponse();
                    validation.setValid(true);
                    validation.setEmail(jwt.getSubject());
                    validation.setUserId(jwt.getClaim("userId"));

                    if (roles != null) {
                        String role = roles.replace("ROLE_", "");
                        validation.setRole(role);
                    }

                    request.setAttribute(AUTH_CONTEXT_KEY, validation);
                }
            } catch (JwtException e) {
                // Token invalido o expirado: no se setea contexto
            }
        }

        filterChain.doFilter(request, response);
    }
}
