//esta clase permite convertir automáticamente entre objetos de diferentes clases
// Evita código repetitivo, Mantiene controllers más limpios y Mejora separación entre base de datos y API
package com.dentalcare.api.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
