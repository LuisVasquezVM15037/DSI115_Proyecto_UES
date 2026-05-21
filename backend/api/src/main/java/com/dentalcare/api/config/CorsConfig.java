// Control de acceso desde el frontend
// Permite que el frontend pueda acceder a la API desde el backend
// Sin esto el navegador bloquea la petición 
package com.dentalcare.api.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //aca definimos que es accesible desde el frontend y de cualquier origen con cualquier metodo de acceso
        source.registerCorsConfiguration("/api/**", config);

        return source;
    }
}