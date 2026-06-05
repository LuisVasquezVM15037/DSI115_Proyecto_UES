package com.dentalcare.api.dtos.Odontologo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO de respuesta para Odontologo.
 * Aplana la relacion con Usuario para no exponer objetos anidados al frontend.
 */
@Data
@AllArgsConstructor
public class OdontologoResponseDto {

    private Integer idOdontologo;
    private String especialidadOdontologo;
    private String jvpoId;

    // Datos del usuario asociado, aplanados para facilitar la renderizacion en el select
    private String nombreCompleto;
}