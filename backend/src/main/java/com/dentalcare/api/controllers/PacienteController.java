package com.dentalcare.api.controllers;
import com.dentalcare.api.models.Paciente;
import com.dentalcare.api.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "http://localhost:5173")
public class PacienteController {
    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    public ResponseEntity<List<Paciente>> listarTodos() {
        return ResponseEntity.ok(pacienteRepository.findAll());
    }
}