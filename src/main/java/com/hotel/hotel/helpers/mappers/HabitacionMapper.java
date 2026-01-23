package com.hotel.hotel.helpers.mappers;

import com.hotel.hotel.api.dto.HabitacionResponse;
import com.hotel.hotel.core.habitacion.model.Habitacion;

import java.util.List;

public class HabitacionMapper {

    private HabitacionMapper() {
        throw new UnsupportedOperationException("This class should never be instantiated");
    }

    public static HabitacionResponse toResponse(Habitacion habitacion) {
        if (habitacion == null) {
            return null;
        }

        HabitacionResponse response = new HabitacionResponse();
        response.setId(habitacion.getId());
        response.setNumero(habitacion.getNumero());
        response.setPrecio(habitacion.getPrecio());
        response.setCapacidad(habitacion.getCapacidad());
        response.setTipoHabitacion(TipoHabitacionMapper.toResponse(habitacion.getTipoHabitacion()));
        if (habitacion.getHotel() != null) {
            response.setHotelId(habitacion.getHotel().getId());
        }
        return response;
    }

    public static List<HabitacionResponse> toResponseList(List<Habitacion> habitaciones) {
        return habitaciones.stream()
                .map(HabitacionMapper::toResponse)
                .toList();
    }
}
