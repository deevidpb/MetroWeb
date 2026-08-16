package com.onion.metro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            @Value("${frontend.url}") String frontUrl
    ){

        http
                .cors(cors -> cors.configurationSource(request -> {

                    var config = new CorsConfiguration();

                    config.setAllowedOrigins(List.of(frontUrl));
                    config.setAllowedMethods(List.of(
                            "GET",
                            "OPTIONS"
                    ));
                    config.setAllowedHeaders(List.of("*"));
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/v3/api-docs/**", "/swagger-ui.html",
                                "/swagger-ui/**", "/actuator/health").permitAll()
                        .anyRequest().denyAll()
                );

        return http.build();
    }
}