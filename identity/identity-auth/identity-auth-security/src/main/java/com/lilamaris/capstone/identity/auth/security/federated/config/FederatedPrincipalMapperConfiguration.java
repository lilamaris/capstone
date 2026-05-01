package com.lilamaris.capstone.identity.auth.security.federated.config;

import com.lilamaris.capstone.identity.auth.security.federated.principal.GithubOAuth2PrincipalMapper;
import com.lilamaris.capstone.identity.auth.security.federated.principal.GoogleOidcPrincipalMapper;
import com.lilamaris.capstone.identity.auth.security.federated.registry.FederatedPrincipalMapper;
import com.lilamaris.capstone.identity.auth.security.federated.registry.FederatedPrincipalMapperRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FederatedPrincipalMapperConfiguration {
    @Bean
    GoogleOidcPrincipalMapper googleOidcPrincipalMapper() {
        return new GoogleOidcPrincipalMapper();
    }

    @Bean
    GithubOAuth2PrincipalMapper githubOAuth2PrincipalMapper() {
        return new GithubOAuth2PrincipalMapper();
    }

    @Bean
    FederatedPrincipalMapperRegistry federatedPrincipalMapperRegistry(List<FederatedPrincipalMapper> mappers) {
        return new FederatedPrincipalMapperRegistry(mappers);
    }
}
