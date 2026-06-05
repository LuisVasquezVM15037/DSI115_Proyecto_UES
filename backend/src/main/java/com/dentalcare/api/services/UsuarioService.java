package com.dentalcare.api.services;

import com.dentalcare.api.dtos.Usuario.CrearUsuarioRequestDto;
import com.dentalcare.api.dtos.Usuario.ActualizarUsuarioRequestDto;
import com.dentalcare.api.dtos.Usuario.UsuarioResponseDto;
import com.dentalcare.api.models.Rol;
import com.dentalcare.api.models.Usuario;
import com.dentalcare.api.repositories.RolRepository;
import com.dentalcare.api.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREAR usuario
    public UsuarioResponseDto crearUsuario(CrearUsuarioRequestDto request) {

        if (usuarioRepository.findByUsernameUsuario(request.getUsernameUsuario()).isPresent())
            throw new RuntimeException("El username '" + request.getUsernameUsuario() + "' ya esta en uso.");

        if (usuarioRepository.findByEmailUsuario(request.getEmailUsuario()).isPresent())
            throw new RuntimeException("El email '" + request.getEmailUsuario() + "' ya esta registrado.");

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("El rol con id " + request.getIdRol() + " no existe."));

        Usuario nuevo = new Usuario();
        nuevo.setNombreUsuario(request.getNombreUsuario());
        nuevo.setApellidoUsuario(request.getApellidoUsuario());
        nuevo.setEmailUsuario(request.getEmailUsuario());
        nuevo.setUsernameUsuario(request.getUsernameUsuario());
        nuevo.setPassworUsuario(passwordEncoder.encode(request.getPassword()));
        nuevo.setEsActivo(true);
        nuevo.setRol(rol);

        return toResponseDto(usuarioRepository.save(nuevo));
    }

    // EDITAR usuario
    public UsuarioResponseDto actualizarUsuario(Integer id, ActualizarUsuarioRequestDto request) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id " + id + " no encontrado."));

        // Validamos unicidad de username solo si cambio respecto al valor actual
        if (!usuario.getUsernameUsuario().equals(request.getUsernameUsuario()) &&
                usuarioRepository.findByUsernameUsuario(request.getUsernameUsuario()).isPresent()) {
            throw new RuntimeException("El username '" + request.getUsernameUsuario() + "' ya esta en uso.");
        }

        // Validamos unicidad de email solo si cambio respecto al valor actual
        if (!usuario.getEmailUsuario().equals(request.getEmailUsuario()) &&
                usuarioRepository.findByEmailUsuario(request.getEmailUsuario()).isPresent()) {
            throw new RuntimeException("El email '" + request.getEmailUsuario() + "' ya esta registrado.");
        }

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("El rol con id " + request.getIdRol() + " no existe."));

        usuario.setNombreUsuario(request.getNombreUsuario());
        usuario.setApellidoUsuario(request.getApellidoUsuario());
        usuario.setEmailUsuario(request.getEmailUsuario());
        usuario.setUsernameUsuario(request.getUsernameUsuario());
        usuario.setEsActivo(request.getEsActivo());
        usuario.setRol(rol);

        // Solo actualizamos la password si el frontend envio un valor no vacio
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPassworUsuario(passwordEncoder.encode(request.getPassword()));
        }

        return toResponseDto(usuarioRepository.save(usuario));
    }

    // ELIMINAR usuario - en realidad lo desactiva si tiene dependencias
    public void eliminarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id " + id + " no encontrado."));

        // En lugar de borrar, desactivamos para preservar integridad referencial
        usuario.setEsActivo(false);
        usuarioRepository.save(usuario);
    }

    // LISTAR todos los usuarios
    public List<UsuarioResponseDto> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // Convierte entidad a DTO de respuesta (sin exponer password)
    private UsuarioResponseDto toResponseDto(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getApellidoUsuario(),
                usuario.getEmailUsuario(),
                usuario.getUsernameUsuario(),
                usuario.getEsActivo(),
                usuario.getRol().getNombreRol().name());
    }
}
