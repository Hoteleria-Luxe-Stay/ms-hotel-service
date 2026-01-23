package com.hotel.hotel.helpers.mappers;

import com.hotel.hotel.api.dto.DepartamentoResponse;
import com.hotel.hotel.core.departamento.model.Departamento;

import java.util.List;

public class DepartamentoMapper {

    private DepartamentoMapper() {
        throw new UnsupportedOperationException("This class should never be instantiated");
    }

    public static DepartamentoResponse toResponse(Departamento departamento) {
        if (departamento == null) {
            return null;
        }

        DepartamentoResponse response = new DepartamentoResponse();
        response.setId(departamento.getId());
        response.setNombre(departamento.getNombre());
        return response;
    }

    public static List<DepartamentoResponse> toResponseList(List<Departamento> departamentos) {
        return departamentos.stream()
                .map(DepartamentoMapper::toResponse)
                .toList();
    }
}
