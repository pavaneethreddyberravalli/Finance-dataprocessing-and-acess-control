package com.finance.config;


import com.finance.security.JwtAuthenticationFilter;
import com.finance.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity   // Enables @PreAuthorize on controller methods
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * BCrypt password encoder with strength 12.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Wires our UserDetailsService and password encoder into
     * the DAO authentication provider.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    /**
     * Exposes the AuthenticationManager bean so AuthService can inject it.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Returns 401 JSON (not a redirect) when an unauthenticated request
     * hits a protected endpoint.
     */
    @Bean
    public AuthenticationEntryPoint unauthorizedHandler() {
        return (request, response, authException) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(
                    "{\"success\":false,\"error\":\"Unauthorized. Please login.\"}");
        };
    }

    /**
     * Main security filter chain.
     *
     * Public routes:
     *   POST /api/auth/register
     *   POST /api/auth/login
     *   POST /api/auth/refresh
     *   GET  /h2-console/**     (dev only)
     *
     * Everything else requires a valid JWT.
     * Role-specific restrictions are enforced via @PreAuthorize.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF — we're using stateless JWT
                .csrf(csrf -> csrf.disable())

                // Return 401 JSON instead of redirect on auth failure
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedHandler()))

                // Stateless — no HttpSession
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // H2 console requires frames (dev only)
                .headers(headers -> headers.frameOptions().disable())

                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/refresh"
                        ).permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()

                        // Everything else needs authentication
                        // Fine-grained role checks are done via @PreAuthorize
                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider(userDetailsService, passwordEncoder()))

                // Run our JWT filter before Spring's default username/password filter
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


