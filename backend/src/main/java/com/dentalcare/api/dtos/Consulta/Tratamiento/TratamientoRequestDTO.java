package com.dentalcare.api.dtos.Consulta.Tratamiento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO de entrada para crear un nuevo tratamiento desde el modulo de consulta.
 * Permite al odontologo registrar tratamientos que no existan en el catalogo.
 */
@Data
public class TratamientoRequestDTO {

    @NotBlank(message = "El nombre del tratamiento es obligatorio.")
    private String nombreTratamiento;

    @NotBlank(message = "La descripcion es obligatoria.")
    private String descripcionTratamiento;

    @NotNull(message = "El costo es obligatorio.")
    private BigDecimal costoTratamiento;
}