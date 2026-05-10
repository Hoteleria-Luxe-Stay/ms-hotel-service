package com.hotel.hotel.helpers.mappers;

import com.hotel.hotel.api.dto.HotelDetalleResponse;
import com.hotel.hotel.api.dto.HotelResponse;
import com.hotel.hotel.core.departamento.model.Departamento;
import com.hotel.hotel.core.habitacion.model.Habitacion;
import com.hotel.hotel.core.hotel.model.Hotel;
import com.hotel.hotel.core.tipoHabitacion.model.TipoHabitacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HotelMapperTest {

    private Hotel hotel;
    private Departamento departamento;

    @BeforeEach
    void setUp() {
        departamento = new Departamento();
        departamento.setId(1L);
        departamento.setNombre("Lima");
        departamento.setImagenUrl("http://img.example.com/lima.jpg");

        hotel = new Hotel();
        hotel.setId(10L);
        hotel.setNombre("Hotel Lima Centro");
        hotel.setDireccion("Av. Arequipa 1234");
        hotel.setDescripcion("Hotel moderno");
        hotel.setTelefono("01-4567890");
        hotel.setEmail("reservas@hotellima.com");
        hotel.setImagenUrl("http://img.example.com/hotel.jpg");
        hotel.setDepartamento(departamento);
    }

    // ==================== toResponse ====================

    @Test
    void toResponseConvierteHotelCorrectamente() {
        HotelResponse response = HotelMapper.toResponse(hotel);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getNombre()).isEqualTo("Hotel Lima Centro");
        assertThat(response.getDireccion()).isEqualTo("Av. Arequipa 1234");
        assertThat(response.getDescripcion()).isEqualTo("Hotel moderno");
        assertThat(response.getTelefono()).isEqualTo("01-4567890");
        assertThat(response.getEmail()).isEqualTo("reservas@hotellima.com");
        assertThat(response.getImagenUrl()).isEqualTo("http://img.example.com/hotel.jpg");
        assertThat(response.getDepartamento()).isNotNull();
        assertThat(response.getDepartamento().getNombre()).isEqualTo("Lima");
    }

    @Test
    void toResponseRetornaNullCuandoHotelEsNull() {
        HotelResponse response = HotelMapper.toResponse(null);

        assertThat(response).isNull();
    }

    @Test
    void toResponseConHotelSinDepartamento() {
        hotel.setDepartamento(null);

        HotelResponse response = HotelMapper.toResponse(hotel);

        assertThat(response).isNotNull();
        assertThat(response.getDepartamento()).isNull();
    }

    // ==================== toDetalleResponse ====================

    @Test
    void toDetalleResponseConvierteHotelConHabitaciones() {
        TipoHabitacion tipo = new TipoHabitacion();
        tipo.setId(1L);
        tipo.setNombre("Simple");

        Habitacion hab1 = new Habitacion();
        hab1.setId(1L);
        hab1.setNumero("101");
        hab1.setEstado("DISPONIBLE");
        hab1.setPrecio(80.0);
        hab1.setCapacidad(1);
        hab1.setHotel(hotel);
        hab1.setTipoHabitacion(tipo);

        Habitacion hab2 = new Habitacion();
        hab2.setId(2L);
        hab2.setNumero("201");
        hab2.setEstado("DISPONIBLE");
        hab2.setPrecio(200.0);
        hab2.setCapacidad(2);
        hab2.setHotel(hotel);
        hab2.setTipoHabitacion(tipo);

        hotel.setHabitaciones(List.of(hab1, hab2));

        HotelDetalleResponse response = HotelMapper.toDetalleResponse(hotel);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getNombre()).isEqualTo("Hotel Lima Centro");
        assertThat(response.getHabitaciones()).hasSize(2);
        assertThat(response.getPrecioMinimo()).isEqualTo(80.0);
        assertThat(response.getPrecioMaximo()).isEqualTo(200.0);
    }

    @Test
    void toDetalleResponseConHotelSinHabitaciones() {
        hotel.setHabitaciones(new ArrayList<>());

        HotelDetalleResponse response = HotelMapper.toDetalleResponse(hotel);

        assertThat(response).isNotNull();
        assertThat(response.getHabitaciones()).isEmpty();
        assertThat(response.getPrecioMinimo()).isEqualTo(0.0);
        assertThat(response.getPrecioMaximo()).isEqualTo(0.0);
    }

    @Test
    void hotelMapperConstructorLanzaUnsupportedOperationException() {
        assertThatThrownBy(() -> {
            var constructor = HotelMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        }).hasCauseInstanceOf(UnsupportedOperationException.class);
    }
}
