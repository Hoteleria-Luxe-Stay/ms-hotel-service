package com.hotel.hotel.core.habitacion.service;

import com.hotel.hotel.api.dto.HabitacionRequest;
import com.hotel.hotel.core.habitacion.model.Habitacion;
import com.hotel.hotel.core.habitacion.repository.HabitacionRepository;
import com.hotel.hotel.core.hotel.model.Hotel;
import com.hotel.hotel.core.hotel.repository.HotelRepository;
import com.hotel.hotel.core.tipoHabitacion.model.TipoHabitacion;
import com.hotel.hotel.core.tipoHabitacion.service.TipoHabitacionService;
import com.hotel.hotel.helpers.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitacionServiceTest {

    @Mock private HabitacionRepository habitacionRepository;
    @Mock private HotelRepository hotelRepository;
    @Mock private TipoHabitacionService tipoHabitacionService;

    @InjectMocks
    private HabitacionService habitacionService;

    private Habitacion habitacion;
    private Hotel hotel;
    private TipoHabitacion tipoHabitacion;
    private HabitacionRequest habitacionRequest;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setNombre("Hotel Lima");

        tipoHabitacion = new TipoHabitacion();
        tipoHabitacion.setId(2L);
        tipoHabitacion.setNombre("Doble");
        tipoHabitacion.setCapacidad(2);

        habitacion = new Habitacion();
        habitacion.setId(10L);
        habitacion.setNumero("101");
        habitacion.setEstado("DISPONIBLE");
        habitacion.setPrecio(120.0);
        habitacion.setCapacidad(2);
        habitacion.setHotel(hotel);
        habitacion.setTipoHabitacion(tipoHabitacion);

        habitacionRequest = new HabitacionRequest();
        habitacionRequest.setNumero("101");
        habitacionRequest.setPrecio(120.0);
        habitacionRequest.setCapacidad(2);
        habitacionRequest.setHotelId(1L);
        habitacionRequest.setTipoHabitacionId(2L);
    }

    // ==================== buscarPorId ====================

    @Test
    void buscarPorIdRetornaHabitacionCuandoExiste() {
        when(habitacionRepository.findById(10L)).thenReturn(Optional.of(habitacion));

        Habitacion result = habitacionService.buscarPorId(10L);

        assertThat(result).isEqualTo(habitacion);
    }

    @Test
    void buscarPorIdLanzaEntityNotFoundExceptionCuandoNoExiste() {
        when(habitacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitacionService.buscarPorId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Habitacion");
    }

    // ==================== buscarPorHotelId ====================

    @Test
    void buscarPorHotelIdRetornaHabitacionesDelHotel() {
        when(habitacionRepository.findByHotelId(1L)).thenReturn(List.of(habitacion));

        List<Habitacion> result = habitacionService.buscarPorHotelId(1L);

        assertThat(result).hasSize(1);
        verify(habitacionRepository).findByHotelId(1L);
    }

    // ==================== crear ====================

    @Test
    void crearGuardaHabitacionConEstadoDisponible() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(tipoHabitacionService.buscarPorId(2L)).thenReturn(tipoHabitacion);
        when(habitacionRepository.save(any(Habitacion.class))).thenReturn(habitacion);

        Habitacion result = habitacionService.crear(habitacionRequest);

        assertThat(result).isNotNull();

        ArgumentCaptor<Habitacion> captor = ArgumentCaptor.forClass(Habitacion.class);
        verify(habitacionRepository).save(captor.capture());
        assertThat(captor.getValue().getEstado()).isEqualTo("DISPONIBLE");
        assertThat(captor.getValue().getNumero()).isEqualTo("101");
        assertThat(captor.getValue().getHotel()).isEqualTo(hotel);
        assertThat(captor.getValue().getTipoHabitacion()).isEqualTo(tipoHabitacion);
    }

    @Test
    void crearLanzaEntityNotFoundExceptionCuandoHotelNoExiste() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitacionService.crear(habitacionRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Hotel");

        verify(habitacionRepository, never()).save(any());
    }

    // ==================== actualizar ====================

    @Test
    void actualizarModificaHabitacionExistente() {
        when(habitacionRepository.findById(10L)).thenReturn(Optional.of(habitacion));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(tipoHabitacionService.buscarPorId(2L)).thenReturn(tipoHabitacion);
        when(habitacionRepository.save(any(Habitacion.class))).thenReturn(habitacion);

        habitacionRequest.setNumero("202");
        habitacionRequest.setPrecio(150.0);
        Habitacion result = habitacionService.actualizar(10L, habitacionRequest);

        assertThat(result).isNotNull();
        assertThat(habitacion.getNumero()).isEqualTo("202");
        assertThat(habitacion.getPrecio()).isEqualTo(150.0);
        verify(habitacionRepository).save(habitacion);
    }

    @Test
    void actualizarLanzaEntityNotFoundExceptionCuandoHabitacionNoExiste() {
        when(habitacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitacionService.actualizar(99L, habitacionRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Habitacion");

        verify(habitacionRepository, never()).save(any());
    }

    @Test
    void actualizarLanzaEntityNotFoundExceptionCuandoHotelNoExiste() {
        when(habitacionRepository.findById(10L)).thenReturn(Optional.of(habitacion));
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitacionService.actualizar(10L, habitacionRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Hotel");

        verify(habitacionRepository, never()).save(any());
    }

    // ==================== eliminar ====================

    @Test
    void eliminarEliminaHabitacionCuandoExiste() {
        when(habitacionRepository.findById(10L)).thenReturn(Optional.of(habitacion));

        habitacionService.eliminar(10L);

        verify(habitacionRepository).delete(habitacion);
    }

    @Test
    void eliminarLanzaEntityNotFoundExceptionCuandoNoExiste() {
        when(habitacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitacionService.eliminar(99L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(habitacionRepository, never()).delete(any());
    }

    // ==================== buscarDisponibles ====================

    @Test
    void buscarDisponiblesRetornaHabitacionesConEstadoDisponible() {
        when(habitacionRepository.findByHotelIdAndEstado(1L, "DISPONIBLE")).thenReturn(List.of(habitacion));

        List<Habitacion> result = habitacionService.buscarDisponibles(1L);

        assertThat(result).hasSize(1);
        verify(habitacionRepository).findByHotelIdAndEstado(1L, "DISPONIBLE");
    }

    @Test
    void buscarDisponiblesRetornaListaVaciaCuandoNoHayNinguna() {
        when(habitacionRepository.findByHotelIdAndEstado(1L, "DISPONIBLE")).thenReturn(List.of());

        List<Habitacion> result = habitacionService.buscarDisponibles(1L);

        assertThat(result).isEmpty();
    }

    // ==================== estaDisponible ====================

    @Test
    void estaDisponibleRetornaTrueCuandoEstadoEsDisponible() {
        habitacion.setEstado("DISPONIBLE");
        when(habitacionRepository.findById(10L)).thenReturn(Optional.of(habitacion));

        boolean result = habitacionService.estaDisponible(10L);

        assertThat(result).isTrue();
    }

    @Test
    void estaDisponibleRetornaFalseCuandoEstadoNoEsDisponible() {
        habitacion.setEstado("OCUPADO");
        when(habitacionRepository.findById(10L)).thenReturn(Optional.of(habitacion));

        boolean result = habitacionService.estaDisponible(10L);

        assertThat(result).isFalse();
    }

    // ==================== obtenerCantidadDisponible ====================

    @Test
    void obtenerCantidadDisponibleRetornaCantidadCorrecta() {
        when(habitacionRepository.countByHotelIdAndEstado(1L, "DISPONIBLE")).thenReturn(3L);

        int result = habitacionService.obtenerCantidadDisponible(1L);

        assertThat(result).isEqualTo(3);
        verify(habitacionRepository).countByHotelIdAndEstado(1L, "DISPONIBLE");
    }

    @Test
    void obtenerCantidadDisponibleRetornaCeroCuandoNoHay() {
        when(habitacionRepository.countByHotelIdAndEstado(1L, "DISPONIBLE")).thenReturn(0L);

        int result = habitacionService.obtenerCantidadDisponible(1L);

        assertThat(result).isEqualTo(0);
    }
}
