package com.microcommerice.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // Allow access to health checks, OpenAPI docs
                                .requestMatchers("/api/v1/cart/**").authenticated() // Secure cart endpoints
                                .anyRequest().denyAll() // Deny any other requests by default
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults())) // Configure as OAuth2 Resource Server validating JWTs
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless sessions
                .csrf(AbstractHttpConfigurer::disable); // Disable CSRF for stateless APIs

        return http.build();
    }
}
