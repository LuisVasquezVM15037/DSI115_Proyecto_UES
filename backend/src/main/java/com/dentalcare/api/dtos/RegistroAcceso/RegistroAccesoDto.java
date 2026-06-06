//Archivo agregado para el pbi de revisar accesos, se crea este DTO para manejar la información que se va a enviar al frontend sobre los registros de acceso
package com.dentalcare.api.dtos.RegistroAcceso;

import java.time.LocalDateTime;

public class RegistroAccesoDto {

    private Integer id_registro_acceso;
    private Boolean es_exitoso;
    private LocalDateTime fecha_acceso;
    private Integer id_usuario;
    private String email_usuario;

    public RegistroAccesoDto(Integer id_registro_acceso, Boolean es_exitoso, LocalDateTime fecha_acceso, Integer id_usuario, String email_usuario) {
        this.id_registro_acceso = id_registro_acceso;
        this.es_exitoso = es_exitoso;
        this.fecha_acceso = fecha_acceso;
        this.id_usuario = id_usuario;
        this.email_usuario = email_usuario;
    }

    public Integer getId_registro_acceso() {
        return id_registro_acceso;
    }

    public Boolean getEs_exitoso() {
        return es_exitoso;
    }

    public LocalDateTime getFecha_acceso() {
        return fecha_acceso;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public String getEmail_usuario() {
        return email_usuario;
    }
}