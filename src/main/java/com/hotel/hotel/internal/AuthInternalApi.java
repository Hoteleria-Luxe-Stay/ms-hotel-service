package com.hotel.hotel.internal;

import com.hotel.hotel.helpers.exceptions.ServiceUnavailableException;
import com.hotel.hotel.internal.dto.UserInternalResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Cliente interno para comunicarse con auth-service.
 * Protegido por Circuit Breaker + Retry.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInternalApi {

    private static final String CB_NAME = "authService";

    private final RestTemplate restTemplate;
    private final ServiceTokenProvider serviceTokenProvider;

    @Value("${internal.auth-service.url}")
    private String authServiceUrl;

    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackGetUserById")
    @Retry(name = CB_NAME)
    public Optional<UserInternalResponse> getUserById(Long userId) {
        try {
            String url = authServiceUrl + "/api/v1/users/" + userId;
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(serviceTokenProvider.getToken());
            HttpEntity<?> request = new HttpEntity<>(headers);
            ResponseEntity<UserInternalResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, request, UserInternalResponse.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("User not found: {}", userId);
            return Optional.empty();
        }
    }

    @SuppressWarnings("unused")
    private Optional<UserInternalResponse> fallbackGetUserById(Long userId, Throwable t) {
        log.error("auth-service unavailable getting user {}: {}", userId, t.getMessage());
        throw new ServiceUnavailableException("auth-service", t);
    }
}
