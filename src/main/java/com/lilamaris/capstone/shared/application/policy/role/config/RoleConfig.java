package com.lilamaris.capstone.shared.application.policy.role.config;

import com.lilamaris.capstone.shared.application.policy.role.defaults.DefaultRoleGraphDefinitionDirectory;
import com.lilamaris.capstone.shared.application.policy.role.port.in.RoleGraphDefinition;
import com.lilamaris.capstone.shared.application.policy.role.port.in.RoleGraphDefinitionDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RoleConfig {
    @Bean
    public RoleGraphDefinitionDirectory roleGraphDefinitionDirectory(
            List<RoleGraphDefinition<?>> definitions
    ) {
        var dir = new DefaultRoleGraphDefinitionDirectory();
        definitions.forEach(dir::addDefinition);
        return dir;
    }
}
