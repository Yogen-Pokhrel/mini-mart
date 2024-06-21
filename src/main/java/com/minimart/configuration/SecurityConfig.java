package com.minimart.configuration;

import com.amazonaws.services.managedgrafana.model.Role;
import com.minimart.auth.filter.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JWTFilter jwtFilter;

    public SecurityConfig() {
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "api/v1/auth/**").permitAll()
                                .requestMatchers("/api/**").authenticated()
//                        .requestMatchers("/admin/**", "*/api/v1/**", "/api/v1/users").hasAuthority(Role.ADMIN.name())
//                        .requestMatchers("/posts/**", "/users/**").hasAnyAuthority(Role.ADMIN.name(), Role.EDITOR.name())
                )
                .csrf(AbstractHttpConfigurer::disable); // spring boot enables csrf by default which blocks POST PUT PATCH requests

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}