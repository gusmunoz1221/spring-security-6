package com.rosanegra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity//es opcional ponerla.
public class RosaNegraSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(RosaNegraSecurityApplication.class, args);
	}
}
