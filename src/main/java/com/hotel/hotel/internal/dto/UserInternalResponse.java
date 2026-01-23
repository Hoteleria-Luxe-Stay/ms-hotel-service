package com.hotel.hotel.internal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de usuario desde auth-service.
 * Solo incluye los campos que este microservicio necesita.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInternalResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
}
