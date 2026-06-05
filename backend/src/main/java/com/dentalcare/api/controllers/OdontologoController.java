package com.dentalcare.api.controllers;

import com.dentalcare.api.dtos.Odontologo.OdontologoResponseDto;
import com.dentalcare.api.models.Odontologo;
import com.dentalcare.api.repositories.OdontologoRepository;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Controlador REST para consulta de odontologos.
 * Por ahora solo expone el listado necesario para poblar
 * el select del formulario de citas en el frontend.
 *
 * GET /api/odontologos - Listar todos los odontologos
 */

@RestController
@RequestMapping("/api/odontologos")
@CrossOrigin(origins = "http://localhost:5173")
public class OdontologoController {
 
    private final OdontologoRepository odontologoRepository;
 
    public OdontologoController(OdontologoRepository odontologoRepository) {
        this.odontologoRepository = odontologoRepository;
    }
 
    // GET /api/odontologos - Retorna todos los odontologos con datos aplanados
    @GetMapping
    public ResponseEntity<List<OdontologoResponseDto>> listarOdontologos() {
        List<OdontologoResponseDto> response = odontologoRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
 
    // Convierte la entidad Odontologo a su DTO aplanando la relacion con Usuario
    private OdontologoResponseDto toDto(Odontologo o) {
        String nombreCompleto = o.getUsuario() != null
                ? o.getUsuario().getNombreUsuario() + " " + o.getUsuario().getApellidoUsuario()
                : "Sin asignar";
        return new OdontologoResponseDto(
                o.getIdOdontologo(),
                o.getEspecialidadOdontologo(),
                o.getJvpoId(),
                nombreCompleto
        );
    }
}