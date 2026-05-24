package com.projeto_aluguelCarro.aluguelCarro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configurações gerais da aplicação.
 * Define beans reutilizáveis injetados nos services que precisam deles.
 */
@Configuration
public class AppConfig {

    /**
     * Encoder BCrypt para hash e verificação de senhas.
     * O strength padrão (10) aplica 2^10 iterações — seguro e razoável em performance.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
