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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final DepartamentoService departamentoService;
    private final HabitacionService habitacionService;

    public HotelService(HotelRepository hotelRepository,
                        DepartamentoService departamentoService,
                        HabitacionService habitacionService) {
        this.hotelRepository = hotelRepository;
        this.departamentoService = departamentoService;
        this.habitacionService = habitacionService;
    }

    public List<Hotel> listarHoteles() {
        return hotelRepository.findAll();
    }

    public List<Hotel> listarPorDepartamentoId(Long departamentoId) {
        return hotelRepository.findByDepartamentoId(departamentoId);
    }

    public Hotel buscarPorId(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel", id));
    }

    @Transactional
    public Hotel guardar(HotelRequest hotelRequest) {
        validarHotelRequest(hotelRequest);

        Departamento departamento = departamentoService.buscarPorId(hotelRequest.getDepartamentoId());

        Hotel hotel = new Hotel();
        hotel.setNombre(hotelRequest.getNombre());
        hotel.setDireccion(hotelRequest.getDireccion());
        hotel.setDescripcion(hotelRequest.getDescripcion());
        hotel.setTelefono(hotelRequest.getTelefono());
        hotel.setEmail(hotelRequest.getEmail());
        hotel.setDepartamento(departamento);

        return hotelRepository.save(hotel);
    }

    @Transactional
    public Hotel actualizar(Long id, HotelRequest hotelRequest) {
        validarHotelRequest(hotelRequest);

        Hotel hotel = buscarPorId(id);
        Departamento departamento = departamentoService.buscarPorId(hotelRequest.getDepartamentoId());

        hotel.setNombre(hotelRequest.getNombre());
        hotel.setDireccion(hotelRequest.getDireccion());
        hotel.setDescripcion(hotelRequest.getDescripcion());
        hotel.setTelefono(hotelRequest.getTelefono());
        hotel.setEmail(hotelRequest.getEmail());
        hotel.setDepartamento(departamento);

        return hotelRepository.save(hotel);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new EntityNotFoundException("Hotel", id);
        }
        hotelRepository.deleteById(id);
    }

    public List<Habitacion> listarHabitacionesPorHotel(Long hotelId) {
        return habitacionService.buscarPorHotelId(hotelId);
    }

    private void validarHotelRequest(HotelRequest request) {
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new ValidationException("nombre", "El nombre del hotel es requerido");
        }
        if (request.getDepartamentoId() == null) {
            throw new ValidationException("departamentoId", "El departamento es requerido");
        }
    }
}
