package com.dentalcare.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dentalcare.api.dtos.Cita.CitaCancelacionDTO;
import com.dentalcare.api.dtos.Cita.CitaRequestDTO;
import com.dentalcare.api.dtos.Cita.CitaResponseDTO;
import com.dentalcare.api.services.CitaService;

@RestController
// La anotación @RequestMapping define la ruta base para todas las solicitudes a
// este controlador, en este caso "/api/citas".
@RequestMapping("/api/citas")
@CrossOrigin(origins = "http://localhost:5173") // Permitir solicitudes desde el backend en vite (que corre en otro
                                                // puerto)
public class CitaController {
    // Inyectamos el servicio de Citas para delegar la lógica de negocio a la capa
    // de servicios, manteniendo el controlador enfocado en manejar las solicitudes
    // HTTP y respuestas.
    @Autowired
    private CitaService citaService;

    /**
     * OBTENER TODAS LAS CITAS CON
     * GET http://localhost:8080/api/citas
     */

    // Este método maneja las solicitudes GET a la ruta "/api/citas" y devuelve una
    // lista de todas las citas en formato DTO de respuesta.
    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> listarCitas() {
        List<CitaResponseDTO> citas = citaService.obtenerTodas();
        return ResponseEntity.ok(citas); // Retorna HTTP 200 OK con la lista
    }

    /**
     * REGISTRAR UNA NUEVA CITA
     * POST http://localhost:8080/api/citas
     */
    // Este método maneja las solicitudes POST a la ruta "/api/citas" para crear una
    // nueva cita.
    // Recibe un DTO de solicitud con los datos necesarios para crear la cita,
    // delega la creación al servicio y devuelve el DTO de respuesta con el nuevo
    // objeto creado.
    @PostMapping
    public ResponseEntity<CitaResponseDTO> registrarCita(@RequestBody CitaRequestDTO request) {
        CitaResponseDTO nuevaCita = citaService.crearCita(request);
        // Retorna HTTP 201 Created junto con el objeto creado enriquecido
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }

    /**
     * CANCELAR UNA CITA
     * PUT http://localhost:8080/api/citas/{id}/cancelar
     */
    // Este método maneja las solicitudes PUT a la ruta "/api/citas/{id}/cancelar"
    // para cancelar una cita existente.
    // Recibe el ID de la cita a cancelar como parte de la ruta y un DTO de
    // cancelación con el motivo en el cuerpo de la solicitud,
    // delega la cancelación al servicio y devuelve el DTO de respuesta con la cita
    // actualizada.
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<CitaResponseDTO> cancelarCita(
            @PathVariable Integer id,
            @RequestBody CitaCancelacionDTO cancelacionRequest) {

        // Delegamos al servicio pasando el ID de la ruta y el motivo del body
        CitaResponseDTO citaCancelada = citaService.cancelarCita(id, cancelacionRequest);
        return ResponseEntity.ok(citaCancelada); // Retorna HTTP 200 OK
    }

    // Este método maneja las solicitudes PUT a la ruta "/api/citas/{id}" para actualizar una cita existente.
    // Recibe el ID de la cita a actualizar como parte de la ruta y un DTO de solicitud con los nuevos datos en el cuerpo de la solicitud,
    // delega la actualización al servicio y devuelve el DTO de respuesta con la cita actualizada
    @PutMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> actualizarCita(@PathVariable Integer id,
            @RequestBody CitaRequestDTO request) {
        return ResponseEntity.ok(citaService.actualizarCita(id, request));
    }
}
