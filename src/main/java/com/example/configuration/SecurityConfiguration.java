package com.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

/* it configures spring security to :
- use JWT for authenticating users (no session)
- allow frontend apps (via CORS which allows cross-origin requests from frontend (e.g.localhost, production URLs)
- protect API endpoints */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter)
    {
        this.authenticationProvider=authenticationProvider;
        this.jwtAuthenticationFilter=jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) //disables CSRF (npt needed for stateless JWT)
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/auth/**").permitAll() //public endpoints: allows public access to "/auth/**" endpoints (signup, login, verify) without authentication
                                .anyRequest().authenticated() //secured endpoints: requires authentication ( a valid JWT token) for all other endpoints
                        //.anyRequest().permitAll()
                )
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //uses stateless sessions (no HTTP sessions) (JWT-based) are stored server-side
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); //adds the JWT filter before spring security's default login filter to process JWTs to validate token
        return http.build();
    }

    /*
    configures CORS for frontend integration
    allows cross-origin requests from: http://localhost:8080
    allows HTTP methods: GET, POST, PUT, DELETE
    allows headers: Authorization, Content-Type
     */

@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:8080"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
}