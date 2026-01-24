package com.lilamaris.capstone.shared.application.jsonPatch.config;

import com.lilamaris.capstone.shared.application.jsonPatch.DomainJsonResolver;
import com.lilamaris.capstone.shared.application.jsonPatch.DomainJsonResolverDirectory;
import com.lilamaris.capstone.shared.application.jsonPatch.defaults.DefaultDomainJsonResolverDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class JsonPatchEngineConfig {
    @Bean
    DomainJsonResolverDirectory jsonPatchResolverDirectory(
            List<DomainJsonResolver> resolvers
    ) {
        return new DefaultDomainJsonResolverDirectory(resolvers);
    }
}
