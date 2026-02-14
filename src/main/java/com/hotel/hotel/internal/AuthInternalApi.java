package com.hotel.hotel.internal;

import com.hotel.hotel.internal.dto.TokenValidationRequest;
import com.hotel.hotel.internal.dto.TokenValidationResponse;
import com.hotel.hotel.internal.dto.UserInternalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

/**
 * Cliente interno para comunicarse con auth-service.
 * Usar esta clase cuando necesites validar tokens o consultar usuarios.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInternalApi {

    private final RestTemplate restTemplate;

    @Value("${internal.auth-service.url}")
    private String authServiceUrl;

    /**
     * Valida un token JWT con el servicio de autenticacion.
     *
     * @param token El token JWT a validar
     * @return TokenValidationResponse si es valido, empty si no
     */
    public Optional<TokenValidationResponse> validateToken(String token) {
        try {
            String url = authServiceUrl + "/api/v1/auth/validate";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            TokenValidationRequest body = new TokenValidationRequest(token);
            HttpEntity<TokenValidationRequest> request = new HttpEntity<>(body, headers);

            ResponseEntity<TokenValidationResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    TokenValidationResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
            return Optional.empty();

        } catch (HttpClientErrorException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error connecting to auth-service: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Obtiene informacion de un usuario por su ID.
     *
     * @param userId ID del usuario
     * @param authToken Token de autorizacion para la peticion
     * @return UserInternalResponse si existe, empty si no
     */
    public Optional<UserInternalResponse> getUserById(Long userId, String authToken) {
        try {
            String url = authServiceUrl + "/api/v1/users/" + userId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);

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
