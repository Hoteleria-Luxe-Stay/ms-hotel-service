package com.hotel.hotel.helpers.mappers;

import com.hotel.hotel.api.dto.HotelDetalleResponse;
import com.hotel.hotel.api.dto.HotelResponse;
import com.hotel.hotel.core.hotel.model.Hotel;

public class HotelMapper {

    private HotelMapper() {
        throw new UnsupportedOperationException("This class should never be instantiated");
    }

    public static HotelResponse toResponse(Hotel hotel) {
        if (hotel == null) {
            return null;
        }

        HotelResponse response = new HotelResponse();
        response.setId(hotel.getId());
        response.setNombre(hotel.getNombre());
        response.setDireccion(hotel.getDireccion());
        response.setDescripcion(hotel.getDescripcion());
        response.setTelefono(hotel.getTelefono());
        response.setEmail(hotel.getEmail());
        response.setImagenUrl(hotel.getImagenUrl());
        response.setDepartamento(DepartamentoMapper.toResponse(hotel.getDepartamento()));
        return response;
    }

    public static HotelDetalleResponse toDetalleResponse(Hotel hotel) {
        HotelDetalleResponse response = new HotelDetalleResponse();
        response.setId(hotel.getId());
        response.setNombre(hotel.getNombre());
        response.setDireccion(hotel.getDireccion());
        response.setDescripcion(hotel.getDescripcion());
        response.setTelefono(hotel.getTelefono());
        response.setEmail(hotel.getEmail());
        response.setImagenUrl(hotel.getImagenUrl());
        response.setDepartamento(DepartamentoMapper.toResponse(hotel.getDepartamento()));
        response.setHabitaciones(HabitacionMapper.toResponseList(hotel.getHabitaciones()));
        response.setPrecioMinimo(hotel.getPrecioMinimo());
        response.setPrecioMaximo(hotel.getPrecioMaximo());
        return response;
    }
}
