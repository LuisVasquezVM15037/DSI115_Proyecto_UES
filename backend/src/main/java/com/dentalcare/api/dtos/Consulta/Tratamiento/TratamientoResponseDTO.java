package com.dentalcare.api.dtos.Consulta.Tratamiento;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO de respuesta para Tratamiento.
 * Usado para poblar el selector de tratamientos en el panel del odontograma.
 */
@Data
@AllArgsConstructor
public class TratamientoResponseDTO {

    private Integer idTratamiento;
    private String nombreTratamiento;
    private String descripcionTratamiento;
    private BigDecimal costoTratamiento;
}
