package com.dentalcare.api.controllers;


import com.dentalcare.api.dtos.Consulta.Evaluacion.EvaluacionClinicaRequestDTO;
import com.dentalcare.api.dtos.Consulta.Evaluacion.EvaluacionClinicaResponseDTO;
import com.dentalcare.api.dtos.Consulta.Medicamento.MedicamentoResponseDTO;
import com.dentalcare.api.dtos.Consulta.Prescripcion.PrescripcionRequestDTO;
import com.dentalcare.api.dtos.Consulta.Prescripcion.PrescripcionResponseDTO;
import com.dentalcare.api.dtos.Consulta.Tratamiento.PlanTratamientoRequestDTO;
import com.dentalcare.api.dtos.Consulta.Tratamiento.PlanTratamientoResponseDTO;
import com.dentalcare.api.dtos.Consulta.Tratamiento.TratamientoRequestDTO;
import com.dentalcare.api.dtos.Consulta.Tratamiento.TratamientoResponseDTO;
import com.dentalcare.api.services.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para el flujo completo de una consulta odontologica.
 *
 * Endpoints:
 *   GET    /api/consulta/tratamientos               - Catalogo de tratamientos
 *   GET    /api/consulta/medicamentos                - Catalogo de medicamentos
 *   POST   /api/consulta/evaluacion                  - Guardar evaluacion clinica (paso 1)
 *   GET    /api/consulta/evaluacion/cita/{idCita}     - Obtener evaluacion de una cita
 *   POST   /api/consulta/hallazgo                     - Registrar hallazgo en odontograma (paso 2)
 *   GET    /api/consulta/hallazgos/{idEvaluacion}     - Obtener hallazgos de una evaluacion
 *   DELETE /api/consulta/hallazgo/{id}               - Eliminar hallazgo
 *   POST   /api/consulta/prescripcion                 - Crear prescripcion con medicamentos (paso 3)
 *   GET    /api/consulta/prescripcion/cita/{idCita}   - Obtener prescripcion de una cita
 */
@RestController
@RequestMapping("/api/consulta")
@CrossOrigin(origins = "http://localhost:5173")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    // --- CATALOGOS ---

    // GET /api/consulta/tratamientos
    @GetMapping("/tratamientos")
    public ResponseEntity<List<TratamientoResponseDTO>> listarTratamientos() {
        return ResponseEntity.ok(consultaService.listarTratamientos());
    }

    // GET /api/consulta/medicamentos
    @GetMapping("/medicamentos")
    public ResponseEntity<List<MedicamentoResponseDTO>> listarMedicamentos() {
        return ResponseEntity.ok(consultaService.listarMedicamentos());
    }

    // --- EVALUACION CLINICA (PASO 1) ---

    // POST /api/consulta/evaluacion
    @PostMapping("/evaluacion")
    public ResponseEntity<?> guardarEvaluacion(@Valid @RequestBody EvaluacionClinicaRequestDTO request) {
        try {
            return ResponseEntity.ok(consultaService.guardarEvaluacion(request));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
        }
    }

    // GET /api/consulta/evaluacion/cita/{idCita}
    @GetMapping("/evaluacion/cita/{idCita}")
    public ResponseEntity<?> obtenerEvaluacion(@PathVariable Integer idCita) {
        EvaluacionClinicaResponseDTO resultado = consultaService.obtenerEvaluacionPorCita(idCita);
        if (resultado == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resultado);
    }

    // --- HALLAZGOS / ODONTOGRAMA (PASO 2) ---

    // POST /api/consulta/hallazgo
    @PostMapping("/hallazgo")
    public ResponseEntity<?> registrarHallazgo(@Valid @RequestBody PlanTratamientoRequestDTO request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.registrarHallazgo(request));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
        }
    }

    // GET /api/consulta/hallazgos/{idEvaluacion}
    @GetMapping("/hallazgos/{idEvaluacion}")
    public ResponseEntity<List<PlanTratamientoResponseDTO>> obtenerHallazgos(@PathVariable Integer idEvaluacion) {
        return ResponseEntity.ok(consultaService.obtenerHallazgosPorEvaluacion(idEvaluacion));
    }

    // DELETE /api/consulta/hallazgo/{id}
    @DeleteMapping("/hallazgo/{id}")
    public ResponseEntity<?> eliminarHallazgo(@PathVariable Integer id) {
        try {
            consultaService.eliminarHallazgo(id);
            return ResponseEntity.ok(Map.of("message", "Hallazgo eliminado."));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
        }
    }

    @PatchMapping("/hallazgo/{id}/estado")
public ResponseEntity<?> actualizarEstadoHallazgo(
        @PathVariable Integer id, 
        @RequestBody Map<String, String> payload) {
    
    String nuevoEstado = payload.get("estado");
    consultaService.actualizarEstadoHallazgo(id, nuevoEstado);
    return ResponseEntity.ok().build();
}

    // --- PRESCRIPCION (PASO 3) ---

    // POST /api/consulta/prescripcion
    @PostMapping("/prescripcion")
    public ResponseEntity<?> crearPrescripcion(@Valid @RequestBody PrescripcionRequestDTO request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.crearPrescripcion(request));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
        }
    }

    // GET /api/consulta/prescripcion/cita/{idCita}
    @GetMapping("/prescripcion/cita/{idCita}")
    public ResponseEntity<?> obtenerPrescripcion(@PathVariable Integer idCita) {
        PrescripcionResponseDTO resultado = consultaService.obtenerPrescripcionPorCita(idCita);
        if (resultado == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resultado);
    }

        // POST /api/consulta/tratamientos - Crear nuevo tratamiento en el catalogo
    @PostMapping("/tratamientos")
    public ResponseEntity<?> crearTratamiento(@Valid @RequestBody TratamientoRequestDTO request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(consultaService.crearTratamiento(request));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}
