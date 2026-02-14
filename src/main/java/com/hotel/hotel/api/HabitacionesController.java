package com.hotel.hotel.api;

import com.hotel.hotel.api.dto.DisponibilidadResponse;
import com.hotel.hotel.api.dto.HabitacionRequest;
import com.hotel.hotel.api.dto.HabitacionResponse;
import com.hotel.hotel.api.dto.HabitacionesDisponiblesResponse;
import com.hotel.hotel.api.dto.MessageResponse;
import com.hotel.hotel.core.habitacion.model.Habitacion;
import com.hotel.hotel.core.habitacion.service.HabitacionService;
import com.hotel.hotel.helpers.auth.AuthUtils;
import com.hotel.hotel.helpers.mappers.HabitacionMapper;
import com.hotel.hotel.internal.dto.TokenValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

@RestController
public class HabitacionesController implements HabitacionesApi {

    private final HabitacionService habitacionService;
    private final NativeWebRequest request;

    public HabitacionesController(HabitacionService habitacionService,
                                  NativeWebRequest request) {
        this.habitacionService = habitacionService;
        this.request = request;
    }

    @Override
    public ResponseEntity<HabitacionesDisponiblesResponse> listarHabitacionesDisponibles(Long hotelId) {

        List<Habitacion> habitaciones = habitacionService.buscarDisponibles(hotelId);

        HabitacionesDisponiblesResponse response = new HabitacionesDisponiblesResponse();
        response.setHotelId(hotelId);
        response.setHabitaciones(HabitacionMapper.toResponseList(habitaciones));

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<HabitacionResponse> crearHabitacion(HabitacionRequest request) {
        TokenValidationResponse auth = AuthUtils.getAuth(this.request);
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!AuthUtils.isAdmin(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Habitacion habitacion = habitacionService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(HabitacionMapper.toResponse(habitacion));
    }

    @Override
    public ResponseEntity<HabitacionResponse> obtenerHabitacion(Long id) {
        Habitacion habitacion = habitacionService.buscarPorId(id);
        return ResponseEntity.ok(HabitacionMapper.toResponse(habitacion));
    }

    @Override
    public ResponseEntity<HabitacionResponse> actualizarHabitacion(Long id, HabitacionRequest request) {
        TokenValidationResponse auth = AuthUtils.getAuth(this.request);
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!AuthUtils.isAdmin(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Habitacion habitacion = habitacionService.actualizar(id, request);
        return ResponseEntity.ok(HabitacionMapper.toResponse(habitacion));
    }

    @Override
    public ResponseEntity<MessageResponse> eliminarHabitacion(Long id) {
        TokenValidationResponse auth = AuthUtils.getAuth(this.request);
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!AuthUtils.isAdmin(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        habitacionService.eliminar(id);
        MessageResponse response = new MessageResponse();
        response.setMessage("Habitacion eliminada");
        response.setTimestamp(java.time.OffsetDateTime.now());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<HabitacionResponse>> listarHabitacionesPorHotel(Long id) {
        List<Habitacion> habitaciones = habitacionService.buscarPorHotelId(id);
        return ResponseEntity.ok(HabitacionMapper.toResponseList(habitaciones));
    }

    @Override
    public ResponseEntity<DisponibilidadResponse> verificarDisponibilidad(Long id) {

        boolean disponible = habitacionService.estaDisponible(id);

        DisponibilidadResponse response = new DisponibilidadResponse();
        response.setHabitacionId(id);
        response.setDisponible(disponible);
        return ResponseEntity.ok(response);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }
}
