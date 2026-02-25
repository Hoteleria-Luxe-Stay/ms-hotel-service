package com.hotel.hotel.internal;

import com.hotel.hotel.internal.dto.UserInternalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

/**
 * Cliente interno para comunicarse con auth-service.
 * Usar esta clase cuando necesites consultar usuarios.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInternalApi {

    private final RestTemplate restTemplate;
    private final ServiceTokenProvider serviceTokenProvider;

    @Value("${internal.auth-service.url}")
    private String authServiceUrl;

    /**
     * Obtiene informacion de un usuario por su ID.
     *
     * @param userId ID del usuario
     * @return UserInternalResponse si existe, empty si no
     */
    public Optional<UserInternalResponse> getUserById(Long userId) {
        try {
            String url = authServiceUrl + "/api/v1/users/" + userId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(serviceTokenProvider.getToken());

            HttpEntity<?> request = new HttpEntity<>(headers);

            ResponseEntity<UserInternalResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    UserInternalResponse.class
            );

            return Optional.ofNullable(response.getBody());

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("User not found: {}", userId);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error fetching user from auth-service: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
