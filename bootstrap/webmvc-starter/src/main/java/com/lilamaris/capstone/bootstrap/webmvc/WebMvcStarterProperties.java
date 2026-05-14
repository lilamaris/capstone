package com.lilamaris.capstone.bootstrap.webmvc;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.net.URI;

@Validated
@ConfigurationProperties(prefix = "capstone.bootstrap.webmvc")
public record WebMvcStarterProperties(
        @DefaultValue("true")
        boolean enabled,

        @Valid
        @DefaultValue
        @NonNull
        Error error
) {
    public record Error(
            @DefaultValue("https://capstone.com/errors")
            @NonNull
            URI typeBaseUri
    ) {
    }
}
