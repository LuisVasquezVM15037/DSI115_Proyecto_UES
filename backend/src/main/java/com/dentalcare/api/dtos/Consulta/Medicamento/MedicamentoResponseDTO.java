package com.dentalcare.api.dtos.Consulta.Medicamento;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO de respuesta para Medicamento.
 * Usado para poblar el selector de medicamentos en el paso de prescripcion.
 */
@Data
@AllArgsConstructor
public class MedicamentoResponseDTO {

    private Integer idMedicamento;
    private String nombreMedicamento;
    private String componenteActivo;
    private String concentracion;
    private BigDecimal costoMedicamento;
    private Integer cantidadInventario;
}
