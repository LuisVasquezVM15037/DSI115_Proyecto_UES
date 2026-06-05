package com.dentalcare.api.dtos.Consulta.Tratamiento;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO de respuesta para PlanTratamiento.
 * Aplana las relaciones para facilitar la renderizacion en el panel de hallazgos.
 */
@Data
@AllArgsConstructor
public class PlanTratamientoResponseDTO {

    private Integer idPlanTratamiento;
    private Integer piezaDental;
    private String estadoPlan;

    // Datos del tratamiento aplanados para evitar objetos anidados
    private Integer idTratamiento;
    private String nombreTratamiento;
    private String descripcionTratamiento;
    private Float precioFloat;
}
