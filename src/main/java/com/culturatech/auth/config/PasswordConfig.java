package com.culturatech.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configura el componente encargado de cifrar y verificar contraseñas.
 */
@Configuration
public class PasswordConfig {

    /**
     * Proporciona BCrypt como algoritmo de almacenamiento de contraseñas.
     *
     * @return codificador BCrypt administrado por Spring
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}