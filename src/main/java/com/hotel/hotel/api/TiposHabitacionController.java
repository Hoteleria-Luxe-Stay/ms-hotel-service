package com.hotel.hotel.api;

import com.hotel.hotel.api.dto.TipoHabitacionResponse;
import com.hotel.hotel.core.tipoHabitacion.model.TipoHabitacion;
import com.hotel.hotel.core.tipoHabitacion.service.TipoHabitacionService;
import com.hotel.hotel.helpers.mappers.TipoHabitacionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TiposHabitacionController implements TiposHabitacionApi {

    private final TipoHabitacionService tipoHabitacionService;

    public TiposHabitacionController(TipoHabitacionService tipoHabitacionService) {
        this.tipoHabitacionService = tipoHabitacionService;
    }

    @Override
    public ResponseEntity<List<TipoHabitacionResponse>> listarTiposHabitacion() {
        List<TipoHabitacion> tipos = tipoHabitacionService.listar();
        return ResponseEntity.ok(TipoHabitacionMapper.toResponseList(tipos));
    }
}
