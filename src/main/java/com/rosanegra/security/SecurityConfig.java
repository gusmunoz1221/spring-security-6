package com.rosanegra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;
import javax.xml.crypto.Data;

@Configuration
public class SecurityConfig {

    @Bean//lo carga al contenedor de spring
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/loans", "/balance","/accounts","/cards").authenticated()
                        .anyRequest().permitAll() )//->cualquier request que llege tiene que tener autentifiacion.
                        .formLogin(Customizer.withDefaults()) // configura el loggin
                .httpBasic(Customizer.withDefaults());   //configura que el metodo de autentificacion es http basic-> user y password
        return http.build();
    }

    // BCrypt lo brinda spring security
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

/*
    // Crea un UserDetailsService que usa JDBC para buscar usuarios y roles
    // directamente en la base de datos a través del DataSource proporcionado.
    @Bean
    UserDetailsService userDetailsService(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }

    //usuarios en memoria
    //sirve  cuando tenemos apps pequeñas.
    @Bean
    InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        UserDetails admin = User.withUsername("admin")
                .password("to_be_encoded")
                .authorities("ADMIN")
                .build();

        UserDetails user = User.withUsername("user")
                .password("to_be_encoded")
                .authorities("USER")
                .build();
        return new InMemoryUserDetailsManager(admin,user);

    //sirve cuando estamos desarrollando la seguridad desde cero. ya que la encriptacion toma tiempo
    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
    } */


}
