package com.lilamaris.capstone.identity.auth.application.shared.config;

import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import com.lilamaris.capstone.identity.core.role.InitialUserGrantedRoleProvider;
import com.lilamaris.capstone.identity.core.role.ProviderBasedInitialUserGrantedRoleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ServiceLoader;

@Configuration
public class RoleConfiguration {
    @Bean
    ProviderBasedInitialUserGrantedRoleRegistry initialUserGrantedRoleRegistry() {
        var providers = ServiceLoader.load(InitialUserGrantedRoleProvider.class).stream()
                .map(ServiceLoader.Provider::get)
                .toList();

        return new ProviderBasedInitialUserGrantedRoleRegistry(providers, CanonicalRole.GUEST);
    }
}
