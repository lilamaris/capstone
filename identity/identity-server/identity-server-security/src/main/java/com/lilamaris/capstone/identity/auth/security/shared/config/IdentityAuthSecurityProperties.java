package com.lilamaris.capstone.identity.auth.security.shared.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@ConfigurationProperties(prefix = "identity.security")
public record IdentityAuthSecurityProperties(
        @DefaultValue()
        List<@NotBlank String> allowedOrigins,

        @DefaultValue()
        List<@NotBlank String> allowedMethods,

        @DefaultValue()
        List<@NotBlank String> allowedHeaders,

        @DefaultValue("true")
        Boolean allowCredentials,

        @DefaultValue()
        List<@NotBlank String> exposedHeaders
) {
}
