package com.hotel.hotel.helpers.mappers;

import com.hotel.hotel.api.dto.DepartamentoResponse;
import com.hotel.hotel.core.departamento.model.Departamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DepartamentoMapperTest {

    private Departamento lima;
    private Departamento cusco;

    @BeforeEach
    void setUp() {
        lima = new Departamento();
        lima.setId(1L);
        lima.setNombre("Lima");
        lima.setImagenUrl("http://img.example.com/lima.jpg");

        cusco = new Departamento();
        cusco.setId(2L);
        cusco.setNombre("Cusco");
        cusco.setImagenUrl("http://img.example.com/cusco.jpg");
    }

    // ==================== toResponse ====================

    @Test
    void toResponseConvierteCorrectamente() {
        DepartamentoResponse response = DepartamentoMapper.toResponse(lima);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNombre()).isEqualTo("Lima");
        assertThat(response.getImagenUrl()).isEqualTo("http://img.example.com/lima.jpg");
    }

    @Test
    void toResponseRetornaNullCuandoDepartamentoEsNull() {
        DepartamentoResponse response = DepartamentoMapper.toResponse(null);

        assertThat(response).isNull();
    }

    @Test
    void toResponseConDepartamentoSinImagenUrl() {
        lima.setImagenUrl(null);

        DepartamentoResponse response = DepartamentoMapper.toResponse(lima);

        assertThat(response).isNotNull();
        assertThat(response.getImagenUrl()).isNull();
    }

    // ==================== toResponseList ====================

    @Test
    void toResponseListConvierteMultiplesDepartamentos() {
        List<DepartamentoResponse> responses = DepartamentoMapper.toResponseList(List.of(lima, cusco));

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getNombre()).isEqualTo("Lima");
        assertThat(responses.get(1).getNombre()).isEqualTo("Cusco");
    }

    @Test
    void toResponseListConListaVaciaRetornaListaVacia() {
        List<DepartamentoResponse> responses = DepartamentoMapper.toResponseList(List.of());

        assertThat(responses).isEmpty();
    }

    @Test
    void departamentoMapperConstructorLanzaUnsupportedOperationException() {
        assertThatThrownBy(() -> {
            var constructor = DepartamentoMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        }).hasCauseInstanceOf(UnsupportedOperationException.class);
    }
}
