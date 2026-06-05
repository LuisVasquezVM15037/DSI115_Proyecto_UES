package com.dentalcare.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion central de Spring Security.
 *
 * Dependencia requerida en pom.xml:
 *   <dependency>
 *     <groupId>org.springframework.boot</groupId>
 *     <artifactId>spring-boot-starter-security</artifactId>
 *   </dependency>
 *
 * NOTA: Por ahora la configuracion es permisiva (permitAll) para facilitar
 * el desarrollo. Una vez que el flujo de login funcione correctamente,
 * el siguiente paso es agregar el JwtAuthFilter para proteger los
 * endpoints privados (ver comentario al final de este archivo).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define el algoritmo de hashing de passwords.
     * BCrypt con factor de costo 10 (balance entre seguridad y rendimiento).
     * Este bean es inyectado en AuthService para verificar passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cadena de filtros de seguridad HTTP.
     *
     * Configuracion actual (FASE DE DESARROLLO):
     * - CSRF deshabilitado: la API es stateless y usa JWT, no sesiones de formulario
     * - Sesiones deshabilitadas: STATELESS porque el estado vive en el JWT del cliente
     * - Todos los endpoints publicos temporalmente para probar el login
     *
     * Para proteger rutas privadas, agregar el filtro JWT:
     *   .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
     *   .authorizeHttpRequests(auth -> auth
     *       .requestMatchers("/api/auth/**").permitAll()
     *       .anyRequest().authenticated()
     *   )
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // .cors(Customizer.withDefaults())

            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // En fase de desarrollo, permitimos todo para no bloquear las pruebas
                .anyRequest().permitAll()
            );

        return http.build();
    }
}