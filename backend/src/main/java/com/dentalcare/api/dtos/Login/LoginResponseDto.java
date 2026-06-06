package com.dentalcare.api.dtos.Login;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO de respuesta tras autenticacion exitosa.
 * Contiene el JWT y datos basicos del usuario para el frontend.
 */
@Data
@AllArgsConstructor
public class LoginResponseDto {

    // Token JWT que el cliente debe enviar en el header Authorization: Bearer <token>
    private String token;

    private String nombreCompleto;

    // Nombre del rol (ej. "ADMIN", "DOCTOR", "RECEPCIONISTA")
    private String rol;
}