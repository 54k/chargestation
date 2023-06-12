package com.example.chargestation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.nio.charset.Charset;
import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(HttpMethod.POST, "/api/v1/session").hasAuthority("customer")
                .requestMatchers(HttpMethod.GET, "/api/v1/session").hasAuthority("admin")
                .requestMatchers(HttpMethod.GET, "/api/v1/vehicle").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/v1/port").hasAuthority("admin")
                .anyRequest().permitAll()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(LogoutConfigurer::disable)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder(@Value("${app.security.randSeed}") String seed) {
        return new BCryptPasswordEncoder(10, new SecureRandom(seed.getBytes(Charset.defaultCharset())));
    }
}
