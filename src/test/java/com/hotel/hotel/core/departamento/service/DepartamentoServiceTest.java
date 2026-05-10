package com.hotel.hotel.core.departamento.service;

import com.hotel.hotel.api.dto.DepartamentoRequest;
import com.hotel.hotel.core.departamento.model.Departamento;
import com.hotel.hotel.core.departamento.repository.DepartamentoRepository;
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
class DepartamentoServiceTest {

    @Mock private DepartamentoRepository departamentoRepository;

    @InjectMocks
    private DepartamentoService departamentoService;

    private Departamento departamento;
    private DepartamentoRequest departamentoRequest;

    @BeforeEach
    void setUp() {
        departamento = new Departamento();
        departamento.setId(1L);
        departamento.setNombre("Lima");
        departamento.setImagenUrl("http://img.example.com/lima.jpg");

        departamentoRequest = new DepartamentoRequest();
        departamentoRequest.setNombre("Lima");
        departamentoRequest.setImagenUrl("http://img.example.com/lima.jpg");
    }

    // ==================== listar ====================

    @Test
    void listarRetornaListaCompleta() {
        when(departamentoRepository.findAll()).thenReturn(List.of(departamento));

        List<Departamento> result = departamentoService.listar();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Lima");
    }

    @Test
    void listarRetornaListaVaciaCuandoNoDatos() {
        when(departamentoRepository.findAll()).thenReturn(List.of());

        List<Departamento> result = departamentoService.listar();

        assertThat(result).isEmpty();
    }

    // ==================== buscarPorId ====================

    @Test
    void buscarPorIdRetornaDepartamentoCuandoExiste() {
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));

        Departamento result = departamentoService.buscarPorId(1L);

        assertThat(result).isEqualTo(departamento);
        assertThat(result.getNombre()).isEqualTo("Lima");
    }

    @Test
    void buscarPorIdLanzaEntityNotFoundExceptionCuandoNoExiste() {
        when(departamentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departamentoService.buscarPorId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Departamento");
    }

    // ==================== buscarPorNombre ====================

    @Test
    void buscarPorNombreRetornaOptionalConDepartamentoCuandoExiste() {
        when(departamentoRepository.findByNombre("Lima")).thenReturn(Optional.of(departamento));

        Optional<Departamento> result = departamentoService.buscarPorNombre("Lima");

        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Lima");
    }

    @Test
    void buscarPorNombreRetornaOptionalVacioCuandoNoExiste() {
        when(departamentoRepository.findByNombre("Inexistente")).thenReturn(Optional.empty());

        Optional<Departamento> result = departamentoService.buscarPorNombre("Inexistente");

        assertThat(result).isEmpty();
    }

    // ==================== crear ====================

    @Test
    void crearGuardaYRetornaNuevoDepartamento() {
        when(departamentoRepository.save(any(Departamento.class))).thenReturn(departamento);

        Departamento result = departamentoService.crear(departamentoRequest);

        assertThat(result).isNotNull();

        ArgumentCaptor<Departamento> captor = ArgumentCaptor.forClass(Departamento.class);
        verify(departamentoRepository).save(captor.capture());
        assertThat(captor.getValue().getNombre()).isEqualTo("Lima");
        assertThat(captor.getValue().getImagenUrl()).isEqualTo("http://img.example.com/lima.jpg");
    }

    // ==================== actualizar ====================

    @Test
    void actualizarModificaDepartamentoExistente() {
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
        when(departamentoRepository.save(any(Departamento.class))).thenReturn(departamento);

        departamentoRequest.setNombre("Lima Actualizado");
        departamentoRequest.setImagenUrl("http://img.example.com/lima-new.jpg");
        Departamento result = departamentoService.actualizar(1L, departamentoRequest);

        assertThat(result).isNotNull();
        assertThat(departamento.getNombre()).isEqualTo("Lima Actualizado");
        assertThat(departamento.getImagenUrl()).isEqualTo("http://img.example.com/lima-new.jpg");
        verify(departamentoRepository).save(departamento);
    }

    @Test
    void actualizarLanzaEntityNotFoundExceptionCuandoNoExiste() {
        when(departamentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departamentoService.actualizar(99L, departamentoRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Departamento");

        verify(departamentoRepository, never()).save(any());
    }

    // ==================== eliminar ====================

    @Test
    void eliminarEliminaDepartamentoCuandoExiste() {
        when(departamentoRepository.existsById(1L)).thenReturn(true);

        departamentoService.eliminar(1L);

        verify(departamentoRepository).deleteById(1L);
    }

    @Test
    void eliminarLanzaEntityNotFoundExceptionCuandoNoExiste() {
        when(departamentoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> departamentoService.eliminar(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Departamento");

        verify(departamentoRepository, never()).deleteById(any());
    }
}
