package com.riegelnick.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
    // Disable CSRF (not needed for stateless JWT APIs)
    .csrf(csrf -> csrf.disable())
    
    // Configure which endpoints require authentication
    .authorizeHttpRequests(auth -> auth
        // Public endpoints - anyone can access
        .requestMatchers("/api/auth/**").permitAll()  // login, register
        .requestMatchers("/api/cars").permitAll()     // view cars
        .requestMatchers("/api/images/**").permitAll() // view images
        .requestMatchers("/api/home").permitAll()     // home endpoint
        
        // Protected endpoints - need authentication
        .requestMatchers("/api/cars/upload-with-image").authenticated()
        .requestMatchers("/api/cars/upload-without-image").authenticated()
        
        // All other requests require authentication by default
        .anyRequest().authenticated()
    )
    
    // Stateless session - don't store sessions on server
    .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    )
    
    // Add our JWT filter before Spring's default authentication filter
    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}