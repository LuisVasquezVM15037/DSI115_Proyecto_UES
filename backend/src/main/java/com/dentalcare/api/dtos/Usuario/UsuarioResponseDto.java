package com.dentalcare.api.dtos.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO de respuesta para operaciones sobre Usuario.
 * Nunca expone la password, ni siquiera el hash.
 */
@Data
@AllArgsConstructor
public class UsuarioResponseDto {

    private Integer idUsuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String emailUsuario;
    private String usernameUsuario;
    private Boolean esActivo;
    private String rol;
}
