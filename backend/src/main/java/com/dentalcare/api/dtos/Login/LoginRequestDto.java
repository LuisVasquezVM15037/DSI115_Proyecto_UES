package com.dentalcare.api.dtos.Login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO de entrada para el endpoint POST /api/auth/login.
 * Acepta username o email como identificador unico del usuario.
 */
@Data
public class LoginRequestDto {

    @NotBlank(message = "El campo username no puede estar vacio.")
    private String username;

    @NotBlank(message = "El campo password no puede estar vacio.")
    private String password;
}