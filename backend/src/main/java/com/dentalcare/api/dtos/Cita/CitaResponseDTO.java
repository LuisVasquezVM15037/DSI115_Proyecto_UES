package com.dentalcare.api.dtos.Cita;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dentalcare.api.models.enums.EstadoCita;

import lombok.Data;

@Data
public class CitaResponseDTO {

    private Integer idCitas;
    private LocalDate fechaCita;
    private LocalDateTime horaInicioCita;
    private LocalDateTime horaFinCita;
    private EstadoCita estadoCita;
    private String motivoCancelacion;
    
    // Datos "aplanados" de las relaciones para facilitar la renderización
    private Integer idPaciente;
    private String nombreCompletoPaciente;   //concatenación de nombre y apellido
    private String numeroIdentidadPaciente; //concatenación de nombre y apellido
    
    private Integer idOdontologo;
    private String especialidadOdontologo;  
}
