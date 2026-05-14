package com.lilamaris.capstone.bootstrap.security.customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

@FunctionalInterface
public interface AuthorizeHttpRequestCustomizer {
    void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>
                           .AuthorizationManagerRequestMatcherRegistry auth);
}
