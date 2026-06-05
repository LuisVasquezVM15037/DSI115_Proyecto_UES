package com.dentalcare.api.dtos.Paciente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

/**
 * DTO de entrada para los endpoints POST y PUT de /api/pacientes.
 * Separa la entidad del contrato de la API para mayor seguridad y flexibilidad.
 */
@Data
public class PacienteRequestDto {

    @NotBlank(message = "El nombre es obligatorio.")
    private String nombrePaciente;

    @NotBlank(message = "El apellido es obligatorio.")
    private String apellidoPaciente;

    // Telefono opcional segun el modelo
    private String telefonoPaciente;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    private LocalDate fechaNacimientoPaciente;

    @NotBlank(message = "El numero de identidad es obligatorio.")
    private String numeroIdentidadPaciente;

    @Email(message = "El formato del email no es valido.")
    private String emailPaciente;

    // Contacto de emergencia opcional segun el modelo
    private String contactoEmergencia;

    // Campo adicional para alergias que maneja el frontend
    // Se puede agregar a la entidad Paciente si se desea persistirlo
    private String alergias;
}