package com.rosanegra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.Optional;

public class ApiKey extends OncePerRequestFilter {
    //deben ser variables de entorno
    private static final String API_KEY= "mykey";
    private static final String API_KEY_HEADER= "api_key";


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            final var apiKeyOptional = Optional.of(request.getHeader(API_KEY_HEADER));
            final var apiKey = apiKeyOptional.orElseThrow(()-> new BadCredentialsException("No header API key"));

            if(!apiKey.equals(API_KEY))
                throw new BadCredentialsException("Invalid API key");

        } catch (Exception e) {
            throw new BadCredentialsException("Invalid API key");
        }

        filterChain.doFilter(request,response);
    }
}
