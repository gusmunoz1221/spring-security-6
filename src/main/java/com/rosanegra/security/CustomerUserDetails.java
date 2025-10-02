package com.rosanegra.security;

import com.rosanegra.repositories.CustomerRespository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Transactional
@Service
public class CustomerUserDetails /*implements UserDetailsService*/ {
   /* private final CustomerRespository customerRespository;


    //el metodo se llama cuando alguien intenta autenticarse.
    //recibe el username --> email.
    //devuelve un objeto UserDetails que Spring usa para verificar contraseña, roles, etc.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRespository.findByEmail(username)
                .map(customer -> {
                    var authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));
                    return new User(customer.getEmail(),customer.getPassword(),authorities);
                }).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

*/
}
