package com.dentalcare.api.models;

import com.dentalcare.api.models.enums.EstadoCita;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

//La anotación Data genera automáticamente los getters, setters, toString, equals y hashCode para la clase Cita, lo que reduce el código y mejora la legibilidad.
@Data 
// La anotación Entity indica que esta clase es una entidad de JPA, lo que significa que se mapeará a una tabla en la base de datos
@Entity 
// La anotación Table especifica el nombre de la tabla en la base de datos a la que se mapeará esta entidad.
@Table(name = "cita") 
public class Cita {
    // La anotación Id indica que el campo idCitas es la clave primaria de la entidad.
    @Id
    // La anotación GeneratedValue con la estrategia GenerationType.IDENTITY indica que el valor de idCitas se generará automáticamente por la base de datos, generalmente utilizando un auto-incremento.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCitas;
    // La anotación ManyToOne indica que hay una relación de muchos a uno entre Cita y Odontologo, lo que significa que muchas citas pueden estar asociadas a un solo odontólogo.
    @ManyToOne(fetch = FetchType.LAZY)
    // La anotación JoinColumn especifica la columna en la tabla de la base de datos que se utilizará para unir esta entidad con la entidad Odontologo. 
    // En este caso, se usará la columna id_odontologo, y se establece como no nula (nullable = false), lo que significa que cada cita debe estar asociada a un odontólogo.
    @JoinColumn(name = "id_odontologo", nullable = false)
    private Odontologo odontologo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;
    // La anotación Column con nullable = false indica que el campo fechaCita no puede ser nulo en la base de datos, lo que significa que cada cita debe tener una fecha asociada.
    @Column(nullable = false)
    private LocalDate fechaCita;

    @Column(nullable = false)
    private LocalDateTime horaInicioCita;

    @Column(nullable = false)
    private LocalDateTime horaFinCita;

    private String motivoCancelacion;
    // La anotación Enumerated con EnumType.STRING indica que el valor del campo estadoCita se almacenará como una cadena en la base de datos, lo que facilita la lectura y comprensión de los estados de las citas.
    @Enumerated(EnumType.STRING) //
    @Column(nullable = false)
    private EstadoCita estadoCita;
}