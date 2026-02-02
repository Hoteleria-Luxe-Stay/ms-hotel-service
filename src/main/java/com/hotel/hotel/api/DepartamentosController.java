package com.hotel.hotel.api;

import com.hotel.hotel.api.dto.DepartamentoRequest;
import com.hotel.hotel.api.dto.DepartamentoResponse;
import com.hotel.hotel.core.departamento.model.Departamento;
import com.hotel.hotel.core.departamento.service.DepartamentoService;
import com.hotel.hotel.helpers.mappers.DepartamentoMapper;
import com.hotel.hotel.internal.dto.TokenValidationResponse;
import com.hotel.hotel.infrastructure.security.AuthContextFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

import java.util.List;
import java.util.Optional;

@RestController
public class DepartamentosController implements DepartamentosApi {

    private final DepartamentoService departamentoService;
    private final NativeWebRequest request;

    public DepartamentosController(DepartamentoService departamentoService,
                                   NativeWebRequest request) {
        this.departamentoService = departamentoService;
        this.request = request;
    }

    @Override
    public ResponseEntity<List<DepartamentoResponse>> listarDepartamentos() {
        List<Departamento> departamentos = departamentoService.listar();
        return ResponseEntity.ok(DepartamentoMapper.toResponseList(departamentos));
    }

    @Override
    public ResponseEntity<DepartamentoResponse> crearDepartamento(DepartamentoRequest request) {
        TokenValidationResponse auth = getAuth();
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!isAdmin(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Departamento departamento = departamentoService.crear(request);
        DepartamentoResponse response = DepartamentoMapper.toResponse(departamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<DepartamentoResponse> obtenerDepartamento(Long id) {
        Departamento departamento = departamentoService.buscarPorId(id);
        return ResponseEntity.ok(DepartamentoMapper.toResponse(departamento));
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    private TokenValidationResponse getAuth() {
        Optional<NativeWebRequest> request = getRequest();
        if (request.isEmpty()) {
            return null;
        }
        Object value = request.get().getAttribute(AuthContextFilter.AUTH_CONTEXT_KEY, RequestAttributes.SCOPE_REQUEST);
        if (value instanceof TokenValidationResponse response) {
            return response;
        }
        return null;
    }

    private boolean isAdmin(TokenValidationResponse auth) {
        return auth.getRole() != null && "ADMIN".equalsIgnoreCase(auth.getRole());
    }
}
