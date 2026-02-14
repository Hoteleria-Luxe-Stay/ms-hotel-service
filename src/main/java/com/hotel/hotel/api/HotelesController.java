package com.hotel.hotel.api;

import com.hotel.hotel.api.dto.HotelDetalleResponse;
import com.hotel.hotel.api.dto.HotelRequest;
import com.hotel.hotel.api.dto.HotelResponse;
import com.hotel.hotel.api.dto.MessageResponse;
import com.hotel.hotel.core.hotel.model.Hotel;
import com.hotel.hotel.core.hotel.service.HotelService;
import com.hotel.hotel.helpers.auth.AuthUtils;
import com.hotel.hotel.helpers.mappers.HotelMapper;
import com.hotel.hotel.internal.dto.TokenValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class HotelesController implements HotelesApi {

    private final HotelService hotelService;
    private final NativeWebRequest request;

    public HotelesController(HotelService hotelService, NativeWebRequest request) {
        this.hotelService = hotelService;
        this.request = request;
    }

    @Override
    public ResponseEntity<List<HotelResponse>> listarHoteles(Long departamentoId) {
        List<Hotel> hoteles = departamentoId == null
                ? hotelService.listarHoteles()
                : hotelService.listarPorDepartamentoId(departamentoId);

        List<HotelResponse> response = hoteles.stream()
                .map(HotelMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<HotelResponse> crearHotel(HotelRequest request) {
        TokenValidationResponse auth = AuthUtils.getAuth(this.request);
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!AuthUtils.isAdmin(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Hotel hotel = hotelService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(HotelMapper.toResponse(hotel));
    }

    @Override
    public ResponseEntity<HotelDetalleResponse> obtenerHotel(Long id) {
        Hotel hotel = hotelService.buscarPorId(id);
        return ResponseEntity.ok(HotelMapper.toDetalleResponse(hotel));
    }

    @Override
    public ResponseEntity<HotelResponse> actualizarHotel(Long id, HotelRequest request) {
        TokenValidationResponse auth = AuthUtils.getAuth(this.request);
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!AuthUtils.isAdmin(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Hotel hotel = hotelService.actualizar(id, request);
        return ResponseEntity.ok(HotelMapper.toResponse(hotel));
    }

    @Override
    public ResponseEntity<MessageResponse> eliminarHotel(Long id) {
        TokenValidationResponse auth = AuthUtils.getAuth(this.request);
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!AuthUtils.isAdmin(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        hotelService.eliminar(id);

        MessageResponse response = new MessageResponse();
        response.setMessage("Hotel eliminado correctamente");
        response.setTimestamp(OffsetDateTime.now());

        return ResponseEntity.ok(response);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }
}
