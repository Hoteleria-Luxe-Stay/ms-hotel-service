package com.hotel.hotel.helpers.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityNotFoundExceptionTest {

    @Test
    void constructorConEntityNameEIdentifierCreaExcepcionCorrectamente() {
        EntityNotFoundException ex = new EntityNotFoundException("Hotel", 42L);

        assertThat(ex.getEntityName()).isEqualTo("Hotel");
        assertThat(ex.getIdentifier()).isEqualTo(42L);
        assertThat(ex.getMessage()).contains("Hotel").contains("42");
    }

    @Test
    void constructorConSoloMensajeCreaExcepcionSinEntityNameNiIdentifier() {
        EntityNotFoundException ex = new EntityNotFoundException("Hotel no encontrado");

        assertThat(ex.getMessage()).isEqualTo("Hotel no encontrado");
        assertThat(ex.getEntityName()).isNull();
        assertThat(ex.getIdentifier()).isNull();
    }

    @Test
    void constructorConStringIdentifierFormataMensajeCorrectamente() {
        EntityNotFoundException ex = new EntityNotFoundException("Departamento", "Lima");

        assertThat(ex.getMessage()).contains("Departamento").contains("Lima");
        assertThat(ex.getEntityName()).isEqualTo("Departamento");
        assertThat(ex.getIdentifier()).isEqualTo("Lima");
    }

    @Test
    void esSubclaseDeRuntimeException() {
        EntityNotFoundException ex = new EntityNotFoundException("Hotel", 1L);

        assertThat(ex).isInstanceOf(RuntimeException.class);
    }
}
