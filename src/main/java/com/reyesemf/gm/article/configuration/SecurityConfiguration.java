package com.reyesemf.gm.article.configuration;

import com.reyesemf.gm.article.configuration.security.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Configuración de seguridad de Spring Security.
 * 
 * <p>Configura un sistema de autenticación basado en tokens personalizados
 * que valida el header 'x-auth-token' en cada request.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private AuthenticationFilter authenticationFilter;

    /**
     * Configuración principal de la cadena de filtros de seguridad.
     * 
     * <p>Características:
     * <ul>
     *   <li>CSRF deshabilitado (API REST con tokens stateless)</li>
     *   <li>Sesiones STATELESS (sin HttpSession)</li>
     *   <li>Autenticación por token personalizado (x-auth-token)</li>
     *   <li>Permisos validados por @RequiredAction en cada endpoint</li>
     * </ul>
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/**").permitAll() // El filtro AuthorizationFilter valida todos los /api/**
            )
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // Para H2 Console
        
        return http.build();
    }
}
