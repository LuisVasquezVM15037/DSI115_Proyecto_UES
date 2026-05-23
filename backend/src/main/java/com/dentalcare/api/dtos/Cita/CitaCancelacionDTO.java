package com.dentalcare.api.dtos.Cita;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data

public class CitaCancelacionDTO {
    @NotBlank(message = "El motivo de cancelación es obligatorio")
    private String motivoCancelacion;
}
