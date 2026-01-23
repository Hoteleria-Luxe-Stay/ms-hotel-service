package com.hotel.hotel.core.habitacion.service;

import com.hotel.hotel.api.dto.HabitacionRequest;
import com.hotel.hotel.core.habitacion.model.Habitacion;
import com.hotel.hotel.core.habitacion.repository.HabitacionRepository;
import com.hotel.hotel.core.hotel.model.Hotel;
import com.hotel.hotel.core.hotel.repository.HotelRepository;
import com.hotel.hotel.core.tipoHabitacion.model.TipoHabitacion;
import com.hotel.hotel.core.tipoHabitacion.service.TipoHabitacionService;
import com.hotel.hotel.helpers.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HabitacionService {

    private static final String ESTADO_DISPONIBLE = "DISPONIBLE";

    private final HabitacionRepository habitacionRepository;
    private final HotelRepository hotelRepository;
    private final TipoHabitacionService tipoHabitacionService;

    public HabitacionService(HabitacionRepository habitacionRepository,
                             HotelRepository hotelRepository,
                             TipoHabitacionService tipoHabitacionService) {
        this.habitacionRepository = habitacionRepository;
        this.hotelRepository = hotelRepository;
        this.tipoHabitacionService = tipoHabitacionService;
    }

    public Habitacion buscarPorId(Long id) {
        return habitacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Habitacion", id));
    }

    public List<Habitacion> buscarPorHotelId(Long hotelId) {
        return habitacionRepository.findByHotelId(hotelId);
    }

    public Habitacion crear(HabitacionRequest request) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel", request.getHotelId()));
        TipoHabitacion tipoHabitacion = tipoHabitacionService.buscarPorId(request.getTipoHabitacionId());

        Habitacion habitacion = new Habitacion();
        habitacion.setNumero(request.getNumero());
        habitacion.setPrecio(request.getPrecio());
        habitacion.setCapacidad(request.getCapacidad());
        habitacion.setEstado(ESTADO_DISPONIBLE);
        habitacion.setHotel(hotel);
        habitacion.setTipoHabitacion(tipoHabitacion);

        return habitacionRepository.save(habitacion);
    }

    public List<Habitacion> buscarDisponibles(Long hotelId, LocalDate inicio, LocalDate fin) {
        return habitacionRepository.findByHotelIdAndEstado(hotelId, ESTADO_DISPONIBLE);
    }

    public boolean estaDisponible(Long habitacionId, LocalDate inicio, LocalDate fin) {
        Habitacion habitacion = buscarPorId(habitacionId);
        return ESTADO_DISPONIBLE.equals(habitacion.getEstado());
    }

    public int obtenerCantidadDisponible(Long hotelId, LocalDate inicio, LocalDate fin) {
        return Math.toIntExact(habitacionRepository.countByHotelIdAndEstado(hotelId, ESTADO_DISPONIBLE));
    }
}
