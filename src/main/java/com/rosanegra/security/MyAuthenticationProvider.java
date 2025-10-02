package com.rosanegra.security;

import com.rosanegra.repositories.CustomerRespository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@AllArgsConstructor
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
    private final CustomerRespository customerRespository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       final var username = authentication.getName();
       final var psw = authentication.getCredentials().toString();

       final var customerFromDb = this.customerRespository.findByEmail(username);
       final var customer = customerFromDb.orElseThrow(()-> new BadCredentialsException("invalidated credentials"));
       final var customerPwd=  customer.getPassword();

       if(passwordEncoder.matches(psw,customerPwd)){
           final var roles = customer.getRoles();

         // una entity con roles->  Collections.singletonList(new SimpleGrantedAuthority(customer.getRole()));
           final var authorities = roles
                   .stream()
                   .map(role -> new SimpleGrantedAuthority(role.getName()))
                   .toList();
           return new UsernamePasswordAuthenticationToken(username,psw,authorities);
       }else throw new BadCredentialsException("invalidated credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
