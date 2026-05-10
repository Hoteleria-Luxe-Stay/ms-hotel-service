package com.hotel.hotel.helpers;

import com.hotel.hotel.core.departamento.model.Departamento;
import com.hotel.hotel.core.departamento.repository.DepartamentoRepository;
import com.hotel.hotel.core.habitacion.model.Habitacion;
import com.hotel.hotel.core.habitacion.repository.HabitacionRepository;
import com.hotel.hotel.core.hotel.model.Hotel;
import com.hotel.hotel.core.hotel.repository.HotelRepository;
import com.hotel.hotel.core.tipoHabitacion.model.TipoHabitacion;
import com.hotel.hotel.core.tipoHabitacion.repository.TipoHabitacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataInitTest {

    @Mock private DepartamentoRepository departamentoRepository;
    @Mock private TipoHabitacionRepository tipoHabitacionRepository;
    @Mock private HotelRepository hotelRepository;
    @Mock private HabitacionRepository habitacionRepository;

    @InjectMocks
    private DataInit dataInit;

    // ==================== initTiposHabitacion ====================

    @Test
    void runCreaTiposHabitacionCuandoRepositoryEstaVacio() {
        when(tipoHabitacionRepository.count()).thenReturn(0L);
        when(departamentoRepository.count()).thenReturn(1L); // skip dep init
        when(hotelRepository.count()).thenReturn(1L);       // skip hotel init

        dataInit.run();

        // Deben crearse 4 tipos: Simple, Doble, Suite, Familiar
        verify(tipoHabitacionRepository, times(4)).save(any(TipoHabitacion.class));
    }

    @Test
    void runSkipsTiposHabitacionCuandoYaExisten() {
        when(tipoHabitacionRepository.count()).thenReturn(4L);
        when(departamentoRepository.count()).thenReturn(1L);
        when(hotelRepository.count()).thenReturn(1L);

        dataInit.run();

        verify(tipoHabitacionRepository, never()).save(any());
    }

    @Test
    void runCreaTiposConNombresCorrectosYCapacidad() {
        when(tipoHabitacionRepository.count()).thenReturn(0L);
        when(departamentoRepository.count()).thenReturn(1L);
        when(hotelRepository.count()).thenReturn(1L);

        dataInit.run();

        ArgumentCaptor<TipoHabitacion> captor = ArgumentCaptor.forClass(TipoHabitacion.class);
        verify(tipoHabitacionRepository, times(4)).save(captor.capture());

        assertThat(captor.getAllValues())
                .extracting(TipoHabitacion::getNombre)
                .containsExactlyInAnyOrder("Simple", "Doble", "Suite", "Familiar");
    }

    // ==================== initDepartamentos ====================

    @Test
    void runCreaDepartamentosCuandoRepositoryEstaVacio() {
        when(tipoHabitacionRepository.count()).thenReturn(4L);
        when(departamentoRepository.count()).thenReturn(0L);
        when(hotelRepository.count()).thenReturn(1L);

        dataInit.run();

        // Deben crearse 3 departamentos: Lima, Cusco, Arequipa
        verify(departamentoRepository, times(3)).save(any(Departamento.class));
    }

    @Test
    void runSkipsDepartamentosCuandoYaExisten() {
        when(tipoHabitacionRepository.count()).thenReturn(4L);
        when(departamentoRepository.count()).thenReturn(3L);
        when(hotelRepository.count()).thenReturn(1L);

        dataInit.run();

        verify(departamentoRepository, never()).save(any());
    }

    @Test
    void runCreaDepartamentosConNombresCorrectos() {
        when(tipoHabitacionRepository.count()).thenReturn(4L);
        when(departamentoRepository.count()).thenReturn(0L);
        when(hotelRepository.count()).thenReturn(1L);

        dataInit.run();

        ArgumentCaptor<Departamento> captor = ArgumentCaptor.forClass(Departamento.class);
        verify(departamentoRepository, times(3)).save(captor.capture());

        assertThat(captor.getAllValues())
                .extracting(Departamento::getNombre)
                .containsExactlyInAnyOrder("Lima", "Cusco", "Arequipa");
    }

    // ==================== initHotelesYHabitaciones ====================

    @Test
    void runSkipsHotelesYHabitacionesCuandoYaExisten() {
        when(tipoHabitacionRepository.count()).thenReturn(4L);
        when(departamentoRepository.count()).thenReturn(3L);
        when(hotelRepository.count()).thenReturn(3L);

        dataInit.run();

        verify(hotelRepository, never()).save(any());
        verify(habitacionRepository, never()).save(any());
    }

    @Test
    void runCreaHotelesYHabitacionesCuandoTodosLosDepsExisten() {
        when(tipoHabitacionRepository.count()).thenReturn(4L);
        when(departamentoRepository.count()).thenReturn(3L);
        when(hotelRepository.count()).thenReturn(0L);

        // Setup departamentos
        Departamento lima = buildDepartamento(1L, "Lima");
        Departamento cusco = buildDepartamento(2L, "Cusco");
        Departamento arequipa = buildDepartamento(3L, "Arequipa");

        when(departamentoRepository.findByNombre("Lima")).thenReturn(Optional.of(lima));
        when(departamentoRepository.findByNombre("Cusco")).thenReturn(Optional.of(cusco));
        when(departamentoRepository.findByNombre("Arequipa")).thenReturn(Optional.of(arequipa));

        // Setup tipos
        TipoHabitacion simple = buildTipo(1L, "Simple", 1);
        TipoHabitacion doble = buildTipo(2L, "Doble", 2);
        TipoHabitacion suite = buildTipo(3L, "Suite", 4);

        when(tipoHabitacionRepository.findByNombre("Simple")).thenReturn(Optional.of(simple));
        when(tipoHabitacionRepository.findByNombre("Doble")).thenReturn(Optional.of(doble));
        when(tipoHabitacionRepository.findByNombre("Suite")).thenReturn(Optional.of(suite));

        // Mock hotel saves to return hotel with a set ID so habitaciones can reference them
        when(hotelRepository.save(any(Hotel.class))).thenAnswer(inv -> inv.getArgument(0));
        when(habitacionRepository.save(any(Habitacion.class))).thenAnswer(inv -> inv.getArgument(0));

        dataInit.run();

        // 3 hoteles: Lima, Cusco, Arequipa
        verify(hotelRepository, times(3)).save(any(Hotel.class));

        // Lima: 5 habitaciones, Cusco: 4 habitaciones, Arequipa: 4 habitaciones = 13 total
        verify(habitacionRepository, times(13)).save(any(Habitacion.class));
    }

    @Test
    void runSkipsHotelesIndividualmenteCuandoDepartamentoNoExiste() {
        when(tipoHabitacionRepository.count()).thenReturn(4L);
        when(departamentoRepository.count()).thenReturn(3L);
        when(hotelRepository.count()).thenReturn(0L);

        // Solo Lima existe, Cusco y Arequipa no
        Departamento lima = buildDepartamento(1L, "Lima");
        when(departamentoRepository.findByNombre("Lima")).thenReturn(Optional.of(lima));
        when(departamentoRepository.findByNombre("Cusco")).thenReturn(Optional.empty());
        when(departamentoRepository.findByNombre("Arequipa")).thenReturn(Optional.empty());

        TipoHabitacion simple = buildTipo(1L, "Simple", 1);
        TipoHabitacion doble = buildTipo(2L, "Doble", 2);
        TipoHabitacion suite = buildTipo(3L, "Suite", 4);

        when(tipoHabitacionRepository.findByNombre("Simple")).thenReturn(Optional.of(simple));
        when(tipoHabitacionRepository.findByNombre("Doble")).thenReturn(Optional.of(doble));
        when(tipoHabitacionRepository.findByNombre("Suite")).thenReturn(Optional.of(suite));

        when(hotelRepository.save(any(Hotel.class))).thenAnswer(inv -> inv.getArgument(0));
        when(habitacionRepository.save(any(Habitacion.class))).thenAnswer(inv -> inv.getArgument(0));

        dataInit.run();

        // Solo se crea el hotel de Lima
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void runSkipsHotelesIndividualmenteCuandoTipoHabitacionNoExiste() {
        when(tipoHabitacionRepository.count()).thenReturn(4L);
        when(departamentoRepository.count()).thenReturn(3L);
        when(hotelRepository.count()).thenReturn(0L);

        Departamento lima = buildDepartamento(1L, "Lima");
        when(departamentoRepository.findByNombre("Lima")).thenReturn(Optional.of(lima));
        when(departamentoRepository.findByNombre("Cusco")).thenReturn(Optional.empty());
        when(departamentoRepository.findByNombre("Arequipa")).thenReturn(Optional.empty());

        // Sin tipos de habitacion disponibles
        when(tipoHabitacionRepository.findByNombre("Simple")).thenReturn(Optional.empty());
        when(tipoHabitacionRepository.findByNombre("Doble")).thenReturn(Optional.empty());
        when(tipoHabitacionRepository.findByNombre("Suite")).thenReturn(Optional.empty());

        dataInit.run();

        // No debe crear ningun hotel si los tipos no estan
        verify(hotelRepository, never()).save(any(Hotel.class));
        verify(habitacionRepository, never()).save(any(Habitacion.class));
    }

    // ==================== helpers ====================

    private Departamento buildDepartamento(Long id, String nombre) {
        Departamento dep = new Departamento();
        dep.setId(id);
        dep.setNombre(nombre);
        return dep;
    }

    private TipoHabitacion buildTipo(Long id, String nombre, int capacidad) {
        TipoHabitacion tipo = new TipoHabitacion();
        tipo.setId(id);
        tipo.setNombre(nombre);
        tipo.setCapacidad(capacidad);
        return tipo;
    }
}
