package com.dentalcare.api.dtos.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para el endpoint PUT /api/usuarios/{id}.
 * La password es opcional: si viene vacia no se actualiza.
 */
@Data
public class ActualizarUsuarioRequestDto {

    @NotBlank(message = "El nombre es obligatorio.")
    private String nombreUsuario;

    @NotBlank(message = "El apellido es obligatorio.")
    private String apellidoUsuario;

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El formato del email no es valido.")
    private String emailUsuario;

    @NotBlank(message = "El username es obligatorio.")
    private String usernameUsuario;

    // Sin @NotBlank: el frontend no la envia si el usuario no quiere cambiarla
    private String password;

    @NotNull(message = "El rol es obligatorio.")
    private Integer idRol;

    @NotNull(message = "El estado es obligatorio.")
    private Boolean esActivo;
}
