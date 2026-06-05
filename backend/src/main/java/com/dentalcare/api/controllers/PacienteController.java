package com.dentalcare.api.controllers;

import com.dentalcare.api.dtos.Paciente.PacienteRequestDto;
import com.dentalcare.api.dtos.Paciente.PacienteResponseDto;
import com.dentalcare.api.services.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestion de pacientes.
 *
 * GET    /api/pacientes           - Listar todos los pacientes
 * GET    /api/pacientes/buscar    - Buscar por nombre, apellido o DUI
 * POST   /api/pacientes           - Crear nuevo paciente
 * PUT    /api/pacientes/{id}      - Actualizar paciente existente
 * DELETE /api/pacientes/{id}      - Eliminar paciente
 */
@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "http://localhost:5173")
public class PacienteController {

    private final PacienteService pacienteService;

    // Inyeccion por constructor en lugar de @Autowired en campo
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // GET /api/pacientes - Listar todos
    @GetMapping
    public ResponseEntity<List<PacienteResponseDto>> listarTodos() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    // GET /api/pacientes/buscar?term=xxx - Busqueda en tiempo real desde el frontend
    @GetMapping("/buscar")
    public ResponseEntity<List<PacienteResponseDto>> buscar(@RequestParam String term) {
        return ResponseEntity.ok(pacienteService.buscar(term));
    }

    // POST /api/pacientes - Crear paciente
    @PostMapping
    public ResponseEntity<?> crearPaciente(@Valid @RequestBody PacienteRequestDto request) {
        try {
            PacienteResponseDto response = pacienteService.crearPaciente(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // PUT /api/pacientes/{id} - Actualizar paciente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPaciente(@PathVariable Integer id,
                                                @Valid @RequestBody PacienteRequestDto request) {
        try {
            PacienteResponseDto response = pacienteService.actualizarPaciente(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // DELETE /api/pacientes/{id} - Eliminar paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPaciente(@PathVariable Integer id) {
        try {
            pacienteService.eliminarPaciente(id);
            return ResponseEntity.ok(Map.of("message", "Paciente eliminado correctamente."));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}