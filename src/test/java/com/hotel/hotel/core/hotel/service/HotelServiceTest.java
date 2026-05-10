package com.hotel.hotel.core.hotel.service;

import com.hotel.hotel.api.dto.HotelRequest;
import com.hotel.hotel.core.departamento.model.Departamento;
import com.hotel.hotel.core.departamento.service.DepartamentoService;
import com.hotel.hotel.core.habitacion.model.Habitacion;
import com.hotel.hotel.core.habitacion.service.HabitacionService;
import com.hotel.hotel.core.hotel.model.Hotel;
import com.hotel.hotel.core.hotel.repository.HotelRepository;
import com.hotel.hotel.helpers.exceptions.EntityNotFoundException;
import com.hotel.hotel.helpers.exceptions.ValidationException;
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
class HotelServiceTest {

    @Mock private HotelRepository hotelRepository;
    @Mock private DepartamentoService departamentoService;
    @Mock private HabitacionService habitacionService;

    @InjectMocks
    private HotelService hotelService;

    private Hotel hotel;
    private Departamento departamento;
    private HotelRequest hotelRequest;

    @BeforeEach
    void setUp() {
        departamento = new Departamento();
        departamento.setId(1L);
        departamento.setNombre("Lima");

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setNombre("Hotel Lima Centro");
        hotel.setDireccion("Av. Arequipa 1234");
        hotel.setDescripcion("Hotel moderno");
        hotel.setTelefono("01-4567890");
        hotel.setEmail("reservas@hotellima.com");
        hotel.setImagenUrl("http://img.example.com/hotel.jpg");
        hotel.setDepartamento(departamento);

        hotelRequest = new HotelRequest();
        hotelRequest.setNombre("Hotel Lima Centro");
        hotelRequest.setDireccion("Av. Arequipa 1234");
        hotelRequest.setDescripcion("Hotel moderno");
        hotelRequest.setTelefono("01-4567890");
        hotelRequest.setEmail("reservas@hotellima.com");
        hotelRequest.setImagenUrl("http://img.example.com/hotel.jpg");
        hotelRequest.setDepartamentoId(1L);
    }

    // ==================== listarHoteles ====================

    @Test
    void listarHotelesRetornaListaCompleta() {
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));

        List<Hotel> result = hotelService.listarHoteles();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Hotel Lima Centro");
    }

    @Test
    void listarHotelesRetornaListaVaciaCuandoNoHayHoteles() {
        when(hotelRepository.findAll()).thenReturn(List.of());

        List<Hotel> result = hotelService.listarHoteles();

        assertThat(result).isEmpty();
    }

    // ==================== listarPorDepartamentoId ====================

    @Test
    void listarPorDepartamentoIdRetornaHotelesFiltrados() {
        when(hotelRepository.findByDepartamentoId(1L)).thenReturn(List.of(hotel));

        List<Hotel> result = hotelService.listarPorDepartamentoId(1L);

        assertThat(result).hasSize(1);
        verify(hotelRepository).findByDepartamentoId(1L);
    }

    @Test
    void listarPorDepartamentoIdRetornaListaVaciaCuandoNinguno() {
        when(hotelRepository.findByDepartamentoId(99L)).thenReturn(List.of());

        List<Hotel> result = hotelService.listarPorDepartamentoId(99L);

        assertThat(result).isEmpty();
    }

    // ==================== buscarPorId ====================

    @Test
    void buscarPorIdRetornaHotelCuandoExiste() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        Hotel result = hotelService.buscarPorId(1L);

        assertThat(result).isEqualTo(hotel);
    }

    @Test
    void buscarPorIdLanzaEntityNotFoundExceptionCuandoNoExiste() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.buscarPorId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Hotel");
    }

    // ==================== guardar ====================

    @Test
    void guardarCreaNuevoHotelYLoRetorna() {
        when(departamentoService.buscarPorId(1L)).thenReturn(departamento);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        Hotel result = hotelService.guardar(hotelRequest);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Hotel Lima Centro");

        ArgumentCaptor<Hotel> captor = ArgumentCaptor.forClass(Hotel.class);
        verify(hotelRepository).save(captor.capture());
        assertThat(captor.getValue().getDepartamento()).isEqualTo(departamento);
    }

    @Test
    void guardarLanzaValidationExceptionCuandoNombreEsNull() {
        hotelRequest.setNombre(null);

        assertThatThrownBy(() -> hotelService.guardar(hotelRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("nombre");

        verify(hotelRepository, never()).save(any());
    }

    @Test
    void guardarLanzaValidationExceptionCuandoNombreEsBlank() {
        hotelRequest.setNombre("   ");

        assertThatThrownBy(() -> hotelService.guardar(hotelRequest))
                .isInstanceOf(ValidationException.class);

        verify(hotelRepository, never()).save(any());
    }

    @Test
    void guardarLanzaValidationExceptionCuandoDepartamentoIdEsNull() {
        hotelRequest.setDepartamentoId(null);

        assertThatThrownBy(() -> hotelService.guardar(hotelRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("departamento");

        verify(hotelRepository, never()).save(any());
    }

    // ==================== actualizar ====================

    @Test
    void actualizarModificaHotelExistenteYLoRetorna() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(departamentoService.buscarPorId(1L)).thenReturn(departamento);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        hotelRequest.setNombre("Hotel Lima Actualizado");
        Hotel result = hotelService.actualizar(1L, hotelRequest);

        assertThat(result).isNotNull();
        verify(hotelRepository).save(hotel);
        assertThat(hotel.getNombre()).isEqualTo("Hotel Lima Actualizado");
    }

    @Test
    void actualizarLanzaEntityNotFoundExceptionCuandoHotelNoExiste() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.actualizar(99L, hotelRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Hotel");

        verify(hotelRepository, never()).save(any());
    }

    @Test
    void actualizarLanzaValidationExceptionCuandoNombreEsInvalido() {
        hotelRequest.setNombre("");

        assertThatThrownBy(() -> hotelService.actualizar(1L, hotelRequest))
                .isInstanceOf(ValidationException.class);

        verify(hotelRepository, never()).save(any());
    }

    // ==================== eliminar ====================

    @Test
    void eliminarEliminaHotelCuandoExiste() {
        when(hotelRepository.existsById(1L)).thenReturn(true);

        hotelService.eliminar(1L);

        verify(hotelRepository).deleteById(1L);
    }

    @Test
    void eliminarLanzaEntityNotFoundExceptionCuandoNoExiste() {
        when(hotelRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> hotelService.eliminar(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Hotel");

        verify(hotelRepository, never()).deleteById(any());
    }

    // ==================== listarHabitacionesPorHotel ====================

    @Test
    void listarHabitacionesPorHotelDelegaAHabitacionService() {
        Habitacion habitacion = new Habitacion();
        habitacion.setId(10L);
        when(habitacionService.buscarPorHotelId(1L)).thenReturn(List.of(habitacion));

        List<Habitacion> result = hotelService.listarHabitacionesPorHotel(1L);

        assertThat(result).hasSize(1);
        verify(habitacionService).buscarPorHotelId(1L);
    }
}
