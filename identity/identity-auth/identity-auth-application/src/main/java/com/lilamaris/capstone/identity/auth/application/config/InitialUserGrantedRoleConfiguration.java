package com.lilamaris.capstone.identity.auth.application.config;

import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import com.lilamaris.capstone.identity.core.role.InitialUserGrantedRoleProvider;
import com.lilamaris.capstone.identity.core.role.ProviderBasedInitialUserGrantedRoleRegistry;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class InitialUserGrantedRoleConfiguration {
    @Bean
    ProviderBasedInitialUserGrantedRoleRegistry initialUserGrantedRoleRegistry(List<InitialUserGrantedRoleProvider> providers) {
        return new ProviderBasedInitialUserGrantedRoleRegistry(providers, CanonicalRole.GUEST);
    }
}
