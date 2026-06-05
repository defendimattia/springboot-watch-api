package it.defendimattia.backenddemo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import it.defendimattia.backenddemo.security.jwt.JwtAuthenticationFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .authorizeHttpRequests(auth -> auth

                        // swagger pubblico
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // login/register pubblici
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/auth/register").permitAll()

                        // USER e ADMIN solo chiamate GET
                        .requestMatchers(HttpMethod.GET, "/api/watches/**").hasAnyRole("USER", "ADMIN")

                        // solo ADMIN chiamate POST, PATCH, DELETE
                        .requestMatchers(HttpMethod.POST, "/api/watches/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/watches/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/watches/**").hasRole("ADMIN")

                        .anyRequest().authenticated());


        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}