package com.dentalcare.api.config;

import com.dentalcare.api.models.*;
import com.dentalcare.api.models.enums.*;
import com.dentalcare.api.repositories.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//Este seeder perimte hacer un llenado de Pacientes, Odontologo, Cita, Usuariosy Roles
//En caso de que hayan datos en alguna tabla de la BD no hace nada y se salta el método
@Component
public class DataSeeder implements ApplicationRunner {

    private final RolRepository        rolRepository;
    private final UsuarioRepository    usuarioRepository;
    private final OdontologoRepository odontologoRepository;
    private final PacienteRepository   pacienteRepository;
    private final CitaRepository       citaRepository;
    private final PasswordEncoder      passwordEncoder;

    public DataSeeder(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            OdontologoRepository odontologoRepository,
            PacienteRepository pacienteRepository,
            CitaRepository citaRepository,
            PasswordEncoder passwordEncoder) {

        this.rolRepository        = rolRepository;
        this.usuarioRepository    = usuarioRepository;
        this.odontologoRepository = odontologoRepository;
        this.pacienteRepository   = pacienteRepository;
        this.citaRepository       = citaRepository;
        this.passwordEncoder      = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<Rol>        roles       = seedRoles();
        List<Usuario>    usuarios    = seedUsuarios(roles);
        List<Odontologo> odontologos = seedOdontologos(usuarios);
        List<Paciente>   pacientes   = seedPacientes();
                                       seedCitas(odontologos, pacientes);
    }

    // ── 1. ROLES ──────────────────────────────────────────────────────────────
    private List<Rol> seedRoles() {
        if (rolRepository.count() > 0) return rolRepository.findAll();

        Rol admin = new Rol(); admin.setNombreRol(NombreRol.ADMIN);
        Rol odont = new Rol(); odont.setNombreRol(NombreRol.ODONTOLOGO);
        Rol recep = new Rol(); recep.setNombreRol(NombreRol.RECEPCIONISTA);

        List<Rol> saved = rolRepository.saveAll(List.of(admin, odont, recep));
        System.out.println("✅ Roles insertados");
        return saved;
    }

    // ── 2. USUARIOS ───────────────────────────────────────────────────────────
    private List<Usuario> seedUsuarios(List<Rol> roles) {
        if (usuarioRepository.count() > 0) return usuarioRepository.findAll();

        List<Usuario> usuarios = List.of(
            crearUsuario("admin",       "admin@dentalcare.com",     "Admin123!",  "Carlos", "García",   roles.get(0), true),
            crearUsuario("dr.martinez", "martinez@dentalcare.com",  "Odont123!",  "Luis",   "Martínez", roles.get(1), true),
            crearUsuario("recepcion1",  "recepcion@dentalcare.com", "Recep123!",  "Ana",    "López",    roles.get(2), true)
        );

        List<Usuario> saved = usuarioRepository.saveAll(usuarios);
        System.out.println("✅ Usuarios insertados");
        return saved;
    }

    private Usuario crearUsuario(String username, String email, String password,
                                  String nombre, String apellido, Rol rol, Boolean activo) {
        Usuario u = new Usuario();
        u.setUsernameUsuario(username);
        u.setEmailUsuario(email);
        u.setPassworUsuario(passwordEncoder.encode(password));
        u.setNombreUsuario(nombre);
        u.setApellidoUsuario(apellido);
        u.setRol(rol);
        u.setEsActivo(activo);
        return u;
    }

    // ── 3. ODONTÓLOGOS ────────────────────────────────────────────────────────
    private List<Odontologo> seedOdontologos(List<Usuario> usuarios) {
        if (odontologoRepository.count() > 0) return odontologoRepository.findAll();

        Odontologo o1 = new Odontologo();
        o1.setUsuario(usuarios.get(0));
        o1.setEspecialidadOdontologo("Ortodoncia");
        o1.setJvpoId("JVPO-001");

        Odontologo o2 = new Odontologo();
        o2.setUsuario(usuarios.get(1));
        o2.setEspecialidadOdontologo("Endodoncia");
        o2.setJvpoId("JVPO-002");

        Odontologo o3 = new Odontologo();
        o3.setUsuario(usuarios.get(2));
        o3.setEspecialidadOdontologo("Periodoncia");
        o3.setJvpoId("JVPO-003");

        List<Odontologo> saved = odontologoRepository.saveAll(List.of(o1, o2, o3));
        System.out.println("✅ Odontólogos insertados");
        return saved;
    }

    // ── 4. PACIENTES ──────────────────────────────────────────────────────────
    private List<Paciente> seedPacientes() {
        if (pacienteRepository.count() > 0) return pacienteRepository.findAll();

        Paciente p1 = new Paciente();
        p1.setNombrePaciente("María");
        p1.setApellidoPaciente("Hernández");
        p1.setTelefonoPaciente("7111-2222");
        p1.setFechaNacimientoPaciente(LocalDate.of(1990, 3, 15));
        p1.setNumeroIdentidadPaciente("01234567-8");
        p1.setEmailPaciente("maria@gmail.com");
        p1.setContactoEmergencia("Pedro Hernández 7999-0001");
        p1.setAlergias("Ninguna conocida");

        Paciente p2 = new Paciente();
        p2.setNombrePaciente("Roberto");
        p2.setApellidoPaciente("Castillo");
        p2.setTelefonoPaciente("7333-4444");
        p2.setFechaNacimientoPaciente(LocalDate.of(1985, 7, 22));
        p2.setNumeroIdentidadPaciente("09876543-2");
        p2.setEmailPaciente("roberto@gmail.com");
        p2.setContactoEmergencia("Laura Castillo 7888-0002");
        p2.setAlergias("Penicilina");

        Paciente p3 = new Paciente();
        p3.setNombrePaciente("Sofía");
        p3.setApellidoPaciente("Ramírez");
        p3.setTelefonoPaciente("7555-6666");
        p3.setFechaNacimientoPaciente(LocalDate.of(2000, 11, 5));
        p3.setNumeroIdentidadPaciente("05678901-3");
        p3.setEmailPaciente("sofia@gmail.com");
        p3.setContactoEmergencia("Carlos Ramírez 7777-0003");
        p3.setAlergias("Ibuprofeno");

        List<Paciente> saved = pacienteRepository.saveAll(List.of(p1, p2, p3));
        System.out.println("✅ Pacientes insertados");
        return saved;
    }

    // ── 5. CITAS ──────────────────────────────────────────────────────────────
    private void seedCitas(List<Odontologo> odontologos, List<Paciente> pacientes) {
        if (citaRepository.count() > 0) return;

        Cita c1 = new Cita();
        c1.setOdontologo(odontologos.get(0));
        c1.setPaciente(pacientes.get(0));
        c1.setFechaCita(LocalDate.of(2026, 5, 25));
        c1.setHoraInicioCita(LocalDateTime.of(2025, 6, 10, 9, 0));
        c1.setHoraFinCita(LocalDateTime.of(2025, 6, 10, 9, 30));
        c1.setEstadoCita(EstadoCita.FINALIZADA);

        Cita c2 = new Cita();
        c2.setOdontologo(odontologos.get(1));
        c2.setPaciente(pacientes.get(1));
        c2.setFechaCita(LocalDate.of(2026, 5, 21));
        c2.setHoraInicioCita(LocalDateTime.of(2025, 6, 12, 10, 0));
        c2.setHoraFinCita(LocalDateTime.of(2025, 6, 12, 11, 0));
        c2.setEstadoCita(EstadoCita.FINALIZADA);

        Cita c3 = new Cita();
        c3.setOdontologo(odontologos.get(2));
        c3.setPaciente(pacientes.get(2));
        c3.setFechaCita(LocalDate.of(2026, 6, 1));
        c3.setHoraInicioCita(LocalDateTime.of(2025, 6, 15, 14, 0));
        c3.setHoraFinCita(LocalDateTime.of(2025, 6, 15, 15, 0));
        c3.setEstadoCita(EstadoCita.PROGRAMADA);

        citaRepository.saveAll(List.of(c1, c2, c3));
        System.out.println("✅ Citas insertadas");
    }
}