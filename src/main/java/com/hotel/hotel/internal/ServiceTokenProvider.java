package com.hotel.hotel.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Cache thread-safe del token tecnico OAuth2 (client_credentials).
 *
 * Round 7: refactor para concurrencia lock-free con AtomicReference.
 * Lectura sin lock; refresh con un solo thread (el que gana el lock) — los demas
 * reusan el snapshot fresco que dejo el primero (double-check inside the lock).
 */
@Component
public class ServiceTokenProvider {

    private static final long EXPIRY_SAFETY_MARGIN_SECONDS = 30L;
    private static final long DEFAULT_TOKEN_TTL_SECONDS = 300L;

    private final RestTemplate restTemplate;
    private final AtomicReference<TokenSnapshot> current = new AtomicReference<>(null);
    private final ReentrantLock refreshLock = new ReentrantLock();

    @Value("${internal.auth-service.url}")
    private String authServiceUrl;

    @Value("${internal.auth-service.client-id}")
    private String clientId;

    @Value("${internal.auth-service.client-secret}")
    private String clientSecret;

    public ServiceTokenProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getToken() {
        TokenSnapshot snapshot = current.get();
        if (snapshot != null && snapshot.isFresh()) {
            return snapshot.token();
        }
        return refreshIfNeeded();
    }

    private String refreshIfNeeded() {
        refreshLock.lock();
        try {
            TokenSnapshot snapshot = current.get();
            if (snapshot != null && snapshot.isFresh()) {
                return snapshot.token();
            }
            TokenSnapshot fresh = fetchFromAuth();
            current.set(fresh);
            return fresh.token();
        } finally {
            refreshLock.unlock();
        }
    }

    private TokenSnapshot fetchFromAuth() {
        String url = authServiceUrl + "/api/v1/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        Map responseBody = response.getBody();
        if (responseBody == null || responseBody.get("access_token") == null) {
            throw new IllegalStateException("No se pudo obtener token tecnico");
        }

        String token = responseBody.get("access_token").toString();
        long ttl = parseExpiresIn(responseBody.get("expires_in"));
        Instant expiresAt = Instant.now().plusSeconds(ttl);
        return new TokenSnapshot(token, expiresAt);
    }

    private long parseExpiresIn(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value instanceof String text) {
            try {
                return Long.parseLong(text);
            } catch (NumberFormatException ex) {
                return DEFAULT_TOKEN_TTL_SECONDS;
            }
        }
        return DEFAULT_TOKEN_TTL_SECONDS;
    }

    private record TokenSnapshot(String token, Instant expiresAt) {
        boolean isFresh() {
            return token != null
                    && expiresAt != null
                    && Instant.now().isBefore(expiresAt.minusSeconds(EXPIRY_SAFETY_MARGIN_SECONDS));
        }
    }
}
