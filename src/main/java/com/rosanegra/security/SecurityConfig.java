package com.rosanegra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean //lo carga al contenedor de spring
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/loans", "/balance","/accounts","/cards").authenticated()
                        .anyRequest().permitAll() )//->cualquier request que llege tiene que tener autentifiacion.
                        .formLogin(Customizer.withDefaults()) // configura el loggin
                .httpBasic(Customizer.withDefaults());   //configura que el metodo de autentificacion es http basic-> user y password
        return http.build();
    }

}
