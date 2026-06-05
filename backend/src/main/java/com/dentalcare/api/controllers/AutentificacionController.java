package com.dentalcare.api.controllers;

import com.dentalcare.api.dtos.Login.LoginRequestDto;
import com.dentalcare.api.dtos.Login.LoginResponseDto;
import com.dentalcare.api.services.AutentificacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para autenticacion de usuarios.
 * Expone un unico endpoint publico: POST /api/auth/login
 */
@RestController
@RequestMapping("/api/auth")
// Permite peticiones desde el servidor de desarrollo del frontend (Vite por defecto)
// En produccion, reemplaza el origen con el dominio real del frontend
@CrossOrigin(origins = "http://localhost:5173")
public class AutentificacionController {

    private final AutentificacionService authService;

    public AutentificacionController(AutentificacionService authService) {
        this.authService = authService;
    }

    /**
     * Autentica al usuario y retorna un JWT.
     *
     * POST /api/auth/login
     * Body: { "username": "...", "password": "..." }
     *
     * @param request DTO validado por @Valid con las credenciales
     * @return 200 OK con el token, 401 si las credenciales son incorrectas
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
        try {
            LoginResponseDto response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            // Retornamos 401 con un mensaje de error estructurado como JSON
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}