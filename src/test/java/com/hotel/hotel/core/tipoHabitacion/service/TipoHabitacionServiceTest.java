package com.hotel.hotel.core.tipoHabitacion.service;

import com.hotel.hotel.core.tipoHabitacion.model.TipoHabitacion;
import com.hotel.hotel.core.tipoHabitacion.repository.TipoHabitacionRepository;
import com.hotel.hotel.helpers.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoHabitacionServiceTest {

    @Mock private TipoHabitacionRepository tipoHabitacionRepository;

    @InjectMocks
    private TipoHabitacionService tipoHabitacionService;

    private TipoHabitacion tipoSimple;
    private TipoHabitacion tipoDoble;

    @BeforeEach
    void setUp() {
        tipoSimple = new TipoHabitacion();
        tipoSimple.setId(1L);
        tipoSimple.setNombre("Simple");
        tipoSimple.setDescripcion("Habitacion individual con cama simple");
        tipoSimple.setCapacidad(1);

        tipoDoble = new TipoHabitacion();
        tipoDoble.setId(2L);
        tipoDoble.setNombre("Doble");
        tipoDoble.setDescripcion("Habitacion con cama doble para 2 personas");
        tipoDoble.setCapacidad(2);
    }

    // ==================== listar ====================

    @Test
    void listarRetornaTodosLosTipos() {
        when(tipoHabitacionRepository.findAll()).thenReturn(List.of(tipoSimple, tipoDoble));

        List<TipoHabitacion> result = tipoHabitacionService.listar();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(TipoHabitacion::getNombre)
                .containsExactly("Simple", "Doble");
    }

    @Test
    void listarRetornaListaVaciaCuandoNoHayTipos() {
        when(tipoHabitacionRepository.findAll()).thenReturn(List.of());

        List<TipoHabitacion> result = tipoHabitacionService.listar();

        assertThat(result).isEmpty();
    }

    // ==================== buscarPorId ====================

    @Test
    void buscarPorIdRetornaTipoCuandoExiste() {
        when(tipoHabitacionRepository.findById(1L)).thenReturn(Optional.of(tipoSimple));

        TipoHabitacion result = tipoHabitacionService.buscarPorId(1L);

        assertThat(result).isEqualTo(tipoSimple);
        assertThat(result.getNombre()).isEqualTo("Simple");
        assertThat(result.getCapacidad()).isEqualTo(1);
    }

    @Test
    void buscarPorIdLanzaEntityNotFoundExceptionCuandoNoExiste() {
        when(tipoHabitacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tipoHabitacionService.buscarPorId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("TipoHabitacion");
    }
}
