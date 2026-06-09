package com.dentalcare.api.dtos.Cita;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dentalcare.api.models.enums.EstadoCita;

import lombok.Data;
//Esta anotación Data genera automáticamente los getters, setters.
@Data

//Un DTO (Data Transfer Object) es un objeto que se utiliza para transferir datos entre diferentes capas de una aplicación,
//  como entre el controlador y el servicio.

//CitaRequestDTO es un DTO que se utiliza para recibir los datos necesarios para crear o actualizar una cita.
public class CitaRequestDTO {

    private Integer idOdontologo;
    private Integer idPaciente;
    private LocalDate fechaCita;
    private LocalDateTime horaInicioCita;
    private LocalDateTime horaFinCita;
    private EstadoCita estadoCita;
}
