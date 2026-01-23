package com.hotel.hotel.api;

import com.hotel.hotel.api.dto.DisponibilidadResponse;
import com.hotel.hotel.api.dto.HabitacionRequest;
import com.hotel.hotel.api.dto.HabitacionResponse;
import com.hotel.hotel.api.dto.HabitacionesDisponiblesResponse;
import com.hotel.hotel.core.habitacion.model.Habitacion;
import com.hotel.hotel.core.habitacion.service.HabitacionService;
import com.hotel.hotel.helpers.mappers.HabitacionMapper;
import com.hotel.hotel.internal.AuthInternalApi;
import com.hotel.hotel.internal.dto.TokenValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class HabitacionesController implements HabitacionesApi {

    private final HabitacionService habitacionService;
    private final AuthInternalApi authInternalApi;
    private final NativeWebRequest request;

    public HabitacionesController(HabitacionService habitacionService,
                                  AuthInternalApi authInternalApi,
                                  NativeWebRequest request) {
        this.habitacionService = habitacionService;
        this.authInternalApi = authInternalApi;
        this.request = request;
    }

    @Override
    public ResponseEntity<HabitacionesDisponiblesResponse> listarHabitacionesDisponibles(
            Long hotelId,
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        List<Habitacion> habitaciones = habitacionService.buscarDisponibles(hotelId, fechaInicio, fechaFin);

        HabitacionesDisponiblesResponse response = new HabitacionesDisponiblesResponse();
        response.setHotelId(hotelId);
        response.setFechaInicio(fechaInicio);
        response.setFechaFin(fechaFin);
        response.setHabitaciones(HabitacionMapper.toResponseList(habitaciones));

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<HabitacionResponse> crearHabitacion(HabitacionRequest request) {
        TokenValidationResponse auth = resolveAuth();
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!isAdmin(auth)) {
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
    public ResponseEntity<List<HabitacionResponse>> listarHabitacionesPorHotel(Long id) {
        List<Habitacion> habitaciones = habitacionService.buscarPorHotelId(id);
        return ResponseEntity.ok(HabitacionMapper.toResponseList(habitaciones));
    }

    @Override
    public ResponseEntity<DisponibilidadResponse> verificarDisponibilidad(
            Long id,
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        boolean disponible = habitacionService.estaDisponible(id, fechaInicio, fechaFin);

        DisponibilidadResponse response = new DisponibilidadResponse();
        response.setHabitacionId(id);
        response.setDisponible(disponible);
        response.setFechaInicio(fechaInicio);
        response.setFechaFin(fechaFin);
        return ResponseEntity.ok(response);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    private TokenValidationResponse resolveAuth() {
        String authorization = resolveAuthorization();
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        String token = authorization.substring(7);
        TokenValidationResponse response = authInternalApi.validateToken(token).orElse(null);
        if (response == null || !Boolean.TRUE.equals(response.getValid())) {
            return null;
        }
        return response;
    }

    private String resolveAuthorization() {
        Optional<NativeWebRequest> request = getRequest();
        if (request.isEmpty()) {
            return null;
        }
        return request.get().getHeader("Authorization");
    }

    private boolean isAdmin(TokenValidationResponse auth) {
        return auth.getRole() != null && "ADMIN".equalsIgnoreCase(auth.getRole());
    }
}
