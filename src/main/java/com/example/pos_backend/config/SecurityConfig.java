package com.example.pos_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Şifreleyiciyi sisteme tanıtıyoruz
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Oturumlar artık biletli (Stateless)
                .authorizeHttpRequests(auth -> auth
                        // --- 1. KRİTİK DÜZELTME: BURASI ARTIK AKTİF ---
                        // Resim klasörüne (uploads) gelen isteklerde kimlik sorma!
                        .requestMatchers("/uploads/**").permitAll()

                        // Giriş ve şifre işlemleri de herkese açık
                        .requestMatchers("/login", "/forgot-password", "/reset-password").permitAll()

                        // Bunların dışındaki her şey için Token zorunlu
                        .anyRequest().authenticated()
                );

        // Bizim yazdığımız filtreyi standart güvenlikten önceye koy
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}