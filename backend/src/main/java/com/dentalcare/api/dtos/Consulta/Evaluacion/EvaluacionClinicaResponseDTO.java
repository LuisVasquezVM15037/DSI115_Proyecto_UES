package com.dentalcare.api.dtos.Consulta.Evaluacion;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO de respuesta para EvaluacionClinica.
 * Incluye el id generado para que el frontend pueda referenciar la evaluacion
 * al crear planes de tratamiento asociados.
 */
@Data
@AllArgsConstructor
public class EvaluacionClinicaResponseDTO {

    private Integer idEvaluacionClinica;
    private Integer idCita;
    private String diagnostico;
    private String observaciones;
}
