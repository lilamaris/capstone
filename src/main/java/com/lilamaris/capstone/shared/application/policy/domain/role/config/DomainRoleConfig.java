package com.lilamaris.capstone.shared.application.policy.domain.role.config;

import com.lilamaris.capstone.shared.application.policy.domain.role.defaults.DefaultDomainRoleGraphDefinitionDirectory;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRoleGraphDefinition;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRoleGraphDefinitionDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DomainRoleConfig {
    @Bean
    public DomainRoleGraphDefinitionDirectory roleGraphDefinitionDirectory(
            List<DomainRoleGraphDefinition<?>> definitions
    ) {
        return new DefaultDomainRoleGraphDefinitionDirectory(definitions);
    }
}
