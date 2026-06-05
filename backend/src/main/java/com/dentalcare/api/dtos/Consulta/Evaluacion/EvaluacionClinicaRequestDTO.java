package com.dentalcare.api.dtos.Consulta.Evaluacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para crear o actualizar una evaluacion clinica.
 * Se asocia siempre a una cita existente.
 */
@Data
public class EvaluacionClinicaRequestDTO {

    @NotNull(message = "El id de la cita es obligatorio.")
    private Integer idCita;

    @NotBlank(message = "El diagnostico es obligatorio.")
    private String diagnostico;

    // Las observaciones son opcionales segun el modelo
    private String observaciones;
}
