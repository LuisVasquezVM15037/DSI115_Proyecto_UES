package com.dentalcare.api.controllers;

import com.dentalcare.api.dtos.Odontologo.OdontologoDTO;
import com.dentalcare.api.repositories.OdontologoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/odontologos")
@CrossOrigin(origins = "http://localhost:5173")
public class OdontologoController {

    @Autowired
    private OdontologoRepository odontologoRepository;

    @GetMapping
    @Transactional(readOnly = true) // Muy importante para poder leer los datos del Usuario asociado
    public ResponseEntity<List<OdontologoDTO>> listarTodos() {
        
        List<OdontologoDTO> lista = odontologoRepository.findAll().stream().map(odontologo -> {
            OdontologoDTO dto = new OdontologoDTO();
            dto.setIdOdontologo(odontologo.getIdOdontologo());
            dto.setEspecialidadOdontologo(odontologo.getEspecialidadOdontologo());
            
            // Extraemos el nombre desde la relación con Usuario
            String nombre = odontologo.getUsuario().getNombreUsuario();
            String apellido = odontologo.getUsuario().getApellidoUsuario();
            dto.setNombreCompleto(nombre + " " + apellido);
            
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }
}