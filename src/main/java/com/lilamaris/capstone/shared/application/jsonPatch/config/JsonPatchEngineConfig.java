package com.lilamaris.capstone.shared.application.jsonPatch.config;

import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchResolver;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchResolverDirectory;
import com.lilamaris.capstone.shared.application.jsonPatch.defaults.DefaultJsonPatchResolverDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class JsonPatchEngineConfig {
    @Bean
    JsonPatchResolverDirectory jsonPatchResolverDirectory(
            List<JsonPatchResolver<?>> resolvers
    ) {
        return new DefaultJsonPatchResolverDirectory(resolvers);
    }
}
