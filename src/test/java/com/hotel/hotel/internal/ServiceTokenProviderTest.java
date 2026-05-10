package com.hotel.hotel.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceTokenProviderTest {

    @Mock private RestTemplate restTemplate;

    @InjectMocks
    private ServiceTokenProvider serviceTokenProvider;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(serviceTokenProvider, "authServiceUrl", "http://auth-service");
        ReflectionTestUtils.setField(serviceTokenProvider, "clientId", "hotel-client");
        ReflectionTestUtils.setField(serviceTokenProvider, "clientSecret", "secret");
        // Reset the cached snapshot so each test starts fresh
        ReflectionTestUtils.setField(serviceTokenProvider, "current",
                new java.util.concurrent.atomic.AtomicReference<>(null));
    }

    // ==================== getToken — fresh fetch ====================

    @Test
    void getTokenFetchaTokenCuandoCacheEstaVacio() {
        Map<String, Object> responseBody = Map.of(
                "access_token", "my-token-123",
                "expires_in", 300L
        );
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(responseBody));

        String token = serviceTokenProvider.getToken();

        assertThat(token).isEqualTo("my-token-123");
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Map.class));
    }

    @Test
    void getTokenUsaCacheCuandoTokenEsFresco() {
        Map<String, Object> responseBody = Map.of(
                "access_token", "cached-token",
                "expires_in", 300L
        );
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(responseBody));

        // Primera llamada — fetch
        serviceTokenProvider.getToken();
        // Segunda llamada — deberia usar cache
        String token = serviceTokenProvider.getToken();

        assertThat(token).isEqualTo("cached-token");
        // Solo UN fetch al backend, la segunda reutiliza el cache
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Map.class));
    }

    @Test
    void getTokenRefrescaCuandoCacheEstaExpirado() {
        Map<String, Object> responseBody = Map.of(
                "access_token", "new-token",
                "expires_in", 300L
        );
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(responseBody));

        // Injectar un snapshot expirado directamente
        injectExpiredSnapshot("old-expired-token");

        String token = serviceTokenProvider.getToken();

        assertThat(token).isEqualTo("new-token");
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Map.class));
    }

    // ==================== fetchFromAuth — edge cases ====================

    @Test
    void getTokenLanzaIllegalStateExceptionCuandoResponseBodyEsNull() {
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(null));

        assertThatThrownBy(() -> serviceTokenProvider.getToken())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("token");
    }

    @Test
    void getTokenLanzaIllegalStateExceptionCuandoAccessTokenEsNull() {
        Map<String, Object> responseBody = Map.of("expires_in", 300L);
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(responseBody));

        assertThatThrownBy(() -> serviceTokenProvider.getToken())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void getTokenUsaTtlPorDefectoCuandoExpiresInNoEsNumerico() {
        Map<String, Object> responseBody = Map.of(
                "access_token", "token-default-ttl",
                "expires_in", "no-es-un-numero"
        );
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(responseBody));

        String token = serviceTokenProvider.getToken();

        // Should NOT throw, uses DEFAULT_TOKEN_TTL_SECONDS = 300
        assertThat(token).isEqualTo("token-default-ttl");
    }

    @Test
    void getTokenParsaExpiresInComoString() {
        Map<String, Object> responseBody = Map.of(
                "access_token", "token-string-ttl",
                "expires_in", "600"
        );
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(responseBody));

        String token = serviceTokenProvider.getToken();

        assertThat(token).isEqualTo("token-string-ttl");
    }

    @Test
    void getTokenUsaTtlPorDefectoCuandoExpiresInEsNullOTipoInvalido() {
        // Usar un Map mutable para poder incluir null value
        java.util.HashMap<String, Object> responseBody = new java.util.HashMap<>();
        responseBody.put("access_token", "token-null-ttl");
        responseBody.put("expires_in", null);

        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(responseBody));

        String token = serviceTokenProvider.getToken();

        assertThat(token).isEqualTo("token-null-ttl");
    }

    // ==================== helpers ====================

    private void injectExpiredSnapshot(String tokenValue) {
        // Create an expired snapshot by using an expiry in the past
        Instant pastExpiry = Instant.now().minusSeconds(60);

        // Use reflection to create the private record TokenSnapshot
        try {
            Class<?> snapshotClass = Class.forName(
                    "com.hotel.hotel.internal.ServiceTokenProvider$TokenSnapshot");
            java.lang.reflect.Constructor<?> ctor = snapshotClass.getDeclaredConstructor(
                    String.class, Instant.class);
            ctor.setAccessible(true);
            Object expiredSnapshot = ctor.newInstance(tokenValue, pastExpiry);

            @SuppressWarnings("unchecked")
            java.util.concurrent.atomic.AtomicReference<Object> current =
                    (java.util.concurrent.atomic.AtomicReference<Object>)
                            ReflectionTestUtils.getField(serviceTokenProvider, "current");
            current.set(expiredSnapshot);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject expired snapshot", e);
        }
    }
}
