package com.hotel.hotel.core.tipoHabitacion.service;

import com.hotel.hotel.core.tipoHabitacion.model.TipoHabitacion;
import com.hotel.hotel.core.tipoHabitacion.repository.TipoHabitacionRepository;
import com.hotel.hotel.helpers.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoHabitacionService {

    private final TipoHabitacionRepository tipoHabitacionRepository;

    public TipoHabitacionService(TipoHabitacionRepository tipoHabitacionRepository) {
        this.tipoHabitacionRepository = tipoHabitacionRepository;
    }

    public List<TipoHabitacion> listar() {
        return tipoHabitacionRepository.findAll();
    }

    public TipoHabitacion buscarPorId(Long id) {
        return tipoHabitacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TipoHabitacion", id));
    }
}
