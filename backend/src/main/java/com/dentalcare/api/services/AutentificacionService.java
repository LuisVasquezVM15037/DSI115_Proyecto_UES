package com.dentalcare.api.services;
import com.dentalcare.api.models.RegistroAcceso; //Import agregado para el pbi revisar accesos, se importa el modelo para poder crear registros al momento de logearse
import com.dentalcare.api.repositories.RegistroAccesoRepository; //Import del repositorio para manejar el registro de accesos, se agg para PBI revisar accesos
import java.time.LocalDateTime;  // Import para manejar fechas se agrega para PBI revisar accesos
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
    
    private final RegistroAccesoRepository registroAccesoRepository; //Se agrega para el PBI revisar accesos 

    // Inyeccion por constructor: practica recomendada sobre @Autowired en campos
    public AutentificacionService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                    RegistroAccesoRepository registroAccesoRepository) { //Modificación del constructor para inyectar el repo de registro de accesos, se agg para el PBI revisar accesos
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder   = passwordEncoder;
        this.jwtUtil           = jwtUtil;
        this.registroAccesoRepository = registroAccesoRepository;
    }

    // Se crea el método para guardar acceso para el PBI revisar accesos 
    private void registrarAcceso(Usuario usuario, boolean exitoso) {
    RegistroAcceso registroAcceso = new RegistroAcceso();
    registroAcceso.setUsuario(usuario);
    registroAcceso.setEsExitoso(exitoso);
    registroAcceso.setFechaAcceso(LocalDateTime.now());

    registroAccesoRepository.save(registroAcceso);
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
            registrarAcceso(usuario, false); //Se modifica el met. login para registrar el intento de acceso fallido PBI revisar accesos. 
            throw new RuntimeException("La cuenta se encuentra desactivada. Contacta al administrador.");
        }

        // Comparamos la password en texto plano contra el hash BCrypt almacenado en BD
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassworUsuario())) {
              registrarAcceso(usuario, false); // Se agrega para PBI revisar accesos
            // Mensaje generico intencional: no indicar si el error es el usuario o la password
            throw new RuntimeException("Credenciales incorrectas.");
        }

        registrarAcceso(usuario, true); //Se agrega para PBI revisar accesos, registrar el acceso exitoso.

        // Obtenemos el nombre del rol como String para incluirlo en el JWT
        String nombreRol = usuario.getRol().getNombreRol().name();

        // Generamos el JWT firmado con los datos del usuario autenticado
        String token = jwtUtil.generateToken(usuario.getUsernameUsuario(), nombreRol);

        // Construimos el nombre completo para mostrar en el frontend
        String nombreCompleto = usuario.getNombreUsuario() + " " + usuario.getApellidoUsuario();

        return new LoginResponseDto(token, nombreCompleto, nombreRol);
    }
}