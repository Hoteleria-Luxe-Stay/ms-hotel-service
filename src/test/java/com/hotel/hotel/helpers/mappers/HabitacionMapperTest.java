package com.hotel.hotel.helpers.mappers;

import com.hotel.hotel.api.dto.HabitacionResponse;
import com.hotel.hotel.core.habitacion.model.Habitacion;
import com.hotel.hotel.core.hotel.model.Hotel;
import com.hotel.hotel.core.tipoHabitacion.model.TipoHabitacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HabitacionMapperTest {

    private Habitacion habitacion;
    private Hotel hotel;
    private TipoHabitacion tipoHabitacion;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(5L);
        hotel.setNombre("Hotel Test");

        tipoHabitacion = new TipoHabitacion();
        tipoHabitacion.setId(2L);
        tipoHabitacion.setNombre("Doble");
        tipoHabitacion.setDescripcion("Habitacion doble");

        habitacion = new Habitacion();
        habitacion.setId(10L);
        habitacion.setNumero("101");
        habitacion.setEstado("DISPONIBLE");
        habitacion.setPrecio(120.0);
        habitacion.setCapacidad(2);
        habitacion.setHotel(hotel);
        habitacion.setTipoHabitacion(tipoHabitacion);
    }

    // ==================== toResponse ====================

    @Test
    void toResponseConvierteHabitacionCorrectamente() {
        HabitacionResponse response = HabitacionMapper.toResponse(habitacion);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getNumero()).isEqualTo("101");
        assertThat(response.getEstado()).isEqualTo("DISPONIBLE");
        assertThat(response.getPrecio()).isEqualTo(120.0);
        assertThat(response.getCapacidad()).isEqualTo(2);
        assertThat(response.getHotelId()).isEqualTo(5L);
        assertThat(response.getTipoHabitacion()).isNotNull();
        assertThat(response.getTipoHabitacion().getNombre()).isEqualTo("Doble");
    }

    @Test
    void toResponseRetornaNullCuandoHabitacionEsNull() {
        HabitacionResponse response = HabitacionMapper.toResponse(null);

        assertThat(response).isNull();
    }

    @Test
    void toResponseConHabitacionSinHotel() {
        habitacion.setHotel(null);

        HabitacionResponse response = HabitacionMapper.toResponse(habitacion);

        assertThat(response).isNotNull();
        assertThat(response.getHotelId()).isNull();
    }

    @Test
    void toResponseConHabitacionSinTipoHabitacion() {
        habitacion.setTipoHabitacion(null);

        HabitacionResponse response = HabitacionMapper.toResponse(habitacion);

        assertThat(response).isNotNull();
        assertThat(response.getTipoHabitacion()).isNull();
    }

    // ==================== toResponseList ====================

    @Test
    void toResponseListConvierteLista() {
        Habitacion hab2 = new Habitacion();
        hab2.setId(20L);
        hab2.setNumero("201");
        hab2.setEstado("OCUPADO");
        hab2.setPrecio(150.0);
        hab2.setCapacidad(2);
        hab2.setHotel(hotel);
        hab2.setTipoHabitacion(tipoHabitacion);

        List<HabitacionResponse> responses = HabitacionMapper.toResponseList(List.of(habitacion, hab2));

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(10L);
        assertThat(responses.get(1).getId()).isEqualTo(20L);
    }

    @Test
    void toResponseListConListaVaciaRetornaListaVacia() {
        List<HabitacionResponse> responses = HabitacionMapper.toResponseList(List.of());

        assertThat(responses).isEmpty();
    }

    @Test
    void habitacionMapperConstructorLanzaUnsupportedOperationException() {
        assertThatThrownBy(() -> {
            var constructor = HabitacionMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        }).hasCauseInstanceOf(UnsupportedOperationException.class);
    }
}
