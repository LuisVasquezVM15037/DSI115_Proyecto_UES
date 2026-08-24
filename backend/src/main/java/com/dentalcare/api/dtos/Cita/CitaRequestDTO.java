package com.dentalcare.api.dtos.Cita;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dentalcare.api.models.enums.EstadoCita;

import lombok.Data;

@Data

public class CitaRequestDTO {

    private Integer idOdontologo;
    private Integer idPaciente;
    private LocalDate fechaCita;
    private LocalDateTime horaInicioCita;
    private LocalDateTime horaFinCita;
    private EstadoCita estadoCita;
}
