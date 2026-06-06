package com.dentalcare.api.dtos.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para el endpoint POST /api/usuarios.
 * Contiene todos los campos requeridos para registrar un nuevo usuario.
 */
@Data
public class CrearUsuarioRequestDto {

    @NotBlank(message = "El nombre es obligatorio.")
    private String nombreUsuario;

    @NotBlank(message = "El apellido es obligatorio.")
    private String apellidoUsuario;

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El formato del email no es valido.")
    private String emailUsuario;

    @NotBlank(message = "El username es obligatorio.")
    private String usernameUsuario;

    @NotBlank(message = "La contrasena es obligatoria.")
    private String password;

    @NotNull(message = "El rol es obligatorio.")
    private Integer idRol;
}
