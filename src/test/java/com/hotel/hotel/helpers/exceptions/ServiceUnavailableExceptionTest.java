package com.hotel.hotel.helpers.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceUnavailableExceptionTest {

    @Test
    void constructorConServiceNameCreaExcepcionCorrectamente() {
        ServiceUnavailableException ex = new ServiceUnavailableException("auth-service");

        assertThat(ex.getServiceName()).isEqualTo("auth-service");
        assertThat(ex.getMessage()).contains("auth-service");
        assertThat(ex.getCause()).isNull();
    }

    @Test
    void constructorConServiceNameYCauseCreaExcepcionConCausa() {
        Throwable causa = new RuntimeException("Connection refused");
        ServiceUnavailableException ex = new ServiceUnavailableException("auth-service", causa);

        assertThat(ex.getServiceName()).isEqualTo("auth-service");
        assertThat(ex.getMessage()).contains("auth-service");
        assertThat(ex.getCause()).isEqualTo(causa);
    }

    @Test
    void esSubclaseDeRuntimeException() {
        ServiceUnavailableException ex = new ServiceUnavailableException("test-service");

        assertThat(ex).isInstanceOf(RuntimeException.class);
    }
}
