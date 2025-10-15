package com.rosanegra.controllers;

import com.rosanegra.entities.JWTRequest;
import com.rosanegra.entities.JWTResponse;
import com.rosanegra.services.JwtService;
import com.rosanegra.services.JwtUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtService jwtService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> postToken(@RequestBody JWTRequest request){
        authenticate(request);
        final var userDetails = jwtUserDetailsService.loadUserByUsername(request.getUsername());
        final var token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new JWTResponse(token));
    }

    private void authenticate(JWTRequest request){
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            }catch (BadCredentialsException | DisabledException e){
                throw new RuntimeException(e.getMessage());
            }
    }
}
