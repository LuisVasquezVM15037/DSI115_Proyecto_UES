package com.dentalcare.api.dtos.Odontologo;

import lombok.Data;

@Data
public class OdontologoDTO {
    private Integer idOdontologo;
    private String nombreCompleto; // Uniremos el nombre y apellido del usuario aquí
    private String especialidadOdontologo;
}