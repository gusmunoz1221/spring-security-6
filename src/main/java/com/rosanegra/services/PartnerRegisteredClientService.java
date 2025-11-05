package com.rosanegra.services;

import com.rosanegra.repositories.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Arrays;


/*service para obtener el objet de la BDD*/
@Service
@AllArgsConstructor
public class PartnerRegisteredClientService implements RegisteredClientRepository {

    private PartnerRepository partnerRepository;

    @Override
    public RegisteredClient findByClientId(String clientId) {
        var partnerOpt = partnerRepository.findByClientId(clientId);
        return partnerOpt.map(partner -> {
            // sin refresh token
            // var authorizationGrandTypes = Arrays.stream(partner.getGrantTypes().split(","))
            //         .map(AuthorizationGrantType::new)
            //         .toList();
            var authorizationGrandTypes = Arrays
                .stream(partner.getGrantTypes().split(","))
                .map(grantType -> {
                    if(grantType.equalsIgnoreCase("refresh_token"))
                        return AuthorizationGrantType.REFRESH_TOKEN;

                    return new AuthorizationGrantType(grantType);
                }).toList();

            var clientAuthenticationMethods = Arrays.stream(partner.getAuthenticationMethods().split(","))
                    .map(ClientAuthenticationMethod::new)
                    .toList();
            var scopes = Arrays.stream(partner.getScopes().split(",")).toList();

            var registeredClientBuilder = RegisteredClient
                    .withId(partner.getId().toString())
                    .clientId(partner.getClientId())
                    .clientSecret(partner.getClientSecret())
                    .clientName(partner.getClientName())
                    .redirectUri(partner.getRedirectUri())
                    .postLogoutRedirectUri(partner.getRedirectUriLogout())
                    //.clientAuthenticationMethod(clientAuthenticationMethods.get(0))
                    //.clientAuthenticationMethod(clientAuthenticationMethods.get(1))
                    //.scope(scopes.get(1))
                    //.scope(scopes.get(0))
                    .authorizationGrantType(authorizationGrandTypes.get(0))
                    .authorizationGrantType(authorizationGrandTypes.get(1))
                    .tokenSettings(tokenSettings());
                    //.build();
            /*agregamos refresh* añadiendo de manera dinamica los elementos de la lista qe tenemos de manera static*/
            clientAuthenticationMethods.forEach(registeredClientBuilder::clientAuthenticationMethod);
            scopes.forEach(registeredClientBuilder::scope);
            authorizationGrandTypes.forEach(registeredClientBuilder::authorizationGrantType);
            return registeredClientBuilder.build();
        }).orElseThrow(() -> new BadCredentialsException("client not exist"));
    }

    @Override
    public void save(RegisteredClient registeredClient) {}

    @Override
    public RegisteredClient findById(String id){return null;}

    private TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofHours(8))
                .refreshTokenTimeToLive(Duration.ofDays(7))
                .reuseRefreshTokens(true)
                .build();
    }

}