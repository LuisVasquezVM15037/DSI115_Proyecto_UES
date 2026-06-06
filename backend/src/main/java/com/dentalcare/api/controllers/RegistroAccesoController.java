// Nuevo archivo controlador agregado para el PBI DE REVISAR ACCESOS
// Se crea este controlador para gestionar las solicitudes relacionadas con registroAcceso
package com.dentalcare.api.controllers;

import com.dentalcare.api.dtos.RegistroAcceso.RegistroAccesoDto;
import com.dentalcare.api.repositories.RegistroAccesoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros-acceso")
@CrossOrigin(origins = "http://localhost:5173")
public class RegistroAccesoController {

    private final RegistroAccesoRepository registroAccesoRepository;

    public RegistroAccesoController(RegistroAccesoRepository registroAccesoRepository) {
        this.registroAccesoRepository = registroAccesoRepository;
    }

    @GetMapping
    public List<RegistroAccesoDto> getRegistrosAcceso() {
        return registroAccesoRepository.findAll()
                .stream()
             .map(registro -> new RegistroAccesoDto(
             registro.getIdRegistroAcceso(),
             registro.getEsExitoso(),
             registro.getFechaAcceso(),
             registro.getUsuario().getIdUsuario(),
             registro.getUsuario().getEmailUsuario()
        ))
                .toList();
    }
}