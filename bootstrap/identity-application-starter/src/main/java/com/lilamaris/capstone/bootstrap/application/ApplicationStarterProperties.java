package com.lilamaris.capstone.bootstrap.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.ZoneId;

@Validated
@ConfigurationProperties(prefix = "capstone.bootstrap.application")
public record ApplicationStarterProperties(
        @DefaultValue("true")
        boolean enabled,

        @DefaultValue("UTC")
        ZoneId timezone
) {
}
