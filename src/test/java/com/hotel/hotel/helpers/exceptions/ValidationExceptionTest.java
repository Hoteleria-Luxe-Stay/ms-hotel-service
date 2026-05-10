package com.hotel.hotel.helpers.exceptions;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationExceptionTest {

    @Test
    void constructorConSoloMensajeCreaExcepcion() {
        ValidationException ex = new ValidationException("Datos invalidos");

        assertThat(ex.getMessage()).isEqualTo("Datos invalidos");
        assertThat(ex.getFieldErrors()).isNull();
    }

    @Test
    void constructorConFieldYMensajeCreaExcepcionConFieldErrors() {
        ValidationException ex = new ValidationException("nombre", "El nombre es requerido");

        assertThat(ex.getMessage()).isEqualTo("El nombre es requerido");
        assertThat(ex.getFieldErrors()).containsEntry("nombre", "El nombre es requerido");
    }

    @Test
    void constructorConMensajeYMapaFieldErrorsCreaExcepcionCompleta() {
        Map<String, String> errors = Map.of(
                "nombre", "El nombre es requerido",
                "email", "El email es invalido"
        );
        ValidationException ex = new ValidationException("Multiples errores", errors);

        assertThat(ex.getMessage()).isEqualTo("Multiples errores");
        assertThat(ex.getFieldErrors()).hasSize(2);
        assertThat(ex.getFieldErrors()).containsEntry("nombre", "El nombre es requerido");
        assertThat(ex.getFieldErrors()).containsEntry("email", "El email es invalido");
    }

    @Test
    void esSubclaseDeRuntimeException() {
        ValidationException ex = new ValidationException("mensaje");

        assertThat(ex).isInstanceOf(RuntimeException.class);
    }
}
