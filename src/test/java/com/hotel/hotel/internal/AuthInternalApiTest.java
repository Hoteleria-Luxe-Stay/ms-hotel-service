package com.hotel.hotel.internal;

import com.hotel.hotel.helpers.exceptions.ServiceUnavailableException;
import com.hotel.hotel.internal.dto.UserInternalResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthInternalApiTest {

    @Mock private RestTemplate restTemplate;
    @Mock private ServiceTokenProvider serviceTokenProvider;

    @InjectMocks
    private AuthInternalApi authInternalApi;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authInternalApi, "authServiceUrl", "http://auth-service");
        when(serviceTokenProvider.getToken()).thenReturn("test-service-token");
    }

    // ==================== getUserById — happy path ====================

    @Test
    void getUserByIdRetornaUserCuandoExiste() {
        UserInternalResponse userResponse = UserInternalResponse.builder()
                .id(1L)
                .username("johndoe")
                .email("john@luxestay.com")
                .role("USER")
                .build();

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(UserInternalResponse.class)))
                .thenReturn(ResponseEntity.ok(userResponse));

        Optional<UserInternalResponse> result = authInternalApi.getUserById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@luxestay.com");
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void getUserByIdRetornaBodyNullComoOptionalEmpty() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(UserInternalResponse.class)))
                .thenReturn(ResponseEntity.ok(null));

        Optional<UserInternalResponse> result = authInternalApi.getUserById(1L);

        assertThat(result).isEmpty();
    }

    // ==================== getUserById — 404 not found ====================

    @Test
    void getUserByIdRetornaEmptyCuandoUserNoExiste() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(UserInternalResponse.class)))
                .thenThrow(HttpClientErrorException.NotFound.create(
                        HttpStatus.NOT_FOUND, "Not Found", null, null, null));

        Optional<UserInternalResponse> result = authInternalApi.getUserById(99L);

        assertThat(result).isEmpty();
    }

    // ==================== fallbackGetUserById ====================

    @Test
    void fallbackGetUserByIdLanzaServiceUnavailableException() {
        Throwable causa = new RuntimeException("auth-service down");

        assertThatThrownBy(() ->
                ReflectionTestUtils.invokeMethod(authInternalApi, "fallbackGetUserById", 1L, causa)
        ).isInstanceOf(ServiceUnavailableException.class)
                .hasMessageContaining("auth-service");
    }

    @Test
    void fallbackGetUserByIdIncluyCausaOriginal() {
        RuntimeException causaOriginal = new RuntimeException("Connection timeout");

        try {
            ReflectionTestUtils.invokeMethod(authInternalApi, "fallbackGetUserById", 1L, causaOriginal);
        } catch (ServiceUnavailableException ex) {
            assertThat(ex.getCause()).isEqualTo(causaOriginal);
            assertThat(ex.getServiceName()).isEqualTo("auth-service");
        }
    }
}
