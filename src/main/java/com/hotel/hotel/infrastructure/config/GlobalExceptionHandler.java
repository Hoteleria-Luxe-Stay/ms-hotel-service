package com.hotel.hotel.infrastructure.config;

import com.hotel.hotel.helpers.errors.ApiErrorResponse;
import com.hotel.hotel.helpers.exceptions.EntityNotFoundException;
import com.hotel.hotel.helpers.exceptions.ServiceUnavailableException;
import com.hotel.hotel.helpers.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(ValidationException exception) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setMessage(exception.getMessage());
        response.setFieldErrors(exception.getFieldErrors());
        response.setTimestamp(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(EntityNotFoundException exception) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setMessage(exception.getMessage());
        response.setTimestamp(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ApiErrorResponse> handleServiceUnavailable(ServiceUnavailableException exception) {
        LOGGER.error("Dependent service unavailable: {}", exception.getServiceName(), exception);
        ApiErrorResponse response = new ApiErrorResponse();
        response.setMessage("Servicio dependiente no disponible: " + exception.getServiceName() + ". Intente de nuevo en unos segundos.");
        response.setTimestamp(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception exception) {
        LOGGER.error("Unexpected error", exception);
        ApiErrorResponse response = new ApiErrorResponse();
        response.setMessage("Error interno del servidor");
        response.setTimestamp(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
