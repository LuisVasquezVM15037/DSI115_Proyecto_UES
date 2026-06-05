package com.dentalcare.api.services;

import com.dentalcare.api.dtos.Login.LoginRequestDto;
import com.dentalcare.api.dtos.Login.LoginResponseDto;
import com.dentalcare.api.models.Usuario;
import com.dentalcare.api.repositories.UsuarioRepository;
import com.dentalcare.api.security.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio que encapsula la logica de autenticacion de usuarios.
 * No expone detalles de implementacion al controlador.
 */
@Service
public class AutentificacionService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Inyeccion por constructor: practica recomendada sobre @Autowired en campos
    public AutentificacionService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder   = passwordEncoder;
        this.jwtUtil           = jwtUtil;
    }

    /**
     * Valida las credenciales del usuario y genera un JWT si son correctas.
     *
     * @param request DTO con username/email y password en texto plano
     * @return DTO con el token JWT y datos basicos del usuario
     * @throws RuntimeException con mensaje de error si la autenticacion falla
     */
    public LoginResponseDto login(LoginRequestDto request) {

        // Buscamos al usuario por username o email (la query acepta ambos)
        Usuario usuario = usuarioRepository
                .findByUsernameOrEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas."));

        // Verificamos que la cuenta este activa antes de cualquier otra validacion
        if (!usuario.getEsActivo()) {
            throw new RuntimeException("La cuenta se encuentra desactivada. Contacta al administrador.");
        }

        // Comparamos la password en texto plano contra el hash BCrypt almacenado en BD
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassworUsuario())) {
            // Mensaje generico intencional: no indicar si el error es el usuario o la password
            throw new RuntimeException("Credenciales incorrectas.");
        }

        // Obtenemos el nombre del rol como String para incluirlo en el JWT
        String nombreRol = usuario.getRol().getNombreRol().name();

        // Generamos el JWT firmado con los datos del usuario autenticado
        String token = jwtUtil.generateToken(usuario.getUsernameUsuario(), nombreRol);

        // Construimos el nombre completo para mostrar en el frontend
        String nombreCompleto = usuario.getNombreUsuario() + " " + usuario.getApellidoUsuario();

        return new LoginResponseDto(token, nombreCompleto, nombreRol);
    }
}