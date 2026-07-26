package com.example.BharatMerchantPayments.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.csrf( AbstractHttpConfigurer::disable )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user").permitAll()
                        .requestMatchers("/user/register/**").permitAll()
                        .requestMatchers("/payments/webhook/**").permitAll()   // ONLY the webhook callback
                        .requestMatchers("/user/auth/login, /payments/**").authenticated()        // everything else needs auth
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
