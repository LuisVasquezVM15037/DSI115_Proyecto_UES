package com.dentalcare.api.dtos.Paciente;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

/**
 * DTO de respuesta para operaciones sobre Paciente.
 * Controla exactamente que campos se exponen al frontend.
 */
@Data
@AllArgsConstructor
public class PacienteResponseDto {

    private Integer idPaciente;
    private String nombrePaciente;
    private String apellidoPaciente;
    private String telefonoPaciente;
    private LocalDate fechaNacimientoPaciente;
    private String numeroIdentidadPaciente;
    private String emailPaciente;
    private String contactoEmergencia;
    private String alergias;
}
