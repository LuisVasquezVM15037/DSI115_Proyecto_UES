package com.dentalcare.api.dtos.Consulta.Tratamiento;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para registrar un hallazgo (plan de tratamiento) en el odontograma.
 * Cada hallazgo referencia una evaluacion clinica, un tratamiento y la pieza dental.
 */
@Data
public class PlanTratamientoRequestDTO {

    @NotNull(message = "El id de la evaluacion clinica es obligatorio.")
    private Integer idEvaluacionClinica;

    @NotNull(message = "El id del tratamiento es obligatorio.")
    private Integer idTratamiento;

    // Numero de pieza dental en notacion FDI (ej. 11, 21, 36)
    @NotNull(message = "La pieza dental es obligatoria.")
    private Integer piezaDental;

    // Estado inicial del plan: por defecto PENDIENTE si no se envia
    private String estadoPlan;

    private Float precioFloat;
}
