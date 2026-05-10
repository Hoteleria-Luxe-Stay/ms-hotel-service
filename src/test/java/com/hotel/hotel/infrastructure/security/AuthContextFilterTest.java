package com.hotel.hotel.infrastructure.security;

import com.hotel.hotel.internal.dto.TokenValidationResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthContextFilterTest {

    @Mock private JwtDecoder jwtDecoder;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @InjectMocks
    private AuthContextFilter authContextFilter;

    // ==================== doFilterInternal — sin header ====================

    @Test
    void doFilterInternalContinuaChainCuandoNoHayAuthorizationHeader() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        authContextFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtDecoder, never()).decode(any());
        verify(request, never()).setAttribute(any(), any());
    }

    @Test
    void doFilterInternalContinuaChainCuandoHeaderNoEsBearer() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Basic abc123");

        authContextFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtDecoder, never()).decode(any());
    }

    // ==================== doFilterInternal — JWT invalido ====================

    @Test
    void doFilterInternalContinuaChainSinSetAtributoCuandoJwtEsInvalido() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer invalid-token");
        when(jwtDecoder.decode("invalid-token")).thenThrow(new JwtException("Token invalido"));

        authContextFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(request, never()).setAttribute(any(), any());
    }

    // ==================== doFilterInternal — JWT valido con userId y roles ====================

    @Test
    void doFilterInternalSetaAtributoDeContextoCuandoJwtTieneUserIdYRoles() throws Exception {
        Jwt jwt = Jwt.withTokenValue("valid-token")
                .header("alg", "RS256")
                .subject("user@luxestay.com")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claim("userId", 42L)
                .claim("roles", "ROLE_USER")
                .build();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer valid-token");
        when(jwtDecoder.decode("valid-token")).thenReturn(jwt);

        authContextFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(request, times(1)).setAttribute(
                eq(AuthContextFilter.AUTH_CONTEXT_KEY),
                any(TokenValidationResponse.class)
        );
    }

    @Test
    void doFilterInternalSetaRoleSinPrefixoROLE_CuandoJwtTieneRoles() throws Exception {
        Jwt jwt = Jwt.withTokenValue("admin-token")
                .header("alg", "RS256")
                .subject("admin@luxestay.com")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claim("userId", 1L)
                .claim("roles", "ROLE_ADMIN")
                .build();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer admin-token");
        when(jwtDecoder.decode("admin-token")).thenReturn(jwt);

        // Capture the attribute value to verify role stripping
        final TokenValidationResponse[] captured = {null};
        org.mockito.ArgumentCaptor<Object> captor = org.mockito.ArgumentCaptor.forClass(Object.class);

        authContextFilter.doFilterInternal(request, response, filterChain);

        verify(request).setAttribute(eq(AuthContextFilter.AUTH_CONTEXT_KEY), captor.capture());
        TokenValidationResponse capturedAuth = (TokenValidationResponse) captor.getValue();
        assertThat(capturedAuth.getRole()).isEqualTo("ADMIN");
        assertThat(capturedAuth.getEmail()).isEqualTo("admin@luxestay.com");
        assertThat(capturedAuth.getValid()).isTrue();
    }

    // ==================== doFilterInternal — JWT sin userId ni roles ====================

    @Test
    void doFilterInternalNOSetaAtributoCuandoJwtNoTieneUserIdNiRoles() throws Exception {
        Jwt jwt = Jwt.withTokenValue("service-token")
                .header("alg", "RS256")
                .subject("hotel-service")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claim("client_id", "hotel-service")
                .build();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer service-token");
        when(jwtDecoder.decode("service-token")).thenReturn(jwt);

        authContextFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(request, never()).setAttribute(any(), any());
    }

    // ==================== doFilterInternal — JWT con solo userId (sin roles) ====================

    @Test
    void doFilterInternalSetaContextoCuandoSoloHayUserId() throws Exception {
        Jwt jwt = Jwt.withTokenValue("user-only-token")
                .header("alg", "RS256")
                .subject("user@luxestay.com")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claim("userId", 5L)
                .build();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer user-only-token");
        when(jwtDecoder.decode("user-only-token")).thenReturn(jwt);

        authContextFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(request, times(1)).setAttribute(
                eq(AuthContextFilter.AUTH_CONTEXT_KEY),
                any(TokenValidationResponse.class)
        );
    }

    // ==================== doFilterInternal — JWT con solo roles (sin userId) ====================

    @Test
    void doFilterInternalSetaContextoCuandoSoloHayRoles() throws Exception {
        Jwt jwt = Jwt.withTokenValue("roles-only-token")
                .header("alg", "RS256")
                .subject("user@luxestay.com")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claim("roles", "ROLE_USER")
                .build();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer roles-only-token");
        when(jwtDecoder.decode("roles-only-token")).thenReturn(jwt);

        authContextFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(request, times(1)).setAttribute(
                eq(AuthContextFilter.AUTH_CONTEXT_KEY),
                any(TokenValidationResponse.class)
        );
    }
}
