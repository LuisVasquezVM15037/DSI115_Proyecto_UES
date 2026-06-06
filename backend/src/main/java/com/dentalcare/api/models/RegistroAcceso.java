//Se agrega en backend este modelo para registrar los intentos de acceso al sistema, sirve para el PBI de revisar accesos*
package com.dentalcare.api.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "registro_acceso")
public class RegistroAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro_acceso")
    private Integer idRegistroAcceso;

    @Column(name = "es_exitoso", nullable = false)
    private Boolean esExitoso;

    @Column(name = "fecha_acceso", nullable = false)
    private LocalDateTime fechaAcceso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}