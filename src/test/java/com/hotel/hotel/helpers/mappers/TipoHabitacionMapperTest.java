package com.hotel.hotel.helpers.mappers;

import com.hotel.hotel.api.dto.TipoHabitacionResponse;
import com.hotel.hotel.core.tipoHabitacion.model.TipoHabitacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TipoHabitacionMapperTest {

    private TipoHabitacion tipoSimple;
    private TipoHabitacion tipoSuite;

    @BeforeEach
    void setUp() {
        tipoSimple = new TipoHabitacion();
        tipoSimple.setId(1L);
        tipoSimple.setNombre("Simple");
        tipoSimple.setDescripcion("Habitacion individual con cama simple");

        tipoSuite = new TipoHabitacion();
        tipoSuite.setId(3L);
        tipoSuite.setNombre("Suite");
        tipoSuite.setDescripcion("Suite de lujo con sala de estar");
    }

    // ==================== toResponse ====================

    @Test
    void toResponseConvierteCorrecteamente() {
        TipoHabitacionResponse response = TipoHabitacionMapper.toResponse(tipoSimple);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNombre()).isEqualTo("Simple");
        assertThat(response.getDescripcion()).isEqualTo("Habitacion individual con cama simple");
    }

    @Test
    void toResponseRetornaNullCuandoTipoEsNull() {
        TipoHabitacionResponse response = TipoHabitacionMapper.toResponse(null);

        assertThat(response).isNull();
    }

    // ==================== toResponseList ====================

    @Test
    void toResponseListConvierteMultiplesTipos() {
        List<TipoHabitacionResponse> responses = TipoHabitacionMapper.toResponseList(List.of(tipoSimple, tipoSuite));

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getNombre()).isEqualTo("Simple");
        assertThat(responses.get(1).getNombre()).isEqualTo("Suite");
    }

    @Test
    void toResponseListConListaVaciaRetornaListaVacia() {
        List<TipoHabitacionResponse> responses = TipoHabitacionMapper.toResponseList(List.of());

        assertThat(responses).isEmpty();
    }

    @Test
    void tipoHabitacionMapperConstructorLanzaUnsupportedOperationException() {
        assertThatThrownBy(() -> {
            var constructor = TipoHabitacionMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        }).hasCauseInstanceOf(UnsupportedOperationException.class);
    }
}
