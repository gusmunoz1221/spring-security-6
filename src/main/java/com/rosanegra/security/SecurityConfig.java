package com.rosanegra.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.rosanegra.services.CustomerUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Configuration
public class SecurityConfig {

    private static final String[] USER_RESOURCES = {"/loans/**","/balance/**"};
    private static final String[] ADMIN_RESOURCES = {"/accounts/**","/cards/**"};
    private static final String AUTH_WRITE = "write";
    private static final String AUTH_READ = "read";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";
    private static final String LOGIN_RESOURCE = "/login";
    private static final String RSA = "rsa";
    private static final Integer RSA_SIZE = 2048 ;
    private static final String APPLICATION_OWNER = "rosaNegra-security";

    @Bean
    @Order(1)
    SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        var authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http
                // Limita este filtro a los endpoints del Authorization Server
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                // Configura el servidor OAuth2
                .with(authorizationServerConfigurer, configurer ->
                        configurer.oidc(Customizer.withDefaults()) // Habilita OpenID Connect 1.0
                )
                // Requiere autenticación para todos los endpoints de OAuth2
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                // Configura login form básico
                .formLogin(Customizer.withDefaults())
                // Configura manejo de excepciones
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_RESOURCE))
                );

        return http.build();
    }

    // para clientes que consumen APIs con JWT
    @Bean
    @Order(2)
    SecurityFilterChain clientSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**") // Aplica a endpoints de API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ADMIN_RESOURCES).hasAuthority(AUTH_WRITE)
                        .requestMatchers(USER_RESOURCES).hasAuthority(AUTH_READ)
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(Customizer.withDefaults()))
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    @Order(3)
    SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ADMIN_RESOURCES).hasRole(ROLE_ADMIN)
                        .requestMatchers(USER_RESOURCES).hasRole(ROLE_USER)
                        .anyRequest().permitAll()
                )
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    AuthenticationProvider authenticationProvider(PasswordEncoder encoder, CustomerUserDetails userDetails){
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(encoder);
        authProvider.setUserDetailsService(userDetails);
        return authProvider;
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:8080") // Cambiar en producción
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter(){
        var converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        return converter;
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(JwtGrantedAuthoritiesConverter settings){
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(settings);
        return converter;
    }

    @Bean
    JWKSource<SecurityContext> jwkSource(){
        var rsa = generatedKeys();
        var jwkSet = new JWKSet(rsa);
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }

    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource){
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer(){
        //solo cuando es un access token
        return context -> {
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)){
                context.getClaims().claims(claim ->
                        claim.putAll(Map.of(
                                "owner",APPLICATION_OWNER,
                                "date_request", LocalDateTime.now())
                        ));
            }
        };
    }
    private static KeyPair generateRsa() {
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(RSA_SIZE);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error generated by RSA", e);
        }
    }

    private static RSAKey generatedKeys(){
        var keyPair = generateRsa();
        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString()).build();
    }

}

    /*
     * Registra un cliente OAuth2 en memoria para pruebas.
     * Se utiliza para que aplicaciones externas (como Postman o un frontend)
     * puedan autenticarse contra el Authorization Server usando clientId y clientSecret.
    @Bean
    RegisteredClientRepository clientRepository() {
        var client = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("rosa-negra")
                .clientSecret("{noop}secret") // para pruebas; en producción debe ir hasheado
                .scope("read")
                .redirectUri("http://localhost:8080/login/oauth2/code/rosa-negra")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();

        return new InMemoryRegisteredClientRepository(client);
    }
    */