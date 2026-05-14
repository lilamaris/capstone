package com.lilamaris.capstone.identity.auth.application.jwks.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "identity.jwt")
public record IssueJwtProperties(
        @DefaultValue("capstone")
        @NotBlank
        String issuer,

        @DefaultValue("PT15M")
        @NotNull
        Duration expiration
) {
}
