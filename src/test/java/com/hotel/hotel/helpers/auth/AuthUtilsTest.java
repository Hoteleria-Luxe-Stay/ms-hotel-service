package com.hotel.hotel.helpers.auth;

import com.hotel.hotel.infrastructure.security.AuthContextFilter;
import com.hotel.hotel.internal.dto.TokenValidationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUtilsTest {

    @Mock private NativeWebRequest nativeWebRequest;

    // ==================== getAuth ====================

    @Test
    void getAuthRetornaTokenValidationResponseCuandoAtributoPresente() {
        TokenValidationResponse validation = new TokenValidationResponse();
        validation.setValid(true);
        validation.setEmail("user@test.com");
        validation.setUserId(1L);
        validation.setRole("ADMIN");

        when(nativeWebRequest.getAttribute(AuthContextFilter.AUTH_CONTEXT_KEY, RequestAttributes.SCOPE_REQUEST))
                .thenReturn(validation);

        TokenValidationResponse result = AuthUtils.getAuth(nativeWebRequest);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("user@test.com");
        assertThat(result.getRole()).isEqualTo("ADMIN");
    }

    @Test
    void getAuthRetornaNullCuandoAtributoNoEsTokenValidationResponse() {
        when(nativeWebRequest.getAttribute(AuthContextFilter.AUTH_CONTEXT_KEY, RequestAttributes.SCOPE_REQUEST))
                .thenReturn("not-a-token-validation-response");

        TokenValidationResponse result = AuthUtils.getAuth(nativeWebRequest);

        assertThat(result).isNull();
    }

    @Test
    void getAuthRetornaNullCuandoAtributoEsNull() {
        when(nativeWebRequest.getAttribute(AuthContextFilter.AUTH_CONTEXT_KEY, RequestAttributes.SCOPE_REQUEST))
                .thenReturn(null);

        TokenValidationResponse result = AuthUtils.getAuth(nativeWebRequest);

        assertThat(result).isNull();
    }

    @Test
    void getAuthRetornaNullCuandoRequestEsNull() {
        TokenValidationResponse result = AuthUtils.getAuth(null);

        assertThat(result).isNull();
    }

    // ==================== isAdmin ====================

    @Test
    void isAdminRetornaTrueCuandoRoleEsAdmin() {
        TokenValidationResponse auth = new TokenValidationResponse();
        auth.setRole("ADMIN");

        assertThat(AuthUtils.isAdmin(auth)).isTrue();
    }

    @Test
    void isAdminRetornaTrueCuandoRoleEsAdminEnMinusculas() {
        TokenValidationResponse auth = new TokenValidationResponse();
        auth.setRole("admin");

        assertThat(AuthUtils.isAdmin(auth)).isTrue();
    }

    @Test
    void isAdminRetornaFalseCuandoRoleEsUser() {
        TokenValidationResponse auth = new TokenValidationResponse();
        auth.setRole("USER");

        assertThat(AuthUtils.isAdmin(auth)).isFalse();
    }

    @Test
    void isAdminRetornaFalseCuandoAuthEsNull() {
        assertThat(AuthUtils.isAdmin(null)).isFalse();
    }

    @Test
    void isAdminRetornaFalseCuandoRoleEsNull() {
        TokenValidationResponse auth = new TokenValidationResponse();
        auth.setRole(null);

        assertThat(AuthUtils.isAdmin(auth)).isFalse();
    }

    @Test
    void authUtilsConstructorLanzaUnsupportedOperationException() {
        assertThatThrownBy(() -> {
            var constructor = AuthUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        }).hasCauseInstanceOf(UnsupportedOperationException.class);
    }
}
