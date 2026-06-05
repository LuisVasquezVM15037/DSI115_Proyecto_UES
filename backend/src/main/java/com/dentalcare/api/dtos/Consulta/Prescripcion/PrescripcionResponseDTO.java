package com.dentalcare.api.dtos.Consulta.Prescripcion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta para Prescripcion.
 * Incluye los detalles aplanados para renderizar la receta en el frontend.
 */
@Data
@AllArgsConstructor
public class PrescripcionResponseDTO {

    private Integer idPrescripcion;
    private Integer idCita;
    private LocalDateTime fechaPrescripcion;

    // Lista de medicamentos con sus indicaciones
    private List<DetallePrescripcionResponseDTO> detalles;

    /**
     * DTO interno de respuesta para cada linea de la prescripcion.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetallePrescripcionResponseDTO {
        private Integer idDetalle;
        private Integer idMedicamento;
        private String nombreMedicamento;
        private String componenteActivo;
        private String concentracion;
        private String dosis;
        private String frecuencia;
        private Integer duracion;
        private String indicaciones;
        
        private Integer idPlanTratamiento;
    }
}
