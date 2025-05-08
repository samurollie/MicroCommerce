package com.microcommerice.customers.security;

import com.microcommerice.customers.security.jwt.AuthTokenFilter;
import com.microcommerice.customers.security.services.AuthEntryPointJwt;
import com.microcommerice.customers.security.services.UserDetailsServiceImpl;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    UserDetailsServiceImpl userDetailsService;

    private final AuthEntryPointJwt unauthorizedHandler;

    public WebSecurityConfig(AuthEntryPointJwt unauthorizedHandler, UserDetailsServiceImpl userDetailsService) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.userDetailsService = userDetailsService;
    }

    // Creates and returns an AuthTokenFilter bean, which is responsible for
    // processing JWT authentication tokens in the security filter chain.
    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    // Configures and returns a DaoAuthenticationProvider bean,
    // which uses the custom UserDetailsService and a password encoder to
    // authenticate users against the database.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    // Exposes the AuthenticationManager bean, which is used to process
    // authentication requests.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Creates and returns a BCryptPasswordEncoder bean, which is used to hash and
    // verify user passwords securely.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configures the main security filter chain for the application.
    // - Disables CSRF protection (suitable for stateless REST APIs).
    // - Sets up custom exception handling for unauthorized access.
    // - Configures session management to be stateless (no HTTP session is created).
    // - Defines authorization rules for different endpoints (public, role-based,
    // and authenticated).
    // - Registers the custom authentication provider and JWT filter in the filter
    // chain.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOriginPattern("http://localhost:[*]"); // Add your frontend URL here
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
                    config.setExposedHeaders(Arrays.asList("Authorization"));
                    config.setAllowCredentials(true);
                    config.setMaxAge(3600L); // 1 hour cache for preflight requests
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(
                        org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers(
                                "/api/auth/**",
                                "/auth/**",
                                "/api/test/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v2/api-docs/**",
                                "/api-docs/**",
                                "/webjars/**",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security")
                        .permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/users/**", "/addresses/**").authenticated()
                        .requestMatchers("/api/services/**").hasRole("SERVICE")
                        .anyRequest().authenticated());
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}