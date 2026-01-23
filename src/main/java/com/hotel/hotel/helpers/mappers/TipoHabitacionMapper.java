package com.hotel.hotel.helpers.mappers;

import com.hotel.hotel.api.dto.TipoHabitacionResponse;
import com.hotel.hotel.core.tipoHabitacion.model.TipoHabitacion;

import java.util.List;

public class TipoHabitacionMapper {

    private TipoHabitacionMapper() {
        throw new UnsupportedOperationException("This class should never be instantiated");
    }

    public static TipoHabitacionResponse toResponse(TipoHabitacion tipo) {
        if (tipo == null) {
            return null;
        }

        TipoHabitacionResponse response = new TipoHabitacionResponse();
        response.setId(tipo.getId());
        response.setNombre(tipo.getNombre());
        response.setDescripcion(tipo.getDescripcion());
        return response;
    }

    public static List<TipoHabitacionResponse> toResponseList(List<TipoHabitacion> tipos) {
        return tipos.stream()
                .map(TipoHabitacionMapper::toResponse)
                .toList();
    }
}
