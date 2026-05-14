package com.lilamaris.capstone.bootstrap.security.customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@FunctionalInterface
public interface OAuth2ResourceServerCustomizer {
    void customize(OAuth2ResourceServerConfigurer<HttpSecurity> oauth);
}
