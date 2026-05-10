package com.hotel.hotel.core.hotel.service;

import com.hotel.hotel.core.habitacion.model.Habitacion;
import com.hotel.hotel.core.hotel.model.Hotel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HotelModelTest {

    @Test
    void getPrecioMinimoRetornaElMenorPrecioDeLasHabitaciones() {
        Hotel hotel = new Hotel();

        Habitacion h1 = new Habitacion();
        h1.setPrecio(80.0);

        Habitacion h2 = new Habitacion();
        h2.setPrecio(250.0);

        Habitacion h3 = new Habitacion();
        h3.setPrecio(120.0);

        hotel.setHabitaciones(List.of(h1, h2, h3));

        assertThat(hotel.getPrecioMinimo()).isEqualTo(80.0);
    }

    @Test
    void getPrecioMaximoRetornaElMayorPrecioDeLasHabitaciones() {
        Hotel hotel = new Hotel();

        Habitacion h1 = new Habitacion();
        h1.setPrecio(80.0);

        Habitacion h2 = new Habitacion();
        h2.setPrecio(250.0);

        Habitacion h3 = new Habitacion();
        h3.setPrecio(120.0);

        hotel.setHabitaciones(List.of(h1, h2, h3));

        assertThat(hotel.getPrecioMaximo()).isEqualTo(250.0);
    }

    @Test
    void getPrecioMinimoRetornaCeroCuandoListaEsVacia() {
        Hotel hotel = new Hotel();
        hotel.setHabitaciones(List.of());

        assertThat(hotel.getPrecioMinimo()).isEqualTo(0.0);
    }

    @Test
    void getPrecioMaximoRetornaCeroCuandoListaEsVacia() {
        Hotel hotel = new Hotel();
        hotel.setHabitaciones(List.of());

        assertThat(hotel.getPrecioMaximo()).isEqualTo(0.0);
    }

    @Test
    void getPrecioMinimoRetornaCeroCuandoHabitacionesEsNull() {
        Hotel hotel = new Hotel();
        hotel.setHabitaciones(null);

        assertThat(hotel.getPrecioMinimo()).isEqualTo(0.0);
    }

    @Test
    void getPrecioMaximoRetornaCeroCuandoHabitacionesEsNull() {
        Hotel hotel = new Hotel();
        hotel.setHabitaciones(null);

        assertThat(hotel.getPrecioMaximo()).isEqualTo(0.0);
    }

    @Test
    void hotelGettersYSettersFuncionanCorrectamente() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setNombre("Hotel Test");
        hotel.setDireccion("Av. Test 123");
        hotel.setDescripcion("Descripcion test");
        hotel.setTelefono("01-1234567");
        hotel.setEmail("test@hotel.com");
        hotel.setImagenUrl("http://img.example.com/test.jpg");

        assertThat(hotel.getId()).isEqualTo(1L);
        assertThat(hotel.getNombre()).isEqualTo("Hotel Test");
        assertThat(hotel.getDireccion()).isEqualTo("Av. Test 123");
        assertThat(hotel.getDescripcion()).isEqualTo("Descripcion test");
        assertThat(hotel.getTelefono()).isEqualTo("01-1234567");
        assertThat(hotel.getEmail()).isEqualTo("test@hotel.com");
        assertThat(hotel.getImagenUrl()).isEqualTo("http://img.example.com/test.jpg");
    }
}
