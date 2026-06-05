package com.dentalcare.api.dtos.Consulta.Prescripcion;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * DTO de entrada para crear una prescripcion completa con sus detalles.
 * La prescripcion siempre pertenece a una cita y contiene uno o mas medicamentos.
 */
@Data
public class PrescripcionRequestDTO {

    @NotNull(message = "El id de la cita es obligatorio.")
    private Integer idCita;

    // Lista de detalles: cada elemento es un medicamento con dosis e indicaciones
    @NotNull(message = "Debe incluir al menos un medicamento.")
    private List<DetallePrescripcionDTO> detalles;

    /**
     * DTO interno que representa un medicamento dentro de la prescripcion.
     * Clase estatica para mantener todo en un solo archivo.
     */
    @Data
    public static class DetallePrescripcionDTO {

        @NotNull(message = "El id del medicamento es obligatorio.")
        private Integer idMedicamento;

        @NotNull(message = "La dosis es obligatoria.")
        private String dosis;

        @NotNull(message = "La frecuencia es obligatoria.")
        private String frecuencia;

        @NotNull(message = "La duracion es obligatoria.")
        private Integer duracion;

        @NotNull(message = "Las indicaciones son obligatorias.")
        private String indicaciones;

        private Integer idPlanTratamiento;
    }
}
